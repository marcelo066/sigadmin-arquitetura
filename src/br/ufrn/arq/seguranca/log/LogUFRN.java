/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.seguranca.log;

/**
 * Interface implementada por todas as classes
 * de log da arquitetura.
 * 
 * @author Gleydson Lima
 *
 */
public interface LogUFRN {

	/**
	 * Retorna qual o sistema do Log. Usado para escolher a conex�o com o
	 * banco de dados
	 *
	 * @return
	 */
	public int getSistema();

}
