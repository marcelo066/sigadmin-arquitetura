/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica
 * Diretoria de Sistemas
 *
 * Criado em 29/07/2009
 */
package br.ufrn.arq.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o que indica que um m�todo espec�fico
 * ou todos os m�todos da classe ser�o medidos
 * de acordo com os crit�rios definidos em {@link MetricType}. 
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Target({ElementType.TYPE, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Metrics {

	MetricType[] value();
	
}
