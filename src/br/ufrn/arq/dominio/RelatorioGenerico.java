/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/10/2007
 */
package br.ufrn.arq.dominio;

import java.util.HashMap;
import java.util.List;

/**
 * Estrutura gen�rica de relat�rio usado para v�rias consultas gen�ricas
 * jogando para uma �nica JSP
 *
 * @author Gleydson Lima
 *
 */
public class RelatorioGenerico {

	// t�tulo do relat�rio
	private String titulo;

	// colunas do t�tulo
	private List<String> colunas;

	// linhas do resultado
	List<HashMap<String, Object>> linhas;

	public List<String> getColunas() {
		return colunas;
	}

	public void setColunas(List<String> colunas) {
		this.colunas = colunas;
	}

	public List<HashMap<String, Object>> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<HashMap<String, Object>> linhas) {
		this.linhas = linhas;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

}
