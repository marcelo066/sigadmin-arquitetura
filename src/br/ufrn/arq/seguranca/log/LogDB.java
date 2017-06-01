/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 30/03/2005
 */
package br.ufrn.arq.seguranca.log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Classe utilizada para Log de opera��es de altera��o no banco de Dados
 *
 * @author Gleydson Lima
 *
 */
@Entity
@Table(name = "log_db")
public class LogDB implements LogUFRN {

	@Id
	@Column(name = "operacao", nullable = false)
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
           parameters={ @Parameter(name="sequence_name", value="hibernate_sequence") })
	/** Tipo da opera��o de log. (I) Insert, (U) Update, (D) Delete, (R) Read */
	private char operacao;

	@Column(name = "id_elemento")
	/** Valor do identificador da linha alterada da tabela. */
	private int idElemento;

	@Column(name = "id_registro_entrada")
	/** Registro de entrada do usu�rio que efetua a opera��o */
	private int idRegistroEntrada;
	
	@Column(name = "id_usuario")
	/** Registro de entrada do usu�rio que efetua a opera��o */
	private int idUsuario;
	
	/** Data da realiza��o da opera��o. */
	private Date data;

	/** Tabela do banco de dados que est� realizando a opera��o. 
	 * No caso, registra-se o nome da classe correspondente. */
	private String tabela;
	
	/** ID da TurmaVirtual para registrar o acesso do Discente assim que ele entra */
    private int turmaVirtual;

	@Column(name = "cod_movimento")
	/** C�digo do processador onde a opera��o � processada. */
	private int codMovimento;

	/** Sistema em que se realiza a opera��o. 1 - SIPAC, 2 - PROTOCOLO, 3 - SCO, 4 - SIGAA */
	private int sistema;

	@Column(name = "alteracoes")
	/** Altera��es realizadas */
	private String alteracao;

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	/**
	 * @return Returns the data.
	 */
	public Date getData() {
		return data;
	}

	/**
	 * @param data
	 *            The data to set.
	 */
	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return Returns the idElemento.
	 */
	public int getIdElemento() {
		return idElemento;
	}

	/**
	 * @param idElemento
	 *            The idElemento to set.
	 */
	public void setIdElemento(int idElemento) {
		this.idElemento = idElemento;
	}

	/**
	 * @return Returns the operacao.
	 */
	public char getOperacao() {
		return operacao;
	}

	/**
	 * @param operacao
	 *            The operacao to set.
	 */
	public void setOperacao(char operacao) {
		this.operacao = operacao;
	}

	/**
	 * @return Returns the tabela.
	 */
	public String getTabela() {
		return tabela;
	}

	/**
	 * @param tabela
	 *            The tabela to set.
	 */
	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	/**
	 * @return Returns the codMovimento.
	 */
	public int getCodMovimento() {
		return codMovimento;
	}

	/**
	 * @param codMovimento
	 *            The codMovimento to set.
	 */
	public void setCodMovimento(int codMovimento) {
		this.codMovimento = codMovimento;
	}

	@Override
	public String toString() {
		return operacao + " " + idElemento + " " + idRegistroEntrada + " "
				+ tabela + " (" + sistema + ")";
	}

	public String getAlteracao() {
		return alteracao;
	}

	public void setAlteracao(String alteracao) {
		this.alteracao = alteracao;
	}

	public int getIdRegistroEntrada() {
		return idRegistroEntrada;
	}

	public void setIdRegistroEntrada(int idRegistroEntrada) {
		this.idRegistroEntrada = idRegistroEntrada;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

    public int getTurmaVirtual() {
        return turmaVirtual;
    }

    public void setTurmaVirtual(int turmaVirtual) {
        this.turmaVirtual = turmaVirtual;
    }

}