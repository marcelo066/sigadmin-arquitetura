/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 18/03/2008
 */
package br.ufrn.arq.seguranca;

/**
 * Constantes com todos os pap�is do sistema comum
 * 
 * @author Ricardo Wendell
 * @author Gleydson Lima
 *
 */
public class ComumPapeis {

	private static int COMUM_SUBSISTEMA = 1000;
	
	/** Permiss�o de envio de notifica��es aos usu�rios */
	public static final int GESTOR_COMUNICACAO = COMUM_SUBSISTEMA + 1;
	
	/** Permiss�o de acesso � �rea de administra��o de redes */
	public static final int ADMINISTRADOR_REDE = COMUM_SUBSISTEMA + 2;
	
	/** Permiss�o para atribuir permiss�es a outros usu�rios */
	public static final int GESTOR_PERMISSOES = COMUM_SUBSISTEMA + 3;
	
}
