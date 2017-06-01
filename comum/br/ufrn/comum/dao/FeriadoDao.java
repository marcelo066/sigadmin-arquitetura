/*
 * Universidade Federal do Rio Grande do Norte
 * Superintendência de Informática 
 * Diretoria de Sistemas
 * 
 * Criado em: 11/08/2009
 */
package br.ufrn.comum.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.ufrn.arq.dao.GenericSharedDBDao;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.arq.util.CalendarUtils;
import br.ufrn.comum.dominio.Feriado;

/**
 * DAO para buscas relacionadas à entidade Feriado.
 * 
 * @author Jean Guerethes
 * @author Gleydson Lima
 * @author David Pereira
 */
@Component @Scope("session") 
public class FeriadoDao extends GenericSharedDBDao {

	/**
	 * Retorna a lista de feriados existentes no ano passdo como parâmetro.
	 * 
	 * @param ano
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findFeriadoAno(int ano) {
		String sql = "select f.id, f.categoria, f.data_feriado, f.descricao, uf.sigla, uf.descricao as estado, cl.nome as nome_ascii, muf.sigla as uf "+
				" from comum.feriado as f left join comum.unidade_federativa uf on (f.id_unidade_federativa = uf.id_unidade_federativa)"+
				" left join comum.municipio cl on (f.id_municipio = cl.id_municipio) left join comum.unidade_federativa muf on (muf.id_unidade_federativa = cl.id_unidade_federativa) "+
				" where date_part('YEAR', data_feriado) = ? order by data_feriado";
		return getJdbcTemplate().queryForList(sql, new Object[] { ano });
	}

	/**
	 * Busca a lista de feriados existentes no mês passado como parâmetro.
	 * 
	 * @param mes
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findFeriadoMes (Integer mes) throws DAOException {
		String sql = "select f.id, f.data_feriado, f.descricao, uf.sigla, uf.descricao as estado, cl.nome as nome_ascii, muf.sigla as uf "+ 
			" from comum.feriado as f left join comum.unidade_federativa uf on (f.id_unidade_federativa = uf.id_unidade_federativa)"+
			" left join comum.municipio cl on (f.id_municipio = cl.id_municipio) left join comum.unidade_federativa muf on (muf.id_unidade_federativa = cl.id_unidade_federativa) " +
			" where date_part('Month', data_feriado) = ? order by data_feriado ";
		return getJdbcTemplate().queryForList(sql, new Object[] { mes });
	}
	
	/**
	 * Busca a lista de feriados no mês e ano passados como parâmetro.
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> findFeriado(Integer ano, Integer mes) throws DAOException {
		String sql = "select f.id, f.data_feriado, f.descricao, uf.sigla, uf.descricao as estado, cl.nome as nome_ascii, muf.sigla as uf "+ 
			" from comum.feriado as f left join comum.unidade_federativa uf on (f.id_unidade_federativa = uf.id_unidade_federativa)"+
			" left join comum.municipio cl on (f.id_municipio = cl.id_municipio) left join comum.unidade_federativa muf on (muf.id_unidade_federativa = cl.id_unidade_federativa) " +
			" where ? = date_part('YEAR', data_feriado) and ? = date_part('Month', data_feriado)" +
			" order by data_feriado ";
		return getJdbcTemplate().queryForList(sql, new Object[] { ano, mes });
	}

	/**
	 * Dada uma data, retorna uma entidade Feriado associada a essa data. Retorna
	 * null, caso não exista feriado nessa data.
	 * @param data
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public Collection<Feriado> findByDataFeriado(Date data) throws DAOException {
		Query q = getSession().createQuery("select fe from Feriado fe where dataFeriado = :data");
		q.setDate("data", CalendarUtils.descartarHoras(data));
		return q.list();
	}
	
	/**
	 * Retorna a lista de feriados que são em dias de semana para
	 * o intervalo de datas passado como parâmetro.
	 * @param inicio
	 * @param fim
	 * @return
	 * @throws DAOException 
	 */
	@SuppressWarnings("unchecked")
	public List<Feriado> findFeriadosPorIntervaloDatas(Date inicio, Date fim) throws DAOException {
		return getSession().createQuery("select f from Feriado f where f.dataFeriado >= ? and f.dataFeriado <= ? order by f.dataFeriado asc")
			.setDate(0, CalendarUtils.descartarHoras(inicio)).setDate(1, CalendarUtils.descartarHoras(fim)).list();
	}
	
