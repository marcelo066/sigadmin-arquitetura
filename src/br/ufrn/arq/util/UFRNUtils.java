/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/10/2007
 */
package br.ufrn.arq.util;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.util.ClassUtils;

import br.ufrn.arq.dao.BDUtils;
import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.dominio.RegistroEntrada;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.HostNaoAutorizadoException;
import br.ufrn.arq.erros.SegurancaException;
import br.ufrn.arq.negocio.validacao.MensagemAviso;
import br.ufrn.arq.negocio.validacao.TipoMensagemUFRN;
import br.ufrn.arq.negocio.validacao.Validatable;
import br.ufrn.comum.dao.MensagemAvisoDao;
import br.ufrn.comum.dominio.Papel;
import br.ufrn.comum.dominio.Permissao;
import br.ufrn.comum.dominio.UnidadeGeral;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 *
 * Classe geral com m�todos utilit�rios
 *
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class UFRNUtils {

	/** Constantes usadas para criptografia. */
	private static final String ARQUIVO_KEY = "arquivo_key";
	private static final String BR_UFRN_ARQ_ARQUIVOS_SECRET = "br.ufrn.arq.arquivos.secret";
	
	/** Constante de indicadores familiares utilizada na express�o regular. */
	private static final String INDICADORES_FAMILIARES = "(JUNIOR)|(J�NIOR)|(JR)|(FILHO)|(NETO)|(BISNETO)|(SOBRINHO)|(SEGUNDO)|(TERCEIRO)|(NETA)";
	
	/** Constante que define a partir de qual nome iniciar� a abrevia��o. */
	private static final int CONTROLE_ABREVIACAO = 2;
	
	/** Constante que define a partir de quantos caracteres o nome ser� abreviado. */
	private static final int NAO_ABREVIAR = 2;

	/** Cache para as mensagens de aviso dos sistemas. */
	public static final Map<String, MensagemAviso> CACHE_MENSAGENS = new HashMap<String, MensagemAviso>();
	
	/** Define o n�mero m�nimo de caracteres que passam pela valida��o de seguran�a da senha. */
	private static final int MIN_CARACTERES_SENHA = 6;
	
	/** Constantes de retorno de icones. */
	private static final String ICONE_WORD = "/shared/img/icones/word.png";
	private static final String ICONE_PDF = "/shared/img/icones/pdf.png";
	private static final String ICONE_DOWNLOAD = "/shared/img/icones/download.png";

	/**
	 * M�todo de truncamento de decimais
	 *
	 * @param d
	 * @param places
	 * @return
	 */
	public static final double truncateDouble(double d, int places) {
		return Math.round(d * Math.pow(10, places)) / Math.pow(10, places);
	}

	/**
	 * M�todo de truncamento de decimais
	 *
	 * @param d
	 * @param places
	 * @return
	 */
	public static final double truncateDouble(float d, int places) {
		return Math.round(d * Math.pow(10, places)) / Math.pow(10, places);
	}

	/**
	 * M�todo de truncamento de decimais sem arredondamento
	 *
	 * @param d
	 * @param places
	 * @return
	 */
	public static final double truncateSemArrendondar(double d, int places) {

		String number = String.valueOf(d);
		String inteira = number.substring(0, number.indexOf("."));
		String decimal = number.substring(number.indexOf(".") + 1, number
				.length());

		if (decimal.length() > places) {
			decimal = decimal.substring(0, places);
		} else if (decimal.length() < places) {
			decimal = decimal
					+ UFRNUtils.completaZeros(0, places - decimal.length());
		}

		String result = inteira + decimal;

		int resultInt = new Integer(result);

		return resultInt / Math.pow(10, places);

		/*
		 * Mudado pois a implementa��o atual dava problema ao multiplicar por 10
		 * devido a representa��o. Ex: 1128.36 * 10; IEE_754
		 * (http://en.wikipedia.org/wiki/IEEE_754)
		 */

	}

	/**
	 * Fun��o para c�lculo de digest da mensagem informada
	 *
	 * @param senha
	 * @return
	 * @throws ArqException
	 * @throws NoSuchAlgorithmException
	 */
	public static String toSHA1Digest(String senha) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
			String s = hash.toString(16);
			if (s.length() % 2 != 0)
				s = "0" + s;
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/*
	 * IPs v�lidos para acesso
	 * 200.17.143.31/24 DMZ, UFRNet 200.19.160.0/24 NAT?s 1:1 200.19.161.0/24
	 * NAT?s 1:1 200.19.162.0/24 NAT?s 1:1 200.19.163.0/24 UFRNet Cabo
	 * 200.19.164.0/24 UFRNet Cabo 200.19.165.0/24 200.19.166.0/24 NAT?s 1:1
	 * 200.19.167.0/24 NAT?s 1:1 200.19.168.0/24 Campus do Interior (Jundia�)
	 * 200.19.169.0/24 Campus do Interior (Caico) 200.19.170.0/24 Campus do
	 * Interior (Santa Cruz) 200.19.171.0/24 Campus do Interior (Currais Novos)
	 * 200.19.172.0/24 UFRNet Cabo 200.19.173.0/24 NAT?s 1:1 200.19.174.0/24
	 * NAT?s 1:1 200.19.175.0/24 NAT?s N:1
	 */

	/**
	 * Valida IPs v�lidos para acesso aos sistemas
	 *
	 * @param registro
	 * @throws HostNaoAutorizadoException
	 */
	@Deprecated
	public static void validaIP(RegistroEntrada registro) throws HostNaoAutorizadoException {
		return;
	}

	/**
	 * Transforma uma cole��o de PertistDB em um array com seus ids
	 *
	 * @param c
	 * @return
	 */
	public static int[] dominioToIdArray(Collection<? extends PersistDB> c) {
		int[] array = new int[c.size()];
		int i = 0;
		for (PersistDB p : c)
			array[i++] = p.getId();

		return array;

	}

	/**
	 * Retorna uma collection com ids a partir de uma collection de objetos de
	 * dom�nio.
	 *
	 * @param collection
	 * @return
	 */
	public static Collection<Integer> dominioToId(
			Collection<? extends PersistDB> collection) {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (PersistDB p : collection) {
			ids.add(p.getId());
		}

		return ids;
	}

	/**
	 * Transforma um array de identificadores, do tipo String, em uma cole��o de PersistDB.
	 * @param <V>
	 * @param classe
	 * @param ids
	 * @return
	 * @throws ArqException
	 */
	public static Collection<? extends PersistDB> idToDominio(Class<? extends PersistDB> classe, String... ids) throws ArqException {
		return idToDominio(classe, ArrayUtils.toIntArray(ids));
	}

	/**
	 * Transforma um array de identificadores, do tipo Integer, em uma cole��o de PersistDB.
	 * @param <V>
	 * @param classe
	 * @param ids
	 * @return
	 * @throws ArqException
	 */
	public static Collection<? extends PersistDB> idToDominio(Class<? extends PersistDB> classe, Integer... ids) throws ArqException {

		ArrayList<PersistDB> retorno = new ArrayList<PersistDB>();

		try{
			for(Integer id : ids){
				PersistDB p = classe.newInstance();
				p.setId(id);
				retorno.add(p);
			}
		}catch (IllegalArgumentException e) {
			throw new ArqException(e);
		}catch (InstantiationException e) {
			throw new ArqException(e);
		}catch (IllegalAccessException e) {
			throw new ArqException(e);
		}

		return retorno;
	}

	/**
	 * Retorna uma collection com ids em formato Integer a partir de uma
	 * collection de ids em formato de Strings
	 *
	 * @param collection
	 * @return
	 */
	public static Collection<Integer> toInteger(Collection<String> collection) {

		ArrayList<Integer> retorno = new ArrayList<Integer>();

		if (!isEmpty(collection)) {
			for (String id : collection)
				retorno.add(Integer.valueOf(id));
		}

		return retorno;
	}

	/**
	 * Retorna um n�mero mascarado com um certo n�mero de zeros � esquerda
	 *
	 * @param numero
	 * @param totalZeros
	 * @return
	 */
	public static String completaZeros(long numero, int totalZeros) {
		String zeros = "";
		for (int a = 0; a < totalZeros; a++) {
			zeros += "0";
		}
		DecimalFormat df = new DecimalFormat(zeros);
		return df.format(numero);
	}

	/**
	 * Transforma toda ocorr�ncia de uma aspas simples em aspas duplicadas.
	 * Utilizado para constru��o de queries SQL.
	 *
	 * @param original
	 * @return
	 */
	public static String trataAspasSimples(String original) {
		if (original != null) return original.replaceAll("'", "''");
		else return original;
	}

	/**
	 * Retorna uma String como alguns caracteres especiais (<,> e \) em sua
	 * forma de caracter especial em HTML.
	 *
	 * @param original
	 * @return
	 */
	public static String escapeHTML(String original) {
		return original.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\"", "\\\"");
	}

	/**
	 * Converte um CPF ou CPNJ formatado em um n�mero Long correspondente,
	 * retirando '.' e '-'.
	 *
	 * @param cpfCnpjString
	 * @return
	 */
	public static Long parseCpfCnpj(String cpfCnpjString) {
		try {
			cpfCnpjString = Formatador.getInstance().parseStringCPFCNPJ(
					cpfCnpjString.trim());

			return Long.parseLong(cpfCnpjString);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Verifica se uma exce��o foi gerada devido a um erro de viola��o de
	 * restri��o de chave estrangeira.
	 *
	 * @param e
	 * @return
	 */
	public static boolean isFKConstraintError(Exception e) {

		if (e.getCause() instanceof ConstraintViolationException) {
			ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
			DataAccessException translatedException = SessionFactoryUtils.convertHibernateAccessException(cve);

			if (translatedException instanceof DataIntegrityViolationException)
				return true;

			if (cve.getSQLException() != null) {
				String msg = cve.getSQLException().getMessage();
				if (msg.contains("violates foreign key constraint"))
					return true;
				else {
					if (cve.getSQLException().getNextException() != null) {
						msg = cve.getSQLException().getNextException().toString();
						if (msg.contains("violates foreign key constraint"))
							return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Verifica se uma determinada senha � v�lida. S�o verificadas as seguintes regras:
	 * proibi��es
	 *		- senha = login
	 *		- senha < MIN_CARACTERES_SENHA digitos
	 *		- com letras e n�meros (pelo menos 2 n�meros)
	 *		- data de anivers�rio
	 *		- identidade ou cpf
	 *		- baixa entropia
	 * @param login
	 * @param senha
	 * @return
	 */
	public static boolean validaSegurancaSenha(String login, String senha) {
		if (senha == null) return false;

		if (senha.equals(login)) {
			return false;
		} else if (senha.length() < MIN_CARACTERES_SENHA) {
			return false;
		} else if (!br.ufrn.arq.util.StringUtils.hasLetter(senha, 2) || !br.ufrn.arq.util.StringUtils.hasNumber(senha, 2)) {
			return false;
		}

		return true;
	}

	/**
	 * Gera uma String com 3 letras min�sculas, 2 mai�sculas e 2 n�meros
	 * para ser utilizado como senha.
	 * @return
	 */
	public static String geraSenhaAleatoria() {

		Random random = new Random();
		// 3 letras minusculas ( 95 - a e 122 - z, c�digos ASCII)
		Character c1 = (char) (random.nextInt(25) + 95);
		Character c2 = (char) (random.nextInt(25) + 95);
		Character c3 = (char) (random.nextInt(25) + 95);
		Character c4 = (char) (random.nextInt(25) + 65);
		Character c5 = (char) (random.nextInt(25) + 65);
		Integer num1 = random.nextInt(9);
		Integer num2 = random.nextInt(9);


		return c1.toString() + c2.toString() + c3.toString() +
		c4.toString() + c5.toString() + num1.toString() + num2.toString();

	}

	/**
	 * Converte um array de Strings em um array de Integer.
	 * @deprecated Usar ArrayUtils.toIntArray()
	 * @param params
	 * @return
	 */
	@Deprecated
	public static Integer[] toIntArray(String[] params) {
		return ArrayUtils.toIntArray(params);
	}

	/**
	 * Converte um array de ints em um array de Integers.
	 * @deprecated Usar ArrayUtils.toIntegerArray()
	 * @param array
	 * @return
	 */
	@Deprecated
	public static Integer[] toIntegerArray(int array[]) {
		return ArrayUtils.toIntegerArray(array);
	}

	/**
	 * Converte um array de Strings em um array de Integer.
	 * @deprecated Usar ArrayUtils.toLongArray()
	 * @param params
	 * @return
	 */
	@Deprecated
	public static Long[] toLongArray(String[] params) {
		return ArrayUtils.toLongArray(params);
	}

	/**
	 * Percorre todos os atributos do tipo PersistDB e os define como nulos caso
	 * sejam objetos transientes.
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static PersistDB clearTransientObjects(PersistDB obj) {
		List<Field> listaVisitados = new ArrayList<Field>();
		return clearTransientObjects(obj, obj.getClass(), listaVisitados);
	}

	/**
	 * Percorre todos os atributos do tipo PersistDB e os define como nulos caso
	 * sejam objetos transientes, levando em considera��o uma lista de objetos
	 * j� visitados.
	 *
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private static PersistDB clearTransientObjects(PersistDB obj, Class<?> classe, List<Field> listaVisitados) {

		if (classe != null) {
			Field[] atributos = classe.getDeclaredFields();
			for (int a = 0; a < atributos.length; a++) {
				Field atual = atributos[a];
				String nomeAtributo = atual.getName();

				if (PersistDB.class.isAssignableFrom(atual.getType())
						&& !ReflectionUtils.hasAnnotation(atual, Transient.class)
						&& ReflectionUtils.hasGetterSetter(obj, nomeAtributo)) {

					System.out.println(nomeAtributo);

					PersistDB db = (PersistDB) ReflectionUtils.getProperty(obj, nomeAtributo);

					if (db != null && db.getId() == 0 && !listaVisitados.contains(atual)) {
						listaVisitados.add(atual);
						ReflectionUtils.setProperty(obj, nomeAtributo, null);
					}

					clearTransientObjects(db, classe.getSuperclass(), listaVisitados);
				}
			}
		}

		return obj;

	}

	/**
	 * Retorna o �cone para o type mime correspondente.
	 *
	 * @param mimeType
	 * @return
	 */
	public static String getIcone(String mimeType) {

		if (mimeType.contains("msword")) {
			return ICONE_WORD;
		}
		if (mimeType.contains("pdf")) {
			return ICONE_PDF;
		}

		return ICONE_DOWNLOAD;

	}

	/**
	 * Transforma um ResultSet em um HashMap para uso em relat�rios
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 * @Deprecated Utilizar JdbcTemplate.queryForList
	 */
	@Deprecated
	public static HashMap<String, Object> resultSetToHashMap(ResultSet rs)
			throws SQLException {

		HashMap<String, Object> linha = new HashMap<String, Object>();
		for (int a = 1; a <= rs.getMetaData().getColumnCount(); a++) {
			linha.put(rs.getMetaData().getColumnName(a), rs.getObject(a));
		}
		return linha;

	}

	/**
	 * Transforma um ResultSet em um HashMap para uso em relat�rios
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static String resultSetToCSV(ResultSet rs) throws SQLException {
		return BDUtils.resultSetToCSV(rs);
	}
	
	/** Cria uma defini��o para a tabela a ser exportada.
	 * @param rs
	 * @param nomeTabela
	 * @return
	 * @throws SQLException
	 */
	public static String ddlFromResultSet(ResultSet rs, String nomeTabela) throws SQLException {
		return BDUtils.ddlFromResultSet(rs, nomeTabela);
	}
	
	/**
	 * Converte os dados do resultset passado para inserts em SQL na tabela passada.
	 *
	 * @param nomeTabela
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static String resultSetToSQLInserts(String nomeTabela, ResultSet rs) throws SQLException {
		 return BDUtils.resultSetToSQLInserts(nomeTabela, rs);
	 }

	/**
	 * Converte os dados do resultset passado para inserts em SQL na tabela passada.
	 *
	 * @param nomeTabela
	 * @param rs
	 * @param incluiDefinicaoTabela caso true, incluir� a DDL para criar a tabela
	 * @return
	 * @throws SQLException
	 */
	public static String resultSetToSQLInserts(String nomeTabela, ResultSet rs, boolean incluiDefinicaoTabela) throws SQLException {
		return BDUtils.resultSetToSQLInserts(nomeTabela, rs, incluiDefinicaoTabela);
	}

	/**
	 * Converte o acesso SQL a uma coluna em sua vers�o ASCII e em mai�sculas
	 *
	 * @param campo
	 * @return
	 */
	public static String toAsciiUpperUTF8(String campo) {

		return " to_ascii(upper(" + campo + "),'LATIN9')";

	}

	/**
	 * Converte o acesso SQL a uma coluna em sua vers�o ASCII
	 *
	 * @param campo
	 * @return
	 */
	public static String toAsciiUTF8(String campo) {

		return " to_ascii(" + campo + ", 'LATIN9')";

	}

	/**
	 * M�todo auxiliar pra consultas ignorando acentos e cedilhas
	 *
	 * @param campo
	 * @return
	 */
	public static String convertUtf8Latin9(String campo) {

		return " to_ascii(convert(" + campo + ", 'UTF8', 'LATIN9'), 'LATIN9')";

	}

	/**
	 * m�todo auxiliar pra consultas ignorando acentos e cedilhas
	 *
	 * @param campo
	 * @return
	 */
	public static String convertUtf8UpperLatin9(String campo) {

		return " to_ascii(upper(" + campo + "), 'LATIN9')";

	}

	/**
	 * Identifica se � um browser recomendado.
	 *
	 * @param userAgent
	 * @return
	 */
	public static boolean identificaBrowserRecomendado(String userAgent) {

		return true;
		/**
		 * // comentado pois j� passou um bom tempo para as pessoas atualizarem
		 * (Gleydson) if (userAgent.contains("Firefox") ||
		 * userAgent.contains("MSIE 7.0") || userAgent.contains("Safari") ||
		 * userAgent.contains("Iceweasel") ) { return true; } return false;
		 */

	}

	/**
	 * Transforma a String passada em MD5 utilizando um salt.
	 * @param senha
	 * @param salt
	 * @return
	 */
	public static String toMD5(String senha, String salts) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			
			if (!isEmpty(salts)) {
				String salttmp[] = salts.split(",");
			    byte salt[] = new byte[salttmp.length];

			    for (int i = 0; i < salt.length; i++) {
			      salt[i] = Byte.parseByte(salttmp[i]);
			    }
			    
			    md.update(salt);
			}

			BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
			String s = hash.toString(16);

			while (s.length() < 32)
				s = "0" + s;

			return s;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Transforma a String passada em MD5 sem usar salt.
	 *
	 * <p><strong>*** IMPORTANTE *** </strong> </p>

     * <p><i>Uma c�pia deste m�todo � usada para calcular a senha do m�dulo de circula��o da biblioteca, <br/>
	 * por isso qualquer altera��o nesse m�todo deve ser informada para que seja atualizado no m�dulo de <br/>
	 * circula��o tamb�m, antes que a atualiza��o v� para produ��o.</i>
	 * </p>
	 *
	 * @param senha
	 * @return
	 */
	public static String toMD5(String senha) {
		return toMD5(senha, null);
	}

	/**
	 * Gera uma sequ�ncia N vezes maior que a original, duplicando os caracteres
	 *
	 * @param ch
	 * @param numero
	 * @return
	 */
	public static String geraCaracteres(String ch, int numero) {
		StringBuffer buffer = new StringBuffer(numero);
		for (int a = 0; a < numero; a++) {
			buffer.append(ch);
		}
		return buffer.toString();

	}

	/**
	 * Gerar String para utiliza��o em clausulas IN do SQL a partir de um 
	 * array de inteiros.
	 *
	 * @param ids
	 * @return
	 */
	public static String gerarStringIn(int[] ids) {
		StringBuilder in = new StringBuilder();
		in.append(" ( ");

		int t = ids.length;
		for (int i = 0; i < t; i++) {
			in.append(ids[i]);
			if (i < (t - 1)) {
				in.append(",");
			}
		}
		in.append(" )");
		return in.toString();

	}

	/**
	 * Gera uma String para utiliza��o em cl�usulas IN do SQL a partir de um
	 * array de chars
	 *
	 * @param ids
	 * @return
	 */
	public static String gerarStringIn(char[] ids) {
		StringBuilder in = new StringBuilder();
		in.append(" ( ");

		int t = ids.length;
		for (int i = 0; i < t; i++) {
			in.append("'" + ids[i] + "'");
			if (i < (t - 1)) {
				in.append(",");
			}
		}
		in.append(" )");
		return in.toString();

	}

	/**
	 * Gera uma String para utiliza��o em cl�usulas IN do SQL a partir de uma
	 * cole��o de objetos PersistDB (utilizando seus IDs), Strings ou caracteres
	 * (acrescentando as aspas necess�rias) ou outros objetos (inteiros, longs,
	 * etc.)
	 *
	 * @param objs
	 * @return
	 */
	public static String gerarStringIn(Collection<?> objs) {
		ArrayList<String> vals = new ArrayList<String>(0);
		for (Object o : objs) {
			if (o instanceof PersistDB) {
				PersistDB p = (PersistDB) o;
				vals.add(p.getId() + "");
			} else if (o instanceof String || o instanceof Character || o instanceof Date) {
				vals.add("'" + o + "'");
			} else {
				vals.add(o + "");
			}
		}
		StringBuilder in = new StringBuilder();
		in.append(" ( ");
		for (int i = 0; i < vals.size(); i++) {
			in.append(vals.get(i));
			if (i < (vals.size() - 1)) {
				in.append(",");
			}
		}
		in.append(" )");
		return in.toString();
	}

	/**
	 * Gera uma String para utiliza��o em cl�usulas IN do SQL a partir de um
	 * array de Strings
	 *
	 * @param vals
	 * @return
	 */
	public static String gerarStringIn(String[] vals) {
		StringBuilder in = new StringBuilder();
		in.append(" ( ");
		int t = vals.length;
		for (int i = 0; i < t; i++) {
			in.append("'" + vals[i] + "'");
			if (i < (t - 1)) {
				in.append(",");
			}
		}
		in.append(" )");
		return in.toString();

	}

	/**
	 * Concatena uma String com um n�mero definido de espa�os em branco
	 *
	 * @param informacao
	 * @param totalEspacos
	 * @return
	 */
	public static String completaEspacos(String informacao, int totalEspacos) {
		int size = informacao.length();
		String branco = "";
		for (int a = 0; a < totalEspacos - size; a++) {
			branco += " ";
		}
		return branco + informacao;
	}

	/**
	 * Concatena uma String com um n�mero definido de espa�os em branco no final
	 *
	 * @param informacao
	 * @param totalEspacos
	 * @return
	 */
	public static String completaEspacosFinal(String informacao, int totalEspacos) {
		int size = informacao.length();
		String branco = "";
		for (int a = 0; a < totalEspacos - size; a++) {
			branco += " ";
		}
		return informacao + branco;
	}

	/**
	 * Gera uma String para utiliza��o em cl�usulas IN do SQL a partir de um
	 * array objetos
	 *
	 * @param objs
	 * @return
	 */
	public static String gerarStringIn(Object[] objs) {
		Collection<Object> col = new ArrayList<Object>();
		for (Object o : objs) {
			col.add(o);
		}
		return gerarStringIn(col);
	}

	/**
	 * Redimensiona uma imagem sem crop se a 
	 * resolu��o dela for superior a desejada
	 *
	 * @param imagem
	 * @param destWidth
	 * @param destHeight
	 * @return
	 * @throws IOException
	 */
	public static byte[] redimensionaProporcional(byte[] imagem, int width, int height)
			throws IOException {
		return redimensionaJPG(imagem, width, height, true);
	}
	
	/**
	 * Redimensiona uma imagem efetuando o crop caso sua 
	 * resolu��o seja superior a desejada
	 *
	 * @param imagem
	 * @param destWidth
	 * @param destHeight
	 * @return
	 * @throws IOException
	 */
	public static byte[] redimensionaJPG(byte[] imagem, int width, int height)
			throws IOException {
		return redimensionaJPG(imagem, width, height, false);
	}
	
	/** Redimensiona uma imagem JPG
	 *
	 * @param imagem
	 * @param destWidth
	 * @param destHeight
	 * @return
	 * @throws IOException
	 */
	public static byte[] redimensionaJPG(byte[] imagem, int width, int height, boolean proporcional)
			throws IOException {

		ByteArrayInputStream in = new ByteArrayInputStream(imagem);
		BufferedImage image = ImageIO.read(in);

		if(!proporcional)
			image = ImageUtils.resizeImage(image, width, height);
		else
			image = ImageUtils.resizeImageProportional(image, width, height);	

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);

		return out.toByteArray();
	}
	

	/**
	 * Realiza a clonagem profunda de um objeto atrav�s de serializa��o. �
	 * necess�rio que a classe a ser clonada implemente
	 * {@link java.io.Serializable} (lembrando que {@link PersistDB} estende
	 * {@link Serializable}).
	 *
	 * @param oldObj
	 *            Objeto a ser clonado
	 * @return Objeto clonado
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deepCopy(Object oldObj) {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); // A
			oos = new ObjectOutputStream(bos); // B
			// serialize and pass the object
			oos.writeObject(oldObj); // C
			oos.flush(); // D
			ByteArrayInputStream bin = new ByteArrayInputStream(bos
					.toByteArray()); // E
			ois = new ObjectInputStream(bin); // F
			// return the new object
			return (T) ois.readObject(); // G
		} catch(Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (oos != null)
				try { oos.close(); } catch(Exception e) { }
			if (ois != null)
				try { ois.close(); } catch(Exception e) { }
		}
	}

	/**
	 * Verifica a entropia da senha. A entropia � um conceito de seguran�a que
	 * mede o grau de aleatoriedade de uma informa��o. Normalmente � importante
	 * para testar boas sementes para entrada de algoritmos de criptografia.
	 * Neste caso � utilizado para analisar a quantidade de caracteres que se
	 * repetem em uma senha.
	 *
	 * @param senha
	 * @return
	 */
	public static boolean verificaEntropiaSenha(String senha, StringBuffer msg) {

		Hashtable<Character, Integer> ocorrencias = new Hashtable<Character, Integer>();
		senha = senha.toLowerCase();

		for (int i = 0; i < senha.length(); i++) {
			char c = senha.charAt(i);
			Integer qtdOcorrencias = ocorrencias.get(c);
			ocorrencias.put(c, qtdOcorrencias == null ? 1 : qtdOcorrencias + 1);
		}

		// verifica a quantidade de caracteres
		if (ocorrencias.keySet().size() <= 5) {
			msg.append("Sua senha possui muitos caracteres repetidos");
			return false;
		}

		// verifica repeti��es
		for (Character c : ocorrencias.keySet()) {
			if (ocorrencias.get(c) > 3) {
				msg
						.append("Sua senha possui mais de 3 repeti��es do caractere '"
								+ c + "'");
				return false;
			}
		}

		// calcula o desvio padr�o dos caracteres digitados
		int media = 0;
		for (Character c : ocorrencias.keySet()) {
			media = (media + c.charValue()) / 2;
		}
		int desvio = 0;
		for (Character c : ocorrencias.keySet()) {
			desvio = (c.charValue() - media) / 2;
		}
		if (desvio < 4) {
			msg
					.append("Sua senha possui muitos caracteres pr�ximos, sendo considerada fraca");
		}

		return true;

	}

	/**
	 *
	 * Verifica se a senha passada como par�metro � considerada fraca.
	 *
	 * @param senha
	 * @param msg
	 * 
	 * @return false se a senha for considerada fraca
	 */
	public static boolean verificaEntropiaSenhaMobile(String senha,
			StringBuffer msg) {

		Hashtable<Character, Integer> ocorrencias = new Hashtable<Character, Integer>();
		senha = senha.toLowerCase();

		for (int i = 0; i < senha.length(); i++) {
			char c = senha.charAt(i);
			Integer qtdOcorrencias = ocorrencias.get(c);
			ocorrencias.put(c, qtdOcorrencias == null ? 1 : qtdOcorrencias + 1);
		}

		// verifica repeti��es
		for (Character c : ocorrencias.keySet()) {
			if (ocorrencias.get(c) > 2) {
				msg
						.append("Sua senha possui mais de 3 repeti��es do caractere '"
								+ c + "'");
				return false;
			}
		}

		// calcula o desvio padr�o dos caracteres digitados
		int media = 0;
		for (Character c : ocorrencias.keySet()) {
			media = (media + c.charValue()) / 2;
		}
		int desvio = 0;
		for (Character c : ocorrencias.keySet()) {
			desvio = (c.charValue() - media) / 2;
		}

		if (desvio < 4) {
			msg
					.append("Sua senha possui muitos caracteres pr�ximos, sendo considerada fraca");
		}

		return true;
	}

	/**
	 * Converte um array de Strings em uma Collection de inteiros
	 *
	 * @param sArray
	 * @return
	 */
	public static Collection<Integer> toIntCollection(String[] sArray) {
		if (sArray == null)
			return null;
		Collection<Integer> col = new ArrayList<Integer>(0);
		for (String s : sArray) {
			try {
				col.add(Integer.parseInt(s));
			} catch (Exception e) {
				col.add(null);
			}
		}
		return col;
	}

	/**
	 * Formata uma String envolvendo-a em uma tag ACRONYM, adicionando a ela um
	 * t�tulo
	 *
	 * @param conteudo
	 * @param titulo
	 * @return
	 */
	public static String generateAcronymTag(String conteudo, String titulo) {
		StringBuilder acr = new StringBuilder();
		if (conteudo != null && titulo != null) {
			acr.append("<acronym title=\"" + titulo + "\">");
			acr.append(conteudo);
			acr.append("</acronym>");
		}
		return acr.toString();
	}

	/**
	 * Converte uma cole��o gen�rica em um ArrayList
	 *
	 * @param <T>
	 * @param col
	 * @return
	 * @deprecated Utilizar CollectionUtils.toList()
	 */
	@Deprecated
	public static <T> ArrayList<T> toList(Collection<T> col) {
		return br.ufrn.arq.util.CollectionUtils.toList(col);
	}

	/**
	 * Converte uma cole��o gen�rica em um Set
	 *
	 * @param <T>
	 * @param col
	 * @return
	 * @deprecated Utilizar CollectionUtils.toSet()
	 */
	@Deprecated
	public static <T> Set<T> toSet(Collection<T> col) {
		return br.ufrn.arq.util.CollectionUtils.toSet(col);
	}

	/**
	 * Verifica se um n�vel est� contido num array de niveis
	 * @deprecated Usar ArrayUtils.contains()
	 * @param niveis
	 * @param nivel
	 * @return
	 */
	@Deprecated
	public static boolean contains(char[] niveis, char nivel) {
		return ArrayUtils.contains(niveis, nivel);
	}

	/**
	 * Verifica se um id est� em uma lista de ids
	 * @deprecated Usar ArrayUtils.idContains()
	 * @author yoshi
	 * */
	@Deprecated
	public static boolean idContains(int id, int[] ids) {
		return ArrayUtils.idContains(id, ids);
	}

	/**
	 * Retorna o nome passado no seguinte formato: "Fernandes, Victor Hugo de Carvalho",
	 * e de acordo com o par�metro abreviarOutrosNomes abrevia, ou n�o, os nomes do meio.
	 *
	 * @param nome
	 * @param abreviarOutrosNomes
	 * @return
	 */
	public static String prepararNomeFormal(String nome, boolean abreviarOutrosNomes) {

		if (nome == null)
			return null;
		nome = nome.trim();

		if(nome.lastIndexOf(" ") == -1){  // Tem 1 �nico nome
			return nome;
		} 
		
		String sobrenome = nome.substring(nome.lastIndexOf(" "), nome.length()).trim();
		String  primeirosNomes = nome.substring(0, nome.lastIndexOf(" ")).trim();

		// Verificar se o nome restante contem alguma indicacao familiar
		Matcher indicadoresMatcher = Pattern.compile(INDICADORES_FAMILIARES).matcher(sobrenome);

		if (indicadoresMatcher.find() && primeirosNomes.lastIndexOf(" ") > 0) {
			String indicativo = sobrenome;
			sobrenome = primeirosNomes.substring(
					primeirosNomes.lastIndexOf(" "), primeirosNomes.length()).trim();
			primeirosNomes = primeirosNomes.substring(0, primeirosNomes.lastIndexOf(" ")).trim();
			sobrenome += " " + indicativo;
		}

		String[] nomes = Pattern.compile(" ").split(primeirosNomes);
		// Manter o primeiro e segundo nomes
		String nomesAbreviados = nomes[0]
				+ (nomes.length > 1 ? " " + nomes[1] : "");

		// Abreviar os outros nomes
		for (int i = CONTROLE_ABREVIACAO; i < nomes.length; i++) {
			if (nomes[i].length() > NAO_ABREVIAR && abreviarOutrosNomes) {
				nomesAbreviados += " " + nomes[i].charAt(0) + ".";
			} else {
				nomesAbreviados += " " + nomes[i];
			}
		}
		primeirosNomes = nomesAbreviados;

		return sobrenome + ", " + primeirosNomes;
	}

	/**
	 * Usado para passar um segundo par�metro no ver arquivo e ver foto. Ver
	 * servlet: VerArquivoServlet
	 *
	 * @param idArquivo
	 * @return
	 */
	public static String generateArquivoKey(int idArquivo) {
		String passkey = gerarPassKey();
		return toMD5(passkey + idArquivo + "");

	}

	/**
	 * Usado para gerar uma String md5 concatenando uma chave secreta com o nome
	 * passado como argumento
	 *
	 * @see UFRNUtils.gerarPassKey()
	 * @param idArquivo
	 * @return
	 */
	public static String generateKey(String nome) {
		String passkey = gerarPassKey();
		return toMD5(passkey + nome + "");
	}

	/**
	 * Pega uma chave secreta para ser usada na criptografia
	 *
	 * @return
	 */
	public static String gerarPassKey() {
		ResourceBundle bundle = ResourceBundle.getBundle(BR_UFRN_ARQ_ARQUIVOS_SECRET);
		String passFile = bundle.getString(ARQUIVO_KEY);
		String passKey;
		
		try {
			passKey = FileUtils.readFileToString(new File(passFile));
		} catch (IOException e) {
			passKey = "default-pass-key";
		}
		
		return passKey;
	}

	/**
	 * Gerar chave aleat�ria de texto, baseado em uma quantidade de caracteres.
	 * Usado para recupera��o de senha.
	 *
	 * @param quantidadeCaracteres
	 * @param letras
	 * 			Diz se a senha deve ser gerada com letras
	 * @param numeros
	 * 			Diz se a senha deve ser gerada com n�meros
	 */

	public static String generatePassKey(int quantidadeCaracteres,boolean letras, boolean numeros) {
		return RandomStringUtils.random(quantidadeCaracteres,letras,numeros);

	}

	/**
	 * M�todo para verificar se um usu�rio possui os pap�is passados
	 * como par�metro. Se n�o tiver, dispara uma exce��o do tipo SegurancaException.
	 *
	 * @param usuario
	 * @param papeis
	 * @throws SegurancaException
	 */
	public static void checkRole(UsuarioGeral usuario, int... papeis) throws SegurancaException {
		checkRole(usuario, null, papeis);
	}

	/**
	 * M�todo para verificar se um usu�rio possui os pap�is passados
	 * como par�metro. Se o usu�rio tiver o papel, verifica ainda se o papel � v�lido para
	 * a unidade. Se n�o for, dispara uma exce��o do tipo SegurancaException.
	 *
	 * @param usuario
	 * @param unidade
	 * @param papeis
	 * @throws SegurancaException
	 */
	public static void checkRole(UsuarioGeral usuario, UnidadeGeral unidade, int... papeis) throws SegurancaException {
		for (int a = 0; a < papeis.length; a++) {
			List<Permissao> permissoes = usuario.getPermissao(papeis[a]);
			boolean possuiPapel = !isEmpty(permissoes);

			if (usuario.possuiPapelTemporario(papeis[a]))
				return;
			
			for (Permissao permissao : permissoes) {
				if (possuiPapel && !isEmpty(unidade) && permissao != null)
					possuiPapel = permissao.permiteUnidade(unidade);
				
				if (possuiPapel) {
					return;
				}else{
					possuiPapel = !isEmpty(permissoes); // voltar possui papel para o valor inicial, antes de entrar no "for"
				}				
			}
			
		}

		throw new SegurancaException("Usu�rio n�o autorizado a realizar esta opera��o.", papeis);
	}

	/**
	 * Recupera as unidades pap�is das permiss�es que o usu�rio especificado como par�metro possui,
	 * que correspondem a algum dos pap�is especificados no par�metro.
	 * @param usuario
	 * @param papeis
	 * @return
	 */
	public static Collection<UnidadeGeral> getUnidadesPapel(UsuarioGeral usuario, int... papeis){

		ArrayList<Permissao> permissoes = new ArrayList<Permissao>();

		for (int a = 0; a < papeis.length; a++) {
			List<Permissao> permissao = usuario.getPermissao(papeis[a]);
			if(permissao != null)
				permissoes.addAll(permissao);
		}

		return getUnidadesPapel(permissoes);
	}

	/**
	 * Recupera as unidades pap�is de todas as permiss�es (quando existir) que o usu�rio passado como par�metro tem.
	 * @param usuario
	 * @return
	 */
	public static Collection<UnidadeGeral> getUnidadesPapel(UsuarioGeral usuario){
		return getUnidadesPapel(usuario.getPermissoes());
	}

	/**
	 * Recupera as unidades pap�is das permiss�es passadas como par�metro, caso exista.
	 * @param permissoes
	 * @return
	 */
	public static Collection<UnidadeGeral> getUnidadesPapel(Collection<Permissao> permissoes){

		ArrayList<UnidadeGeral> unidadesPapel = new ArrayList<UnidadeGeral>();

		if(!isEmpty(permissoes)){
			for (Permissao permissao : permissoes) {
				if(permissao.getPapel().isExigeUnidade()){
					UnidadeGeral unidadePapel = permissao.getUnidadePapel();
					if(unidadePapel != null && !unidadesPapel.contains(unidadePapel))
						unidadesPapel.add(unidadePapel);
				}
			}
		}

		return unidadesPapel;
	}

	/**
	 * M�todo para verificar se um usu�rio possui algum dos pap�is passados como par�metro.
	 * Se o usu�rio tiver o papel, verifica ainda se o papel � v�lido para a unidade do servidor.
	 * Se n�o for, dispara uma exce��o do tipo SegurancaException.
	 * Caso usu�rio tenha algum dos pap�is especificados e o mesmo n�o exija unidade, � permitido o acesso.
	 * @param usuario
	 * @param unidade
	 * @param papeis
	 * @throws SegurancaException
	 */
	@SuppressWarnings("unchecked")
	public static void checkHierarchyRole(UsuarioGeral usuario, UnidadeGeral unidade, int... papeis) throws SegurancaException {

		ArrayList<Papel> papeisUsuario = new ArrayList<Papel>(usuario.getPapeis());
		papeisUsuario.addAll(usuario.getPapeisTemporarios());

		Predicate predicate = new Predicate(){
			public boolean evaluate(Object obj) {
				Papel papel = (Papel) obj;
				return papel.isExigeUnidade();
			}
		};

		Collection<Papel> papeisUnidade = CollectionUtils.select(papeisUsuario, predicate);
		papeisUsuario.removeAll(papeisUnidade);

		for(Papel papel : papeisUsuario){
			if(ArrayUtils.idContains(papel.getId(), papeis))
				return;
		}

		for(Papel papel : papeisUnidade){
			if(ArrayUtils.idContains(papel.getId(), papeis)){
				List<Permissao> permissoes = usuario.getPermissao(papel.getId());
				for (Permissao permissao : permissoes) {
					if ((permissao != null && permissao.permiteUnidadeHierarquia(unidade))) {
						return;
					}
				}
			}
		}

		throw new SegurancaException("Usu�rio n�o autorizado a realizar esta opera��o.", papeis);
	}

	private static StackTraceElement stackTraceInvocador(int profundidade) {
		return Thread.currentThread().getStackTrace()[profundidade];
	}

	private static StackTraceElement stackTraceInvocador() {
		return stackTraceInvocador(4);
	}

	/**
	 * Identifica que classe chamou o m�todo onde o m�todo abaixo
	 * tiver sido chamado.
	 * @return
	 */
	public static Class<?> buscaClasseInvocadora() {
		try {
			return ClassUtils.forName(stackTraceInvocador().getClassName());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Identifica que m�todo chamou o m�todo onde o m�todo abaixo
	 * tiver sido chamado.
	 * @return
	 */
	public static String buscaMetodoInvocador() {
		StackTraceElement ste = stackTraceInvocador();
		return ste.getMethodName();
	}

	/**
	 * Identifica qual linha da classe que chamou o m�todo onde o m�todo abaixo
	 * tiver sido chamado.
	 * @return
	 */
	public static int buscaLinhaInvocadora() {
		StackTraceElement ste = stackTraceInvocador();
		 return ste.getLineNumber();
	}

	/**
	 * Busca uma mensagem no banco de dados de acordo com o c�digo
	 * passado como par�metro.
	 * @param codigo
	 */
	public static MensagemAviso getMensagem(String codigo, Object... params) {
		MensagemAviso msg = CACHE_MENSAGENS.get(codigo);
		if (msg == null) {
			msg = new MensagemAvisoDao().findByCodigo(codigo);
			if (msg == null) 
				return new MensagemAviso("Mensagem n�o encontrada. Por favor, contate a administra��o do sistema.", TipoMensagemUFRN.WARNING);
			 else 
				CACHE_MENSAGENS.put(codigo, msg);
		}
		return new MensagemAviso(String.format(msg.getMensagem(), params), msg.getTipo());
	}

	/**
	 * M�todo para retornar um ano ou um intervalo do anoInicio at� o ano atual, usado para informa��o no rodap� das p�ginas.
	 * 
	 * @param anoInicio
	 * @return
	 */
	public static String getCopyright(int anoInicio) {
		int ano = CalendarUtils.getAnoAtual();
		if (ano == anoInicio)
			return String.valueOf(anoInicio);
		else
			return anoInicio + "-" + ano;
	}

	/**
	 * Retorna a letra do alfabeto correspondente ao n�mero inteiro informado.
	 * O segundo argumento indica se o retorno desejado deve ser em caixa alta ou n�o.
	 * @param numero
	 * @param caps
	 * @return
	 */
	public static String inteiroToAlfabeto(int numero, boolean caps){
		return String.valueOf( (char) (numero + (caps?64:96)) );
	}

	/**
	 * Sobrecarga do m�todo anterior passando o argumento para que o retorno seja
	 * feito em caixa alta por padr�o.
	 * @param numero
	 * @return
	 */
	public static String inteiroToAlfabeto(int numero){
		return inteiroToAlfabeto(numero, true);
	}
	
	/**
	 * Seta para null os atributos especificados, desde que implementem a
	 * interface {@link PersistDB} e que o m�todo getId() retorne zero.
	 * Atributos n�o especificados n�o ser�o setados como null.<br>
	 * Exemplo de uso:
	 * <code>
	 *     obj = new br.ufrn.sigaa.ensino.graduacao.dominio.MatrizCurricular();
	 *     obj.setEnfase(new br.ufrn.sigaa.ensino.graduacao.dominio.Enfase());
	 *     obj.setHabilitacao(new br.ufrn.sigaa.ensino.graduacao.dominio.Habilitacao());
	 *     // o usu�rio seleciona uma �nfase
	 *     obj.getEnfase().setId(123);
	 *     // neste ponto, o atributo enfase tem um ID setado, enquanto que o atributo habilitacao n�o.
	 *     anularAtributos(obj, "enfase", "habilitacao");
	 *     obj.getEnfase() == null; // retorna falso
	 *     obj.getHabilitacao() == null; // retorna verdadeiro
	 * </code>
	 * @param obj objeto que ter� os atributos anulados.
	 * @param campos lista de atributos, que implementam a interface PersistDB, a serem anulados,caso possuam ID igual a zero.
	 */
	public static void anularAtributosVazios(PersistDB obj, String... atributos) {
		if (obj != null){
			for (String atributo : atributos) {
				String nomeMetodo = "get" + atributo.substring(0, 1).toUpperCase() + atributo.substring(1);
				Method metodo = ReflectionUtils.getMethod(obj.getClass(), nomeMetodo);
				if (metodo != null) {
					Class<?> tipoRetorno = metodo.getReturnType();
					Class<?>[] interfacesRetorno = tipoRetorno.getInterfaces();
					for (Class<?> interfaceRetorno : interfacesRetorno) {
						if (interfaceRetorno.getName().equals(PersistDB.class.getName()) || interfaceRetorno.getName().equals(Validatable.class.getName())) {
							PersistDB valor = (PersistDB) ReflectionUtils.getFieldValue(obj, atributo);
							// verifica que o atributo n�o est� nulo e possui ID igual a zero 
							if (valor != null && valor.getId() == 0) {
								ReflectionUtils.setFieldValue(obj, atributo, null);
								break;
							}
						}
					}
				}
			}
		}
	}
}