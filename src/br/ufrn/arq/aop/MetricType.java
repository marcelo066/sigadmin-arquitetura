/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica
 * Diretoria de Sistemas
 *
 * Criado em 29/07/2009
 */
package br.ufrn.arq.aop;

/**
 * Define os tipos de m�tricas que ser�o utilizadas
 * no profiling de m�todos de classes.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public enum MetricType {

	TIME, // M�dia de tempo de execu��o
	
	COUNT_CALL, // N�mero total de chamadas ao m�todo
	
	MAX_TIME // Tempo m�ximo de execu��o do m�todo
	
}
