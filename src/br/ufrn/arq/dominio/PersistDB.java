/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 15/09/2004
 */
package br.ufrn.arq.dominio;

import java.io.Serializable;

/**
 * Interface implementada por todos os objetos que ser�o
 * persistidos pela camada de mapeamento objeto relacional.
 * 
 *  
 * @author Gleydson Lima
 *
 */
public interface PersistDB extends Serializable {
    
    public int getId();
    
    public void setId(int id);
}
