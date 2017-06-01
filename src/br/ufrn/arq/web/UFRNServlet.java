/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 27/03/2008
 */
package br.ufrn.arq.web;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.ufrn.arq.dominio.Comando;
import br.ufrn.arq.dominio.Movimento;
import br.ufrn.arq.dominio.MovimentoCadastro;
import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.negocio.ArqListaComando;
import br.ufrn.arq.negocio.FacadeDelegate;
import br.ufrn.comum.dominio.SubSistema;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe base para cria��o de servlets nos sistemas.
 * 
 * @author Itamir Filho
 *
 */
public class UFRNServlet extends HttpServlet {

	/**
	 * Retorna o usu�rio logado
	 * @param req
	 * @return
	 */
	public UsuarioGeral getUsuarioLogado(HttpServletRequest req) {
		return (UsuarioGeral) req.getSession(false).getAttribute("usuario");
	}
	

	/**
	 * SubSistema que o usu�rio est� usando
	 * @param req
	 * @return
	 */
	public SubSistema getSubSistemaCorrente(HttpServletRequest req) {
		return (SubSistema) req.getSession().getAttribute("subsistema");
	}

	/**
	 * Chama o modelo
	 * @param mov
	 * @param req
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 * @throws RemoteException
	 */
	public Object execute(Movimento mov, HttpServletRequest req) throws NegocioException, ArqException, RemoteException {
		return getUserDelegate(req).execute(mov, req);
	}

	/**
	 * Retorna o Delegate do Usu�rio e cria caso ainda n�o possua
	 *
	 * @param req
	 * @return
	 */
	protected FacadeDelegate getUserDelegate(HttpServletRequest req)
			throws ArqException {

		HttpSession session = req.getSession(true);
		if (session == null) {
			throw new ArqException("Sess�o n�o ativada");
		} else {

			FacadeDelegate facade = (FacadeDelegate) session
					.getAttribute("userFacade");
			if (facade == null) {
				String jndiName = (String) getServletContext()
						.getAttribute("jndiName");
				facade = new FacadeDelegate(jndiName);
				session.setAttribute("userFacade", facade);
			}
			return facade;
		}
	}

	protected void atualizarObj(PersistDB obj, HttpServletRequest req, Comando comando) throws Exception {
		prepareMovimento(comando, req);
		MovimentoCadastro mov = new MovimentoCadastro();
		mov.setCodMovimento(comando);
		mov.setObjMovimentado(obj);
		mov.setUsuarioLogado(getUsuarioLogado(req));
		execute(mov, req);
	}
	protected void atualizarObj(PersistDB obj, HttpServletRequest req) throws Exception {
		atualizarObj(obj, req, ArqListaComando.ALTERAR);
	}

	public void prepareMovimento(Comando comando, HttpServletRequest req)
		throws ArqException, RemoteException, NegocioException {
		getUserDelegate(req).prepare(comando.getId(), req);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName, HttpServletRequest req) {
		WebApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext());
		return (T) ac.getBean(beanName);
	}
	
}
