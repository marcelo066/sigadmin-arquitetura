/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 08/04/2009
 */
package br.ufrn.arq.seguranca.dominio;

import java.util.Date;

import br.ufrn.arq.dominio.RegistroEntrada;
import br.ufrn.comum.dominio.Sistema;

/**
 * Representa um token para autentica��o que 
 * permite que um usu�rio acesse um recurso existente
 * em outro sistema sem a necessidade de efetuar logon.
 *  
 * @author David Pereira
 *
 */
public class TokenAutenticacao {

	/** Identificador do token */
	private int id;
	
	/** Chave do token. */
	private String key;
	
	/** Data de cria��o do token. */
	private Date data;
	
	/** Registro de entrada do usu�rio que criou o token. */
	private RegistroEntrada entrada;
	
	/** Sistema de origem do usu�rio que criou o token. */
	private Sistema origem;
	
	/** Se o token j� foi usado ou n�o. Se j� tiver sido usado, � inv�lido. */
	private boolean valido;

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public RegistroEntrada getEntrada() {
		return entrada;
	}

	public void setEntrada(RegistroEntrada entrada) {
		this.entrada = entrada;
	}

	public Sistema getOrigem() {
		return origem;
	}

	public void setOrigem(Sistema origem) {
		this.origem = origem;
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}
	
}
