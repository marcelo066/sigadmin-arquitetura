/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 18/02/2009
 */
package br.ufrn.arq.web;

/**
 * Interface implementada por objetos cujo estado
 * ser� visualizado atrav�s de uma barra de progresso.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public interface Progressable {

	public int getCurrent();
	
	public int getCurrentPercent();
	
	public int getTotal();

	public boolean isFinished();

	public void increment();
	
	public void reset();
	
}
