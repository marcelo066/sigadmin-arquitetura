/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 28/10/2004
 */
package br.ufrn.arq.parametrizacao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import br.ufrn.arq.dominio.Parametro;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.ConfiguracaoAmbienteException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe Utilit�ria para buscar par�metros.
 *
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class ParametroHelper {

	/* Inst�ncia da classe */
	private static ParametroHelper singleton = new ParametroHelper();

	/* Cache com os valores dos par�metros */
	private static Map<String, String> cache = new HashMap<String, String>();

	/* Tempo da �ltima vez em que o valor do par�metro foi buscado */
	private static Map<String, Long> cacheTime = new HashMap<String, Long>();
	
	/* Cache com o tempo m�ximo em que o cache de par�metros � v�lido */
	private static Map<String, Integer> cacheMaxTime = new HashMap<String, Integer>();

	/* Tempo m�ximo default para o cache dos par�metros. */
	private static final int CACHE_TIME = 5*60*1000;

	private ParametroHelper() {
	}

	public static ParametroHelper getInstance() {
		return singleton;
	}

	/**
	 * Busca um par�metro com o c�digo informado para o sistema informado.
	 *
	 * @param codigo
	 * @param sistema
	 * @return
	 */
	public String getParametro(String codigo) {
		// verifica tempo do cache

		if (cache.get(codigo) != null) {
			Long tempo = cacheTime.get(codigo);
			Integer tempoMaximo = cacheMaxTime.get(codigo);
			
			if ( System.currentTimeMillis() - tempo > tempoMaximo ) {
				cache.put(codigo, null);
			}
		}

		if (cache.get(codigo) == null) {
			ParametroDao dao = new ParametroDao();
			try {
				Map<String, Object> mapa = dao.getParametroAsMap(codigo);
				if (mapa != null) { 
					String parametro = (String) mapa.get("valor");
					Integer tempoMaximo = (Integer) mapa.get("tempo_maximo");
					
					if (parametro == null)
						throw new ConfiguracaoAmbienteException("Par�metro n�o definido. � necess�rio ter esse par�metro para que o sistema funcione corretamente. C�digo do par�metro: " + codigo + ".");
					if (tempoMaximo == null)
						tempoMaximo = CACHE_TIME;
					
					cache.put(codigo, parametro);
					cacheTime.put(codigo, System.currentTimeMillis());
					cacheMaxTime.put(codigo, tempoMaximo);
					
					return parametro;
				} else {
					throw new ConfiguracaoAmbienteException("Par�metro n�o definido. � necess�rio ter esse par�metro para que o sistema funcione corretamente. C�digo do par�metro: " + codigo + ".");
				}
			} finally {
				dao.close();
			}
		} else {
			return cache.get(codigo);
		}
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado, convertido
	 * para int.
	 *
	 * @param codigo
	 * @return
	 */
	public int getParametroInt(String codigo) {
		String param = getParametro(codigo);
		
		try {
			return Integer.parseInt(param.trim());			
		} catch(NumberFormatException e) {
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser num�rico, mas o seu valor est� \"" + param + "\".");			
		}
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado, convertido
	 * para um array de ints.
	 * @param codigo
	 * @return
	 */
	public int[] getParametroIntArray(String codigo) {
		String[] partes = getParametroStringArray(codigo);
		int[] result = new int[partes.length];

		try {
			int i = 0;
			for (String p : partes)
				result[i++] = Integer.parseInt(p.trim());
			return result;	
		} catch(NumberFormatException e) {
			String valor = getParametro(codigo);
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser uma lista de n�meros separados por v�rgulas, mas o seu valor est� \"" + valor + "\".");			
		}
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado, convertido
	 * para um array de Integer.
	 * @param codigo
	 * @return
	 */
	public Integer[] getParametroIntegerArray(String codigo) {
		String[] partes = getParametroStringArray(codigo);
		Integer[] result = new Integer[partes.length];
		
		try {
			int i = 0;
			for (String p : partes)
				result[i++] = Integer.parseInt(p.trim());
			return result;	
		} catch(NumberFormatException e) {
			String valor = getParametro(codigo);
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser uma lista de n�meros separados por v�rgulas, mas o seu valor est� \"" + valor + "\".");			
		}
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado, convertido
	 * para um array de Strings.
	 * @param codigo
	 * @return
	 */
	public String[] getParametroStringArray(String codigo) {
		String param = getParametro(codigo);
		return param.split(",");
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado, convertido
	 * para int.
	 *
	 * @param codigo
	 * @return
	 */
	public long getParametroLong(String codigo) {
		String param = getParametro(codigo);
		
		try {
			return Long.parseLong(param);	
		} catch(NumberFormatException e) {
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser num�rico, mas o seu valor est� \"" + param + "\".");			
		}
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado no formato
	 * double.
	 *
	 * @param codigo
	 * @return
	 */
	public double getParametroDouble(String codigo) {
		String param = getParametro(codigo);
		
		try {
			return Double.parseDouble(param);	
		} catch(NumberFormatException e) {
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser num�rico decimal, mas o seu valor est� \"" + param + "\".");			
		}
		
	}

	/**
	 * Esse m�todo retorna o par�metro que possui o c�digo passado no formato
	 * double.
	 *
	 * @param codigo
	 * @return
	 */
	public Date getParametroDate(String codigo) {
		String param = getParametro(codigo);
		
		try {
			return new Date(Long.parseLong(param));	
		} catch(NumberFormatException e) {
			cache.remove(codigo);
			throw new ConfiguracaoAmbienteException("O par�metro de c�digo \"" + codigo + "\" deveria ser um valor num�rico, mas o seu valor est� \"" + param + "\".");			
		}
	}

	/**
	 * Retorna o par�metro que possui o c�digo informado no formato boolean.
	 *
	 * @param bloqueioConsolidacaoAvaliacao
	 * @return
	 */
	public boolean getParametroBoolean(String codigo) {
		String param = getParametro(codigo);
		return new Boolean(param);
	}

	/**
	 * Retorna o par�metro que possui o c�digo informado no formato Map.
	 * @param codigo
	 * @return
	 */
	public Map<String, String> getParametroMap(String codigo) {
		String param = getParametro(codigo);
		Map<String, String> result = new HashMap<String, String>();
		
		for (String linha : param.split("\n")) {
			String[] partes = linha.split("=");
			result.put(partes[0].trim(), partes[1].trim());
		}
		
		return result;
	}
	
	/**
	 * Atualiza um par�metro no banco de dados e o remove do cache.
	 * @param usuario
	 * @param sistema
	 * @param codigo
	 * @param valor
	 * @throws NegocioException
	 * @throws ArqException
	 */
	public void atualizaParametro(UsuarioGeral usuario, int sistema, String codigo, String valor) throws NegocioException, ArqException {
		ParametroDao dao = new ParametroDao();
		try {
			Parametro parametro = dao.findByPrimaryKey(codigo);
			parametro.setValor(valor);
			dao.salvar(parametro);

			cache.remove(codigo);
		} finally {
			dao.close();
		}
	}

	/**
	 * Apaga todo o cache de par�metros do sistema informado.
	 *
	 * @param sistema
	 */
	public void resetCache(Integer sistema) {
		cache = new HashMap<String, String>();
	}

}