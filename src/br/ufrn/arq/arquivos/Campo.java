/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 12/12/2009
 */
package br.ufrn.arq.arquivos;

import java.io.Serializable;
import java.math.BigDecimal;

import br.ufrn.arq.util.UFRNUtils;

/**
 * Identifica uma posi��o dentro de um layout de arquivo. Usado tanto para
 * leitura como para gera��o de arquivos textos
 *
 * @author Gleydson Lima
 *
 */
public class Campo implements Serializable {

	/** Nome do campo no arquivo */
	private String nome;

	/** Posi��o inicial do campo */
	private int inicio;

	/** Posi��o final do campo */
	private int fim;

	/** Tipo do campo - (X) Alfanum�rico e (9) Num�rico */
	private char tipo;

	/** Valor do campo */
	private String value;
	
	/** Quantidade de casas decimais, caso seja um n�mero decimal */
	private int qtdCasasDecimais;

	/**
	 * Construtor recebendo o nome.
	 * @param campo
	 */
	public Campo(String campo) {
		this.nome = campo;
	}
	
	/**
	 * Construtor recebendo o nome, posi��o inicial, posi��o final e o tipo do campo.
	 * @param nome
	 * @param inicio
	 * @param fim
	 * @param tipo
	 */
	public Campo(String nome, int inicio, int fim, char tipo) {
		this.nome = nome;
		// A partir da posi��o 0
		this.inicio = inicio - 1;
		this.fim = fim;
		this.tipo = tipo;
	}
	
	/**
	 * Construtor recebendo o nome, posi��o inicial, posi��o final, quantidade de casas decimais e o tipo do campo.
	 * @param nome
	 * @param inicio
	 * @param fim
	 * @param qtdCasasDecimais
	 * @param tipo
	 */
	public Campo(String nome, int inicio, int fim, int qtdCasasDecimais, char tipo) {
		this.nome = nome;
		this.inicio = inicio;
		this.fim = fim;
		this.qtdCasasDecimais = qtdCasasDecimais;
		this.tipo = tipo;
	}

	/**
	 * Completa o valor do campo com zeros.
	 */
	public void preencheZero() {
		setValue(UFRNUtils.completaZeros(0, fim - inicio));
	}

	/**
	 * Completa o valor do campo espa�os em branco.
	 */
	public void preencheEspaco() {
		setValue(UFRNUtils.completaEspacos(" ", fim - inicio));
	}

	public int getFim() {
		return fim;
	}

	public void setFim(int fim) {
		this.fim = fim;
	}

	public int getInicio() {
		return inicio;
	}

	public void setInicio(int inicio) {
		this.inicio = inicio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setValue(Integer value) {
		this.value = String.valueOf(value);
	}
	
	public void setValue(BigDecimal value) {
		this.value = String.valueOf(value);
	}

	public void extractValue(String linha) {
		value = linha.substring(inicio, fim);
//		try {
//			Integer.parseInt(value);
//		} catch (NumberFormatException e) {
//			value = "'" + value + "'";
//		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Campo other = (Campo) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

	public char getTipo() {
		return tipo;
	}

	public void setTipo(char tipo) {
		this.tipo = tipo;
	}

	public int getQtdCasasDecimais() {
		return qtdCasasDecimais;
	}

	public void setQtdCasasDecimais(int qtdCasasDecimais) {
		this.qtdCasasDecimais = qtdCasasDecimais;
	}
	
	/**
	 * Retorna o tamanho do campo.
	 * OBS: � adicionado 1 ao fim pois o tamanho dos campos tem intervalo fechado.
	 * Exemplo: Um campo que vai de 5 a 10 na verdade tem 6 posi��es, ou seja, ((10 + 1) - 5).
	 * @return
	 */
	public int size(){
		return fim - inicio;
	}

	@Override
	public String toString() {
		return nome + " :" + value;
	}
}