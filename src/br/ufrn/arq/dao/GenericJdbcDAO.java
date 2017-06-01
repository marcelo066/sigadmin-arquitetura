/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/11/2008
 */
package br.ufrn.arq.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * DAO gen�rico que utiliza JDBCTemplate 
 * para gerenciar persist�ncias
 * 
 * @author Ricardo Wendell
 * @author Gleydson Lima
 *
 */
public class GenericJdbcDAO {

	private DataSource sipacDs = null;
	private DataSource sigaaDs = null;
	private DataSource comumDs = null;

	private JdbcTemplate sipacTemplate = null;
	private JdbcTemplate sigaaTemplate = null;
	private JdbcTemplate comumTemplate = null;
	
	public GenericJdbcDAO() {
		sipacDs = Database.getInstance().getSipacDs();
		sigaaDs = Database.getInstance().getSigaaDs();
		comumDs = Database.getInstance().getComumDs();
	}

	public JdbcTemplate getComumTemplate() {
		if (comumTemplate == null) {
			comumTemplate = new JdbcTemplate(comumDs);
		}
		return comumTemplate;
	}

	public JdbcTemplate getSigaaTemplate() {
		if (sigaaTemplate == null) {
			sigaaTemplate = new JdbcTemplate(sigaaDs);
		}
		return sigaaTemplate;
	}

	public JdbcTemplate getSipacTemplate() {
		if (sipacTemplate == null) {
			sipacTemplate = new JdbcTemplate(sipacDs);
		}
		return sipacTemplate;
	}
	
}
