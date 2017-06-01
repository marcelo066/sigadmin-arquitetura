/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 19/07/2010
 */
package br.ufrn.arq.seguranca.dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe que representa o bloqueio de um usu�rio devido � inatividade
 * nos sistemas, ou seja, por ele n�o ter entrado nos sistemas por um
 * per�odo de tr�s meses, por exemplo. 
 * 
 * Ap�s bloqueado, um usu�rio dever� passar por um desbloqueio para
 * poder entrar novamente, no qual ele ir� confirmar os seus dados
 * e o bloqueio ser� inativado.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Entity @Table(name="bloqueio_usuario_inativo", schema="comum")
public class BloqueioUsuarioInativo implements PersistDB {

	@Id
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
	           parameters={ @Parameter(name="sequence_name", value="comum.bloqueio_usuario_inativo_seq") })
	private int id;
	

	/**
	 * Usu�rio que est� bloqueado devido � inatividade
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_usuario")
	private UsuarioGeral usuario;
	
	/**
	 * Data em que o bloqueio foi efetuado.
	 */
	@Column(name = "data_bloqueio")
	private Date dataBloqueio;
	
	/**
	 * Data em que o usu�rio desbloqueou a sua conta
	 */
	@Column(name = "data_desbloqueio")
	private Date dataDesbloqueio;
	
	/**
	 * Se o bloqueio est� ativo ou n�o. Quando o bloqueio n�o est� ativo
	 * significa que o usu�rio realizou o desbloqueio de sua conta.
	 */
	private boolean ativo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UsuarioGeral getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioGeral usuario) {
		this.usuario = usuario;
	}

	public Date getDataBloqueio() {
		return dataBloqueio;
	}

	public void setDataBloqueio(Date dataBloqueio) {
		this.dataBloqueio = dataBloqueio;
	}

	public Date getDataDesbloqueio() {
		return dataDesbloqueio;
	}

	public void setDataDesbloqueio(Date dataDesbloqueio) {
		this.dataDesbloqueio = dataDesbloqueio;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}
