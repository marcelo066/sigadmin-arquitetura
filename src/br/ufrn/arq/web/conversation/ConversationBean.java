/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 13/02/2009
 */
package br.ufrn.arq.web.conversation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * Anota��o para marcar managed beans que possuem
 * o escopo Conversation.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ConversationBean {

	String value() default "";
	
}
