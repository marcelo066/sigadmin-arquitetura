/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 14/06/2010
 */
package br.ufrn.arq.dao;

/**
 * Interface implementada por recursos ou adapters para recursos
 * que podem ser fechados, como conex�es ou sess�es do Hibernate.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public interface ClosableResource {

	/** Fecha o recurso */
	public void close();
	
}
