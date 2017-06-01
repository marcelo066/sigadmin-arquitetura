/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/07/2006
 */
package br.ufrn.arq.dao;

/**
 * Esse DAO � implementado por todos os DAOS que precisam ter comportamento de
 * pagina��o.
 * 
 * @author Gleydson Lima
 *  
 */
public interface PaginableDAO {

    public boolean isPaginable();

    public void setPaginable(boolean paginable);

    public int getPageNum();

    public void setPageNum(int pageNum);

    public int getPageSize();

    public void setPageSize(int pageSize);

    public int getCount();

    public void setCount(int count);
    
    public int getTotalPaginas();
}