/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/10/2007
 */
package br.ufrn.arq.erros;

/**
 *
 * Exce��o utilizada para informar viola��o de acesso
 * para hosts n�o autorizados.
 *
 * @author Gleydson Lima
 *
 */

public class HostNaoAutorizadoException extends ArqException {

	public HostNaoAutorizadoException(String msg) {
		super(msg);
		setNotificavel(false);
	}

}
