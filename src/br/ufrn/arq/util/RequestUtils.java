/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 13/12/2006
 */
package br.ufrn.arq.util;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import javax.servlet.http.HttpServletRequest;

/**
 * Classe Utilit�ria para trabalhar com HttpServletRequest
 *
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class RequestUtils {

	/**
	 * Retorna o contexto da aplica��o WEB
	 * @param request
	 * @return
	 */
	public static String getContextPath(HttpServletRequest request) {
		return request.getContextPath();
	}

	/**
	 * Retorna a URL atual
	 * @param request
	 * @return
	 */
	public static String getCurrentURL(HttpServletRequest request) {
		return request.getRequestURL().toString();
	}

	/**
	 * Pega um par�metro em request
	 * @param param
	 * @param request
	 * @return
	 */
	public static String getParameter(String param, HttpServletRequest request) {
		return request.getParameter(param);
	}

	/**
	 * Retorna um array com o conjunto de valores para um par�metro em request
	 * @param param
	 * @param request
	 * @return
	 */
	public static String[] getParameterValues(String param, HttpServletRequest request) {
		return request.getParameterValues(param);
	}

	/**
	 * Retorna um array de inteiros com o conjunto de valores para um par�metro em request.
	 * @param param
	 * @param request
	 * @return
	 */
	public static Integer[] getParameterIntValues(String param, HttpServletRequest request) {
		String[] params = RequestUtils.getParameterValues(param, request);
		if (params != null) {
			return ArrayUtils.toIntArray(params);
		} else {
			return null;
		}
	}

	/**
	 * Pega um par�metro em request e converte para Integer. Caso o par�metro n�o exista, retorna null.
	 * @param param
	 * @param request
	 * @return
	 */
	public static Integer getParameterInt(String param, HttpServletRequest request) {
		return getParameterInt(param, null, request);
	}

	/**
	 * Pega um par�metro em request e converte para Integer.
	 * Caso o par�metro n�o exista, retorna o valor passado como valor padr�o.
	 * @param param Nome do par�metro
	 * @param defaultValue Valor padr�o, caso o par�metro seja null.
	 * @param request
	 * @return
	 */
	public static Integer getParameterInt(String param, Integer defaultValue, HttpServletRequest request) {
		String paramValue = getParameter(param, request);
		if (isEmpty(paramValue))
			return defaultValue;
		else
			paramValue = paramValue.trim();

		try {
			return new Integer(paramValue);
		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Pega um par�metro em request e converte para Long. Caso o par�metro n�o exista, retorna null.
	 * @param param
	 * @param request
	 * @return
	 */
	public static Long getParameterLong(String param, HttpServletRequest request) {
		return getParameterLong(param, null, request);
	}

	/**
	 * Pega um par�metro em request e converte para Long.
	 * Caso o par�metro n�o exista, retorna o valor passado como valor padr�o.
	 * @param param Nome do par�metro
	 * @param defaultValue Valor padr�o, caso o par�metro seja null.
	 * @param request
	 * @return
	 */
	public static Long getParameterLong(String param, Long defaultValue, HttpServletRequest request) {
		String paramValue = getParameter(param, request);
		if (isEmpty(paramValue))
			return defaultValue;

		try {
			return new Long(paramValue);
		} catch(NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Pega um par�metro em request e converte para char. Se o par�metro for null, retorna espa�o.
	 * @param param
	 * @param request
	 * @return
	 */
	public static char getParameterChar(String param, HttpServletRequest request){
		return getParameterChar(param, ' ', request);
	}

	/**
	 * Pega um par�metro em request e converte para char.
	 * Se o par�metro for null, retorna o char passado como par�metro.
	 * @param param
	 * @param defaultValue
	 * @param request
	 * @return
	 */
	public static char getParameterChar(String param, char defaultValue, HttpServletRequest request){
		String paramValue = getParameter(param, request);
		char retorno = defaultValue;
		if( !isEmpty( paramValue ) )
			retorno = paramValue.charAt(0);
		return retorno;
	}

	/**
	 * Pega um par�metro em request e converte para Boolean.
	 * Retorna true se o valor do par�metro for a String "true" (n�o case sensitive)
	 * e false para qualquer outro caso, inclusive null.
	 * @param param
	 * @param request
	 * @return
	 */
	public static Boolean getParameterBoolean(String param, HttpServletRequest request) {
		return Boolean.valueOf(getParameter(param, request));
	}

	/**
	 * Pega um par�metro em request e retorna o int equivalente.
	 * Se a String n�o puder ser convertida para int, � retornado o defaultValue.
	 * @param req
	 * @param param
	 * @param defaultValue
	 * @return
	 */
	public static final int getIntParameter(HttpServletRequest req, String param, int defaultValue) {
		String value = req.getParameter(param);
		int result = defaultValue;

		try {
			result = Integer.parseInt(value);
		} catch(Exception e) {	}
		return result;
	}

	/**
	 * Pega um par�metro em request e retorna o int equivalente.
	 * Se a String n�o puder ser convertida para int, � retornado 0.
	 * @param req
	 * @param param
	 * @return
	 */
	public static final int getIntParameter(HttpServletRequest req, String param) {
		return RequestUtils.getIntParameter(req, param, 0);
	}

}
