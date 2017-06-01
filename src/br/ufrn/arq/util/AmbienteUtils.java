/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 11/08/2009
 */
package br.ufrn.arq.util;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;

import br.ufrn.ambientes.dominio.ConstantesParametrosAmbientes;
import br.ufrn.ambientes.dominio.TipoAmbiente;
import br.ufrn.arq.dominio.ConstantesParametroGeral;
import br.ufrn.arq.erros.ConfiguracaoAmbienteException;
import br.ufrn.arq.erros.RuntimeNegocioException;
import br.ufrn.arq.parametrizacao.ParametroHelper;

/**
 * M�todos utilit�rios para fun��es relacionadas
 * aos ambientes de execu��o dos sistemas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 */
public class AmbienteUtils {

	/**
	 * Deixa um sistema em modo de manuten��o.
	 * 
	 * @param sistema
	 */
	public static void deixarSistemaEmManutencao(int sistema) {
		try {
			FileUtils.touch(new File(getDirShared() + "manutencao/" + sistema));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tira um sistema do modo de manuten��o.
	 * 
	 * @param sistema
	 */
	public static void tirarSistemaDeManutencao(int sistema) {
		new File(getDirShared() + "manutencao/" + sistema).delete();
	}
	
	/**
	 * Verifica se um sistema est� em modo de manuten��o.
	 * 
	 * @param sistema
	 */
	public static boolean isSistemaEmManutencao(int sistema) {
		return new File(getDirShared() + "manutencao/" + sistema).exists();
	}
	
	private static String getDirShared() {
		String dirShared = ParametroHelper.getInstance().getParametro(ConstantesParametroGeral.CAMINHO_RECURSOS);
		if (!dirShared.endsWith("/")) {
			dirShared += "/";
		}
		return dirShared;
	}

	/**
	 * Verifica se o servidor atual � o de testes
	 *
	 * @return
	 */
	public static boolean isServidorTestes() {
	
		List<String> servidores = Arrays
				.asList(new String[] { "desenvolvimento", "testes" });
		return servidores.contains(AmbienteUtils.getLocalName());
	
	}

	/**
	 * Verifica se o servidor atual � localhost.
	 * @return
	 */
	public static boolean isServidorLocalhost() {
		return !isServidorTestes() && !isServidorProducao();
	}
	
	/**
	 * Verifica se o servidor atual � o de produ��o
	 *
	 * @return
	 */
	public static boolean isServidorProducao() {
		int tipoAmbiente = ParametroHelper.getInstance().getParametroInt(ConstantesParametrosAmbientes.TIPO_AMBIENTE_ATUAL);
		return tipoAmbiente == TipoAmbiente.PRODUCAO;
	}

	/**
	 * Retorna o nome da m�quina em que o m�todo � executado
	 * @deprecated Usar m�todos de {@link NetworkUtils}
	 * @return
	 */
	@Deprecated
	public static String getLocalName() {
		return NetworkUtils.getLocalName();
	}

	/**
	 * Retorna o endere�o da m�quina onde o m�todo foi executado
	 * @deprecated Usar m�todos de {@link NetworkUtils}
	 * @return
	 */
	@Deprecated
	public static String getLocalAddress() {
		return NetworkUtils.getLocalAddress();
	}

	/**
	 * Verifica se o protocolo de acesso a uma URL n�o est� seguro
	 *
	 * @param request
	 * @return
	 */
	public static boolean isNotSecure(HttpServletRequest request) {
		return !request.isSecure() && isServidorProducao()
				&& request.getServerName().contains("www");
	}

	/**
	 * Retorna uma String com o html da tela de manuten��o de um determinado
	 * sistema passado como par�metro.
	 * @param sistema
	 * @return
	 */
	public static String getTelaManutencao(Integer sistema) {
		File tela = new File(getDirShared() + "manutencao/" + sistema + ".htm");
		try {
			return FileUtils.readFileToString(tela);
		} catch (IOException e) {
			e.printStackTrace();
			return "Sistema em manuten��o.";
		}
	}
	
	/**
	 * Escreve uma String contendo o html da tela de manuten��o do
	 * sistema passado como par�metro em um arquivo.
	 * @param sistema
	 * @param conteudo
	 */
	public static void escreverTelaManutencao(Integer sistema, String conteudo) {
		if (getDirShared() == null) 
			throw new ConfiguracaoAmbienteException("N�o foi poss�vel encontrar o diret�rio para armazenar as informa��es solicitadas porque o par�metro CAMINHO_RECURSOS (c�digo 3_1_4) n�o est� definido.");
		
		if (!isEmpty(conteudo)) {
			File tela = new File(getDirShared() + "manutencao/" + sistema + ".htm");
			try {
				FileUtils.writeStringToFile(tela, conteudo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeNegocioException("Nenhum conte�do foi informado para a tela de manuten��o dos sistemas. Por favor, informe esse conte�do para continuar.");
		}
	}
	
	public static String obterVersaoProducao(HttpServletRequest request){
		return request.getParameter("sistema");
	}
	
	/**
	 * M�todo que retorna os dados da vers�o dos sistemas.
	 * 
	 * Recebe como par�metro o arquivo bundle associado, conforme o projeto.
	 * 
	 * @param arquivo
	 * @return
	 */
	public static HashMap<String, String> dadosVersao(String arquivo){
		HashMap<String, String> retorno = null;
		try {
			ResourceBundle p = ResourceBundle.getBundle(arquivo);
			retorno = new HashMap<String, String>();
			Enumeration<String> em = p.getKeys();
			
			while(em.hasMoreElements()){
				String chave = (String) em.nextElement();
				retorno.put(chave, p.getString(chave));
			}
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	/**
	 * Retorna a inst�ncia atual do JBoss em que o sistema est� rodando.
	 * @return
	 */
	public static String getInstanceName() {
		return System.getProperty("br.ufrn.jboss.instanceName");
	}

	/**
	 * Retorna o nome do servidor do JBoss com a inst�ncia em que o sistema
	 * est� sendo executado.
	 * @return
	 */
	public static String getNomeServidorComInstancia() {
		String nomeServidor = NetworkUtils.getLocalName();
		String nomeInstancia = getInstanceName();
		if (nomeInstancia != null)
			nomeServidor += "." + nomeInstancia;
		return nomeServidor;
	}
	
	/**
	 * M�todo que que verifica se um agent passado como par�metro � um mobile agent.
	 * @param agent
	 * @return
	 */
	public static boolean isMobileUserAgent(String agent) {
		String mobileAgents = ParametroHelper.getInstance().getParametro(ConstantesParametrosAmbientes.TIPOS_MOBILE_AGENTS);
		return agent.toLowerCase().matches(mobileAgents);
	}
	
}
