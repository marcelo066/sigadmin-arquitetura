/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/07/2005
 */
package br.ufrn.arq.dominio;

import java.util.Date;

import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe utilizada para logar a entrada do usu�rio no sistema
 *
 * @author Gleydson Lima
 *
 */

public class RegistroEntrada implements PersistDB {

	/** Identificador */
	private int id;

	/** Usu�rio associado � entrada no sistema */
	private UsuarioGeral usuario;

	/** Data do registro de entrada */
	private Date data;

	/** IP da m�quina do registro de entrada */
	private String IP;
	
	/** IP da m�quina dentro de uma rede com NAT */
	private String ipInternoNat;

	/** Indica a qual subsistema o registro de entrada pertence */
	private Integer sistema;

	/** Servidor do Cluster que o usu�rio logou */
	private String server;

	/** Informa��es do Browser do Cliente */
	private String userAgent;

	/** Data de LogOff */
	private Date dataSaida;

	/** n�o persistido, buscado do log de opera��es */
	private Date dataUltimaOperacao;

	/** Resolu��o do monitor que o cliente possui */
	private String resolucao;

	/** C�digo do passaporte quando acesso parte do SIGRH ou SIGAA. */
	private Integer passaporte;

	/** Canal de acesso: (W) Web, (M) Mobile, (D) Desktop */
	private String canal;

	public static final String CANAL_WEB =  "W";
	
	public static final String CANAL_WAP =  "WA";

	public static final String CANAL_WEB_MOBILE = "M";

	public static final String CANAL_DESKTOP = "D";
	
	public static final String CANAL_DEVICE = "S";
	
	public Integer getPassaporte() {
		return passaporte;
	}

	public void setPassaporte(Integer passaporte) {
		this.passaporte = passaporte;
	}

	public Date getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String ip) {
		IP = ip;
	}
	
	public String getIpInternoNat() {
		return ipInternoNat;
	}

	public void setIpInternoNat(String ipInternoNat) {
		this.ipInternoNat = ipInternoNat;
	}

	public UsuarioGeral getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioGeral usuario) {
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getSistema() {
		return sistema;
	}

	public void setSistema(Integer sistema) {
		this.sistema = sistema;
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

	public Date getDataUltimaOperacao() {
		return dataUltimaOperacao;
	}

	public void setDataUltimaOperacao(Date dataUltimaOperacao) {
		this.dataUltimaOperacao = dataUltimaOperacao;
	}

	public String getResolucao() {
		return resolucao;
	}

	public void setResolucao(String resolucao) {
		this.resolucao = resolucao;
	}

	public String getCanal() {
		return canal;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	@Override
	public String toString() {
		
		if( usuario == null )
			return null;
			
		if( usuario.getPessoa() != null )
			return usuario.getPessoa().getNome();
		
		return usuario.getLogin();
	}
}