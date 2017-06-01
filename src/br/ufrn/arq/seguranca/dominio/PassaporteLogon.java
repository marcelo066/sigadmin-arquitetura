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

import br.ufrn.arq.dominio.AbstractMovimento;

/**
 * Registra a entrada de um usu�rio em um sistema atrav�s de passaporte. 
 * Utilizado para um usu�rio ir de um sistema para outro sem a necessidade de um novo logon.
 *
 * @author Gleydson Lima
 *
 */
@Entity
@Table(name = "passaporte_logon", schema="infra")
public class PassaporteLogon extends AbstractMovimento {

	@Id
	@Column(name = "id_passaporte", nullable = false)
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
           parameters={ @Parameter(name="sequence_name", value="hibernate_sequence") })
	private int id;

	/** login do usu�rio */
	private String login;

	/** id do usu�rio */
	@Column(name = "id_usuario")
	private int idUsuario;

	/** Registro de entrada do usu�rio */
	@Column(name = "id_registro")
	private int idRegistro;

	/** Sistema para onde o usu�rio est� indo */
	@Column(name = "sistema_alvo")
	private int sistemaAlvo;

	/** Sistema de onde o usu�rio est� saindo */
	@Column(name = "sistema_origem")
	private int sistemaOrigem;

	/** Timestamp da validade do passaporte */
	private Long validade;

	/** hora em que o passaporte foi criado */
	private Date hora;
	
	/** A��o a ser executada (m�todo de managed bean) ap�s o logon */
	private String acao;

	/** Id da unidade que o usu�rio dever� utilizar no sistema ap�s o logon */
	@Column(name="id_unidade")
	private Integer idUnidade;
	
	/**
	 * 
	 * Usado no SIGAA para quando o usua�rio vier de outro sistema ser redirecionado para o caso de uso espec�fico. 
	 * <br>
	 * Aqui interessa somente o caminho relativo sem o contexto, por exemplo de conte�do: /verPortalDiscente.do
	 */
	@Column(name="url")
	private String url;
	
	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public Long getValidade() {
		return validade;
	}
	
	public void setValidade(Long validade) {
		this.validade = validade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdRegistro() {
		return idRegistro;
	}

	public void setIdRegistro(int idRegistro) {
		this.idRegistro = idRegistro;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getSistemaAlvo() {
		return sistemaAlvo;
	}

	public void setSistemaAlvo(int sistemaAlvo) {
		this.sistemaAlvo = sistemaAlvo;
	}

	public int getSistemaOrigem() {
		return sistemaOrigem;
	}

	public void setSistemaOrigem(int sistemaOrigem) {
		this.sistemaOrigem = sistemaOrigem;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Integer getIdUnidade() {
		return idUnidade;
	}

	public void setIdUnidade(Integer idUnidade) {
		this.idUnidade = idUnidade;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
