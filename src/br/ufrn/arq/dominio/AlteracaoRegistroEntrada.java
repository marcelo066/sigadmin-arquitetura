/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/07/2006
 */
package br.ufrn.arq.dominio;

import java.util.Date;

import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe utilizada para logar opera��es do administrador logado como um outro usu�rio.
 * 
 * @author Rafael Borja
 * @author Gleydson Lima
 * 
 */
public class AlteracaoRegistroEntrada implements PersistDB {
	
	/** Identificador */
	private int id;

	/** Registro de entrada do usu�rio que est� logando como */
	private RegistroEntrada registroEntrada;

	/** Usuario que o administrador utilizou para se logar */
	private UsuarioGeral usuario;

	/** Hora em que a troca de usu�rio foi feita */
	private Date data;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RegistroEntrada getRegistroEntrada() {
		return registroEntrada;
	}

	public void setRegistroEntrada(RegistroEntrada registroEntrada) {
		this.registroEntrada = registroEntrada;
	}

	public UsuarioGeral getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioGeral usuario) {
		this.usuario = usuario;
	}
}
