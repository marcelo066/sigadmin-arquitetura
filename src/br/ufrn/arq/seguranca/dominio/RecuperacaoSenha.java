/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 14/11/2007
 */
package br.ufrn.arq.seguranca.dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.ufrn.arq.dominio.PersistDB;

/**
 * Entidade persistida ao recuperar a senha do usu�rio.
 *
 * @author Gleydson Lima
 *
 */
@Entity
@Table(name = "recuperacao_senha", schema="infra")
public class RecuperacaoSenha implements PersistDB {

	@Id
	@Column(name = "id_recuperacao_senha", nullable = false)
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
           parameters={ @Parameter(name="sequence_name", value="hibernate_sequence") })
	private int id;

	/** Login do usu�rio */
	private String login;

	/** E-Mail do usu�rio */
	private String email;

	/** Data em que foi efetuado o pedido de recupera��o */
	private Date data;

	/** IP do usu�rio */
	private String ip;

	/** Senha hash para seguran�a. Utilizada para garantir que o usu�rio que solicitou a recupera��o � o que vai realiz�-la. */
	private String hash;
	
	/** Se a recupera��o da senha j� foi realizada ou n�o. */
	private boolean efetuada;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isEfetuada() {
		return efetuada;
	}

	public void setEfetuada(boolean efetuada) {
		this.efetuada = efetuada;
	}

	public boolean isExpirada() {
		Date agora = new Date();
		long doisDias = 2L * 24 * 60 * 60 * 1000;
		
		if (agora.getTime() - data.getTime() > doisDias)
			return true;
		return false;
	}
	
	public boolean isValida() {
		return !isEfetuada() && !isExpirada();
	}

}
