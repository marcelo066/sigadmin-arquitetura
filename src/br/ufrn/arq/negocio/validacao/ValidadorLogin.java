/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.negocio.validacao;

import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.util.StringUtils;

/**
 * Classe para validar o formato do Login do usu�rio
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class ValidadorLogin {

	public static String validar(String login) throws NegocioException {
		
		if (login.indexOf(" ") != -1) 
			throw new NegocioException("N�o � poss�vel cadastrar login com espa�os em brancos");
		
		String chars = "`~!@#$%^&*()=+[{]}|\\\'\":;/?,<>�������������Ǿ������Ķ��Ϸ�������������";
		
		for (int i = 0; i < chars.length(); i++) {
			if (login.indexOf(chars.charAt(i)) != -1) {
				throw new NegocioException("Caractere inv��lido encontrado no login: " + chars.charAt(i));
			}
		}
		
		if (!login.equals(StringUtils.toAscii(login))) {
			throw new NegocioException("Login n�o pode ter acentos ou caracteres especiais");
		}
		
		try {
			Integer.parseInt(login);
			throw new NegocioException("N�o � permitido cadastras um login apenas com n�meros.");
		} catch(NumberFormatException e) { 	}
		
		return login.toLowerCase();
		
	}
	
}
