/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 16/04/2010
 */
package br.ufrn.ambientes.dominio;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.ufrn.arq.dominio.PersistDB;


/**
 * Classe utilizada para registrar as informa��es sobre cada vez 
 * que um usu�rio entra em um ambiente (que n�o o de produ��o) que 
 * possua seguran�a ativa.
 *  
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Entity @Table(name="registro_entrada_ambiente", schema="comum")
public class RegistroEntradaAmbiente implements PersistDB {

	@Id
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
	           parameters={ @Parameter(name="sequence_name", value="comum.registro_entrada_ambiente_seq") })
	private int id;
	
	/** Usu�rio que acessou o ambiente */
	@ManyToOne @JoinColumn(name="id_usuario_ambiente")
	private UsuarioAmbiente usuario;
	
	/** Data de acesso */
	private Date data;
	
	/** IP do usu�rio */
	private String ip;

	/** IP da m�quina dentro de uma rede com NAT */
	@Column(name="ip_interno_nat")
	private String ipInternoNat;
	
	/** Servidor no qual o usu�rio se logou */
	private String server;
	
	/** Informa��es do Browser do Cliente */
	@Column(name="user_agent")
	private String userAgent;
	
	/** Resolu��o utilizada no monitor do usu�rio */
	private String resolucao;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UsuarioAmbiente getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioAmbiente usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpInternoNat() {
		return ipInternoNat;
	}

	public void setIpInternoNat(String ipInternoNat) {
		this.ipInternoNat = ipInternoNat;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getResolucao() {
		return resolucao;
	}

	public void setResolucao(String resolucao) {
		this.resolucao = resolucao;
	}
	
}