	/**
	 * Método que Retorna uma coleção de datas da tabela feriado 
	 * @return
	 * @throws HibernateException
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public Collection<Date> findDataFeriado() throws HibernateException, DAOException{
		return getSession().createQuery("select dataFeriado from Feriado").list();
	}
	
	/**
	 * Recupera todos os feriados existentes em uma determina data.
	 * Caso seja informado o muncípio e ou o estado, também filtra os registros pela localidade informada.
	 * 
	 * @param data
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public Collection<Feriado> findByDataFeriadoLocalidade(Date data,Integer municipio, Integer estado) throws DAOException {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fe FROM Feriado fe ")
		   .append(" LEFT JOIN fe.municipio municipio ")
		   .append(" LEFT JOIN fe.unidade estado ")
		   .append(" WHERE fe.dataFeriado = :data AND ")
		   .append(" ( ((municipio.id IS NULL AND estado.id IS NULL)) " );
		
		if(municipio != null)
			hql.append(" OR (municipio.id = :municipio) ");
		
		if(estado != null)
			hql.append(" OR (estado.id = :estado) ");
		
		hql.append(" ) ");
		
		Query q = getSession().createQuery(hql.toString());
		q.setDate("data", CalendarUtils.descartarHoras(data));
		
		if(municipio != null)
			q.setInteger("municipio", municipio);
		
		if(estado != null)
			q.setInteger("estado", estado);
		
		return q.list();
		
	}
	
	/**
	 * Recupera todos os feriados existentes em uma determina data.
	 * Caso seja informado o muncípio e ou o estado, também filtra os registros pela localidade informada.
	 * 
	 * @param data
	 * @return
	 * @throws DAOException
	 */
	@SuppressWarnings("unchecked")
	public List<Feriado> findByPeriodoFeriadosLocalidade(Date dataInicio,Date dataFim,Integer municipio, Integer estado) throws DAOException {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT fe FROM Feriado fe ")
		   .append(" LEFT JOIN fe.municipio municipio ")
		   .append(" LEFT JOIN fe.unidade estado ")
		   .append(" WHERE fe.dataFeriado >= :dataInicio and fe.dataFeriado <= :dataFim AND ")
		   .append(" ( ((municipio.id IS NULL AND estado.id IS NULL)) " );
		
		if(municipio != null)
			hql.append(" OR (municipio.id = :municipio) ");
		
		if(estado != null)
			hql.append(" OR (estado.id = :estado) ");
		
		hql.append(" ) ");
		
		Query q = getSession().createQuery(hql.toString());
		q.setDate("dataInicio", CalendarUtils.descartarHoras(dataInicio));
		q.setDate("dataFim", CalendarUtils.descartarHoras(dataFim));
		
		if(municipio != null)
			q.setInteger("municipio", municipio);
		
		if(estado != null)
			q.setInteger("estado", estado);
		
		return q.list();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Feriado> findFeriadosPorAnoMunicipioEstado(int ano, String municipio, String siglaEstado) throws DAOException {

		StringBuilder hql = new StringBuilder(" SELECT fe FROM Feriado fe LEFT JOIN fe.municipio municipio LEFT JOIN fe.unidade estado LEFT JOIN municipio.unidadeFederativa uf WHERE "
				+ " EXTRACT( YEAR FROM fe.dataFeriado ) = :ano AND ( (municipio.id IS NULL AND estado.id IS NULL) ");
		
		if(municipio != null)
			hql.append(" OR ( UPPER( municipio.nome ) = UPPER( TRIM(:municipio ) ) AND ( UPPER( uf.sigla ) = UPPER( TRIM(:estado ) ) ) ) ");
		
		if(siglaEstado != null)
			hql.append(" OR ( ( UPPER( estado.sigla ) = UPPER( TRIM(:estado ) ) ) AND ( municipio.id IS NULL ) ) ");
		
		hql.append(")");
		
		Query q = getSession().createQuery(hql.toString());
		q.setInteger("ano", ano);
		
		if(municipio != null) {
			q.setString("municipio", municipio);
			q.setString("estado", siglaEstado);
		}
		
		if(siglaEstado != null)
			q.setString("estado", siglaEstado);
		
		return q.list();
	}
	
}