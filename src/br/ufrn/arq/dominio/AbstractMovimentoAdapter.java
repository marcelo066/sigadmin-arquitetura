/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 03/01/2005
 */
package br.ufrn.arq.dominio;

/**
 * Adapta o AbstractMovimento para Object.
 * Usado para movimentos n�o persistentes
 *
 * @author Gleydson Lima
 *
 */
public class AbstractMovimentoAdapter extends AbstractMovimento {

    public int getId() {
        return 0;
    }

    public void setId(int id) {
    }

    
}
