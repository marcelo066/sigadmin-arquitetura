/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 14/07/2009
 */
package br.ufrn.arq.seguranca.log;

import java.util.Date;

/**
 * Classe utilizada para Log de updates realizados
 * com JDBC.
 *
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class LogJdbcUpdate implements LogUFRN {

	private int id;

	/** SQL utilizado para o update no banco de dados */
	private String sql;

	/** Par�metros enviados � consulta */
	private Object[] params;
	
	/** Par�metros concatenados em uma String */
	private String paramsStr;
	
	/** Registro de entrada do usu�rio que efetua a opera��o */
	private Integer idRegistroEntrada;
	
	/** Registro de entrada do usu�rio que efetua a opera��o */
	private Integer idUsuario;
	
	/** Data da realiza��o da opera��o. */
	private Date data;

	/** C�digo do processador onde a opera��o � processada. */
	private Integer codMovimento;

	/** Sistema em que se realiza a opera��o. 1 - SIPAC, 2 - PROTOCOLO, 3 - SCO, 4 - SIGAA */
	private int sistema;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public Integer getIdRegistroEntrada() {
		return idRegistroEntrada;
	}

	public void setIdRegistroEntrada(Integer idRegistroEntrada) {
		this.idRegistroEntrada = idRegistroEntrada;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodMovimento() {
		return codMovimento;
	}

	public void setCodMovimento(Integer codMovimento) {
		this.codMovimento = codMovimento;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public String getParamsStr() {
		return paramsStr;
	}

	public void setParamsStr(String paramsStr) {
		this.paramsStr = paramsStr;
	}

}