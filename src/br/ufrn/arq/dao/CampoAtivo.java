/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica
 * Diretoria de Sistemas
 *
 * Criado em 15/07/2009
 */
package br.ufrn.arq.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o a ser usada em um campo e que
 * indica que o campo � utilizado para sinalizar se a entidade
 * est� ativa ou n�o.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CampoAtivo {

	boolean value() default true;
	
}
