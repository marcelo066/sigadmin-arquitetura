/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 22/10/2007
 */
package br.ufrn.arq.seguranca.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anota��o utilizada em campos de classes
 * de dom�nio que forem do tipo UsuarioGeral. Serve
 * para guardar qual foi o usu�rio que cadastrou a entidade.
 * Quando o campo tem essa anota��o, seu valor � setado
 * automaticamente pelo DAO na hora do cadastro.
 *  
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CriadoPor {

}
