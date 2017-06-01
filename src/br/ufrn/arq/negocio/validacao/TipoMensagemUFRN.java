/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.negocio.validacao;

/**
 * Enumera��o com os tipos de mensagens de aviso. Usado para exibir mensagens
 * personalizadas por tipo
 *
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public enum TipoMensagemUFRN {

	ERROR,

	WARNING,

	INFORMATION;

	/**
	 * Retorna um tipo de mensagem de acordo com um n�mero que
	 * representa a sua ordem de declara��o.
	 * @param ordinal
	 * @return
	 */
	public static TipoMensagemUFRN valueOf(int ordinal) {
		return values()[ordinal];
	}

}
