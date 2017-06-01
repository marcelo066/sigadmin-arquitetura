/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 03/02/2009
 */
package br.ufrn.arq.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufrn.arq.erros.DAOException;

/**
 * DAO para intergra��o dos sub-sistemas de projetos acad�micos(pesquisa, monitoria e extens�o) com outros
 * m�dulos do SIGAA.
 * 
 * @author ilueny santos
 * @deprecated Utilizar servi�os remotos expostos pelo Spring HTTP Invoker.
 * 
 */
@SuppressWarnings("unchecked")
@Deprecated
public class ProjetosAcademicosDao extends GenericJdbcDAO {

	/**
	 * Lista o t�tulo de todos os projetos de monitoria e extens�o que est�o
	 * pendentes de autoriza��o por parte dos departamentos envolvidos.
	 * 
	 * @return
	 * @throws DAOException
	 */
	public List<Map<String, Object>> findProjetosMonitoriaExtenssaoPendentesAutorizacao(int idUnidade, int prazo)
			throws DAOException {

		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		String sqlExtensao = "select distinct 'Projeto de Extens�o: ' || p.titulo as titulo from extensao.autorizacao_atividade_extensao a inner join " 
				+ "projetos.projeto p on a.id_atividade = p.id_projeto where a.data_autorizacao is null and a.ativo = trueValue() and a.id_unidade = ? "
				+ "and (date(a.data_cadastro) + ?) < now()";
		
		String sqlMonitoria = "select distinct 'Projeto de Monitoria: ' || p.titulo as titulo from monitoria.autorizacao_projeto_monitoria a inner join " 
				+ "projetos.projeto p on a.id_projeto_monitoria = p.id_projeto where data_autorizacao is null and a.ativo = trueValue() and a.id_unidade = ? "
				+ "and (date(a.data_cadastro) + ?) < now()";

		result.addAll(getSigaaTemplate().queryForList(sqlExtensao, new Object[] { idUnidade, prazo }));
		result.addAll(getSigaaTemplate().queryForList(sqlMonitoria, new Object[] { idUnidade, prazo }));

		return result;

	}
}
