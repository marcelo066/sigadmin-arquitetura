/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/08/2010
 */
package br.ufrn.arq.seguranca;


/**
 * Pap�is do Subsistema do PortalUFRN
 * 
 * @author M�rio Rizzi
 * @author Gleydson Lima
 *
 */
public class PortalPapeis {

	/** Atribuir permiss�es aos usu�rios administradores dos sites relacinados ao gerenciador de conte�do. */
	public static final int ADMINISTRADOR_SITE = PortalSubsistemas.ADMINISTRACAO.getId() + 1;

	/** Atribuir permiss�es aos usu�rios dos sites relacinados ao gerenciador de conte�do. */
	public static final int GESTOR_SITES =  PortalSubsistemas.ADMINISTRACAO.getId() + 2;
		
	/** Atribuir permiss�es aos usu�rios administradores dos sites relacinados ao gerenciador de conte�do. */
	public static final int GESTOR_AGENCIA_COMUNICACAO = PortalSubsistemas.ADMINISTRACAO.getId() + 3;
	
}
