/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 28/11/2008
 */
package br.ufrn.arq.tasks;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import br.ufrn.arq.dao.Database;
import br.ufrn.arq.dao.JdbcTemplate;
import br.ufrn.arq.dao.PermissaoDAO;
import br.ufrn.arq.util.ValidatorUtil;
import br.ufrn.comum.dominio.Papel;
import br.ufrn.comum.dominio.Sistema;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe para timer de consultas com informacoes de acesso aos bancos administrativo e academico e comum
 * 
 *  Iprojet Tarefa n�mero: 1980  (Fazer Timer para Envio do Relat�rio de Fornecedores em Atraso)
 *  Envio Autom�tico de Informa��es
 *  Criar os timers das consultas feitas por William.
 *  As Consultas s�o :
 *   Fazer as consultas SQL para as seguintes listagens
 *	- Aditivos de contratos na semana, separados por contrato.
 *	- Pagamentos de contratos na semana.
 *	- Medi��es de Obras cadastradas por intervalos de data.
 *	- Gastos da Unidade no intervalo de data, detalhando a rubrica e o valor.
 *	- Contratos cadastrados em um intervalo de data.
 *	- Atendimentos do almoxarifado no DIA. (todos os materiais, quantidades e setores)
 *	- Tombamentos realizados no DIA, com bens e termos.
 *
 * @author Raphaela Galhardo
 * @author Gleydson Lima
 *
 */
public abstract class TimerConsultas extends TarefaTimer {

	private int sistema;

	private JdbcTemplate template;
	
	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public Connection getConnection(int sistema) throws SQLException {
		return Database.getInstance().getConnection(new Sistema(sistema));
	}
	
	public DataSource getDataSource(int sistema) {
		return Database.getInstance().getDataSource(new Sistema(sistema));
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return getJdbcTemplate(Database.getInstance().getDataSource(new Sistema(sistema)));
	}

	public JdbcTemplate getJdbcTemplate(DataSource ds) {
		if (template == null)
			template = new JdbcTemplate(ds);
		return template;
	}
	
	public JdbcTemplate getJdbcTemplate(Sistema sistema) {
		return getJdbcTemplate(Database.getInstance().getDataSource(sistema));
	}
	
	public JdbcTemplate getJdbcTemplate(int sistema) {
		return getJdbcTemplate(Database.getInstance().getDataSource(new Sistema(sistema)));
	}
	
	/**
	 * Retorna os emails do usu�rios por papel. Utilizado para facilitar a cria��o de timers
	 * que enviam emails para usu�rios com papeis espec�ficos.
	 * @param idPapel
	 * @return
	 */
	public String getEmailsPorPapel(int idPapel) {
		PermissaoDAO permissaoDao = new PermissaoDAO();
		String emails = "";
		try {
			//buscando usu�rios por papel
			Collection<UsuarioGeral> usuarios = permissaoDao.findByPapel(new Papel(idPapel));
			StringBuilder listaEmail = new StringBuilder();
			if(!ValidatorUtil.isEmpty(usuarios)) {
				for (UsuarioGeral usuario : usuarios) {
					listaEmail.append(usuario.getEmail() + ";");
				}
				//retirando a �ltima ","
				emails = listaEmail.toString().substring(0, listaEmail.toString().lastIndexOf(";"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			permissaoDao.close();
		}
		return emails;
	}
	
}
