/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 25/08/2009
 */
package br.ufrn.arq.migracao;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe auxiliar para quebrar as linhas de um arquivo
 * de layout fixo e extrair delas as informa��es importantes
 * para posterior processamento.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class LineSplitter {
	
	/**
	 * Quebra uma linha em um conjunto de srings nos pontos
	 * informados atrav�s do par�metro tamanhos.
	 * 
	 * @param linha
	 * @param tamanhos
	 * @return
	 */
	public List<String> split(String linha, Integer... tamanhos) {
		List<String> partes = new ArrayList<String>();
		int start = 0;
		for (int fim : tamanhos) {
			partes.add(linha.substring(start, start + fim).trim());
			start = start + fim;
		}
		return partes;
	}
	
}