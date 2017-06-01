/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 09/04/2010
 */
package br.ufrn.comum.dominio;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.ufrn.arq.dominio.PersistDB;

/**
 * Classe com constantes para armazenar os tipos de usu�rio existentes nos
 * sistemas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 * 
 */
@Entity @Table(name="tipo_usuario", schema="comum")
public class TipoUsuario implements PersistDB {

	public static final int TIPO_SERVIDOR = 1;

	public static final int TIPO_ALUNO = 2;

	public static final int TIPO_CREDOR = 3;

	public static final int TIPO_OUTROS = 4;

	/**
	 * Usu�rio consultor de projetos de pesquisa que entram no sistema para
	 * avaliar projetos de pesquisa.
	 */
	public static final int TIPO_CONSULTOR = 5;

	public static final int DOCENTE_EXTERNO = 6;

	/**
	 * Usu�rio de empresas consignat�rias que consultam a margem consign�vel do
	 * servidor
	 */
	public static final int TIPO_CONSIGNATARIA = 7;

	/**
	 * Usu�rios de empresas de planos de sa�de que confirmam os planos aderidos
	 * pelos servidores.
	 */
	public static final int TIPO_PLANO_SAUDE = 8;

	/**
	 * Usu�rio das institui��es que possuem termo de coopera��o com a UFRN.
	 */
	public static final int TIPO_COOPERACAO = 9;

	/** Usu�rio preceptor de est�gio */
	public static final int TIPO_PRECEPTOR_ESTAGIO = 10;
	
	/** Usu�rio do Familiar do discente */
	public static final int TIPO_FAMILIAR = 11;	
	
	/** Usu�rio do Familiar do discente */
	public static final int TIPO_ELETROSUL= 12;	
	
	/** Usu�rio do Familiar do discente */
	public static final int TIPO_BOLSISTA = 13;	
	
	/** Usu�rio do Familiar do discente */
	public static final int TIPO_ESTAGIARIO = 14;	
	
	
	/** Identificador do tipo de usu�rio. */
	@Id
	private int id;
	
	/** Nome do tipo de usu�rio */
	private String nome;
	
	/** Se o usu�rio � interno, ou seja, faz parte da institui��o, ou � externo, ou seja, n�o faz parte da institui��o. */
	private boolean interno;
	
	public TipoUsuario() {
		
	}
	
	public TipoUsuario(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isInterno() {
		return interno;
	}

	public void setInterno(boolean interno) {
		this.interno = interno;
	}

	/**
	 * Retorna a descri��o dos tipos de usu�rio.
	 * @param tipo
	 * @return
	 */
	public static String getTipoUsuarioDesc(int tipo) {
		switch (tipo) {
		case TIPO_SERVIDOR:
			return "Servidor";
		case TIPO_ALUNO:
			return "Aluno";
		case TIPO_CONSULTOR:
			return "Consultor";
		case TIPO_CREDOR:
			return "Credor";
		case TIPO_OUTROS:
			return "Outros";
		case TIPO_COOPERACAO:
			return "Coopera��o";
		case TIPO_FAMILIAR:
			return "Familiar";			
		}
		

		return null;
	}
}
