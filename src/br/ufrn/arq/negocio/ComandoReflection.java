/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 28/05/2008
 */
package br.ufrn.arq.negocio;

import br.ufrn.arq.dominio.Comando;

/**
 * Interface para utilizar as classes de comandos na arquitetura.
 * COmo elas est�o nos pacotes dos sistemas � necess�rio carreg�-la por refletion.
 * Esta interface serve para ter uma abstra��o �nica delas na opera��o de recuperar
 * o comando pelo c�digo.
 * 
 * @author Gleydson Lima
 *
 */
public interface ComandoReflection {

	public Comando getComando(int codigo);
	
}
