/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 20/10/2009
 */
package br.ufrn.arq.negocio.validacao;

/**
 * Exce��o associada ao mau uso da API para valida��o
 * da arquitetura.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class ValidationException extends RuntimeException {

	public ValidationException(String msg) {
		super(msg);
	}

}
