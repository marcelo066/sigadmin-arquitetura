/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 18/11/2008
 */
package br.ufrn.arq.dao;



/**
 * DAO respons�vel pelas consultas a informa��es sobre bloqueio de emiss�o documentos autenticados.
 *
 * @author Ricardo Wendell
 *
 */
public class BloqueioDocumentoAutenticadoDAO extends GenericSharedDBDao {

	/**
	 * Verificar se um determinado tipo de documento est� bloqueado para emiss�o.
	 *
	 * @param tipoDocumento
	 * @param nivelEnsino
	 * @return
	 */
	public boolean isDocumentoLiberado(int tipoDocumento, Character nivelEnsino) {
		String hql = "select id from BloqueioDocumentoAutenticado " +
				" where bloqueado = trueValue() and tipoDocumento = ?";

		Object[] parametros;
		if (nivelEnsino != null) {
			parametros = new Object[] {tipoDocumento, nivelEnsino};
			hql += " and (nivelEnsino is null or nivelEnsino = ?)";
		} else {
			parametros = new Object[] {tipoDocumento};
			hql += " and nivelEnsino is null";
		}

		return getHibernateTemplate().find(hql,parametros).isEmpty();
	}

}
