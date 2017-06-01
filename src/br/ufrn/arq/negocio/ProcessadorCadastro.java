/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/10/2007
 */
package br.ufrn.arq.negocio;

import java.rmi.RemoteException;

import br.ufrn.arq.dao.GenericDAO;
import br.ufrn.arq.dominio.Movimento;
import br.ufrn.arq.dominio.MovimentoCadastro;
import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.util.UFRNUtils;

/**
 * Processador para as opera��es b�sicas de CRUD
 *
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class ProcessadorCadastro extends AbstractProcessador {

	public void validate(Movimento mov) throws NegocioException, ArqException {
		
	}

	public Object execute(Movimento movimento) throws NegocioException, ArqException, RemoteException {
		MovimentoCadastro mov = (MovimentoCadastro) movimento;
		validate(mov);
		Object obj = null;

		if (mov.getCodMovimento().equals(ArqListaComando.CADASTRAR)) {
			obj = criar(mov);
		} else if (mov.getCodMovimento().equals(ArqListaComando.ALTERAR)) {
			obj = alterar(mov);
		} else if (mov.getCodMovimento().equals(ArqListaComando.REMOVER)) {
			obj = remover(mov);
		} else if (mov.getCodMovimento().equals(ArqListaComando.DESATIVAR)) {
			obj = desativar(mov);
		}

		return obj;
	}


	protected Object criar(MovimentoCadastro mov) throws DAOException, NegocioException, ArqException {
		GenericDAO dao = getGenericDAO(mov);
		try {
			dao.create(mov.getObjMovimentado());
		} finally {
			dao.close();
		}
		return mov.getObjMovimentado();
	}

	protected Object alterar(MovimentoCadastro mov) throws DAOException, NegocioException, ArqException  {
		GenericDAO dao = getGenericDAO(mov);
		try {
			dao.update(mov.getObjMovimentado());
		} finally {
			dao.close();
		}
		return mov.getObjMovimentado();
	}

	protected Object remover(MovimentoCadastro mov) throws DAOException, NegocioException, ArqException  {
		GenericDAO dao = getGenericDAO(mov);
		try {
			dao.remove(mov.getObjMovimentado());
		} catch (Exception e) {
			if (UFRNUtils.isFKConstraintError(e))  {
				NegocioException ne = new NegocioException(e);
				ne.addErro("Esse registro n�o pode ser removido, pois est� associado a outros registros da base de dados.");
				throw ne;
			}
			throw new DAOException(e);
		} finally {
			dao.close();
		}
		return mov.getObjMovimentado();
	}

	protected Object desativar(MovimentoCadastro mov) throws DAOException, NegocioException, ArqException  {
		GenericDAO dao = getGenericDAO(mov);
		PersistDB obj = mov.getObjMovimentado();
		try {
			dao.updateField(obj.getClass(), obj.getId(), "ativo", false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DAOException(e);
		} finally {
			dao.close();
		}
		return mov.getObjMovimentado();
	}

}