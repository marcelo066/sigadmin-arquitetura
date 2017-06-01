/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 10/02/2009
 */
package br.ufrn.arq.web.conversation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o que indica que um m�todo pode ser utilizado
 * mesmo que o escopo conversa��o n�o esteja ativo.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface NoConversation {

}
