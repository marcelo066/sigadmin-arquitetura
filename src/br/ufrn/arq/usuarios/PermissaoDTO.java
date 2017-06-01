/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 14/04/2010
 */
package br.ufrn.arq.usuarios;

import java.io.Serializable;

/**
 * Data Transfer Object para armazenar as
 * informa��es das permiss�es de um usu�rio. Utilizado
 * na integra��o entre sistemas atrav�s de chamada
 * remota de m�todos.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class PermissaoDTO implements Serializable {
	
	private static final long serialVersionUID = -1L;

	private int id;
	
	private int idPapel;
	
	private String nomePapel;
	
	private int idUsuario;
	
	private String loginUsuario;
	
	private Integer unidadePermissao;
	
	private Integer designacao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdPapel() {
		return idPapel;
	}

	public void setIdPapel(int idPapel) {
		this.idPapel = idPapel;
	}

	public String getNomePapel() {
		return nomePapel;
	}

	public void setNomePapel(String nomePapel) {
		this.nomePapel = nomePapel;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public Integer getUnidadePermissao() {
		return unidadePermissao;
	}

	public void setUnidadePermissao(Integer unidadePermissao) {
		this.unidadePermissao = unidadePermissao;
	}

	public Integer getDesignacao() {
		return designacao;
	}

	public void setDesignacao(Integer designacao) {
		this.designacao = designacao;
	}
	
}
