/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 30/05/2005
 */
package br.ufrn.arq.util;

/**
 * Classe privada usada para manipular links;
 * 
 * @author Rafael Borja
 * @author Gleydson Lima
 *  
 */
public class Link {
    /** T�tulo do link */
    private String titulo;

    /** Endere�o do link */
    private String url;

    public Link() {
        titulo = new String();
        url = new String();
    }

    public Link(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}