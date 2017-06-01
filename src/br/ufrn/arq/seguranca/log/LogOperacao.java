/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.seguranca.log;

import java.util.Date;
import java.util.StringTokenizer;

import br.ufrn.arq.util.UFRNUtils;

/**
 * Classe de dom�nio do log de opera��es do usu�rio
 * 
 * @author Gleydson Lima
 * 
 */
public class LogOperacao implements LogUFRN {

	private int idRequest;

	// Data e Hora da opera��o
	private Date hora;

	// URL acionada
	private String action;

	// Parametros enviados
	private String parameters;

	// Registro de entrada do usu�rio
	private Integer idRegistroEntrada;

	// Registro de entrada do usu�rio na parte p�blica
	private Integer idRegistroAcessoPublico;

	// Tempo que durou a opera��o
	private long tempo;

	// Se uma exce��o foi disparada
	private Boolean erro;

	// Caso a exce��o foi disparada, o tracing da mesma
	private String tracing;

	// Id do log opera��o
	private int id;

	// sistema que originou a opera��o
	private int sistema;
	
	// mensagens exibidas para o usu�rio durante a requisi��o
	private String mensagens;

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTracing() {
		return tracing;
	}

	public void setTracing(String tracing) {
		this.tracing = tracing;
	}

	public Boolean getErro() {
		return erro;
	}

	public void setErro(Boolean erro) {
		this.erro = erro;
	}

	public long getTempo() {
		return tempo;
	}

	public double getTempoSegundos() {
		return UFRNUtils.truncateDouble((double) tempo / 1000, 2);
	}

	public void setTempo(long tempo) {
		this.tempo = tempo;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Integer getIdRegistroEntrada() {
		return idRegistroEntrada;
	}

	public void setIdRegistroEntrada(Integer idRegistroEntrada) {
		this.idRegistroEntrada = idRegistroEntrada;
	}
	
	public Integer getIdRegistroAcessoPublico() {
		return idRegistroAcessoPublico;
	}

	public void setIdRegistroAcessoPublico(Integer idRegistroAcessoPublico) {
		this.idRegistroAcessoPublico = idRegistroAcessoPublico;
	}

	public int getIdRequest() {
		return idRequest;
	}

	public void setIdRequest(int idRequest) {
		this.idRequest = idRequest;
	}

	public String getParameters() {

		StringTokenizer st = new StringTokenizer(parameters, "\n");
		StringBuffer newEspec = new StringBuffer(parameters.length() + 10);

		while (st.hasMoreTokens()) {
			String linha = st.nextToken();
			if (linha.length() > 50) {
				linha = linha.substring(0, 50) + "<br>"
						+ linha.substring(50, linha.length());
			}
			newEspec.append(linha + "<br>");
		}

		return newEspec.toString();
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getMensagens() {
		return mensagens;
	}

	public void setMensagens(String mensagens) {
		this.mensagens = mensagens;
	}

}
