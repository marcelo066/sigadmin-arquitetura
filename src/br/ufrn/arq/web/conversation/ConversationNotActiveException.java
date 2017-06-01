/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 12/02/2009
 */
package br.ufrn.arq.web.conversation;

/**
 * Exce��o disparada quando o usu�rio tenta acessar um m�todo
 * de um managed bean com escopo conversation e a conversa��o
 * ainda n�o foi iniciada ou foi finalizada.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class ConversationNotActiveException extends RuntimeException {

	public ConversationNotActiveException(String msg) {
		super(msg);
	}
	
}
