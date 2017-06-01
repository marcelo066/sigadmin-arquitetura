/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 23/04/2004
 */
package br.ufrn.arq.web.exceptions;

/**
 * Enum com os poss�veis resultados de um exception handler.
 * 
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public enum ExceptionHandlerResult {

	/** Continuar o processamento, o n� da cadeia n�o p�de fazer nada com a exce��o. */
	CONTINUE,
	
	/** Parar o processamento, algo j� foi feito com a exce��o. */
	STOP,

	/** Parar o processamento, ignorar a exce��o. */
	IGNORE;
	
	public boolean isContinue() {
		return this == CONTINUE;
	}

	public boolean isIgnore() {
		return this == IGNORE;
	}

	public boolean isStop() {
		return this == STOP;
	}
	
}
