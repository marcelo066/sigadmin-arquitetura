/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 07/07/2005
 */
package br.ufrn.arq.seguranca.log;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;

import br.ufrn.arq.dao.RegistroEntradaDAO;
import br.ufrn.arq.dao.RegistroEntradaImpl;
import br.ufrn.arq.dominio.RegistroEntrada;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.DAOException;

/**
 * Informa��es de estat�sticas de usu�rios
 *
 * @author Gleydson Lima
 *
 */
public class UserAgent {

	/**
	 * Busca registros de entrada dos �ltimos usu�rios logados.
	 * @param desdeHora
	 * @param sistema
	 * @return
	 * @throws DAOException
	 */
	public static Collection<RegistroEntrada> getLogadosDesde(int desdeHora, int sistema) throws DAOException {
		RegistroEntradaDAO dao = new RegistroEntradaImpl(sistema);
		Collection<RegistroEntrada> registros = null;

		try {
			registros = dao.findByTempo(desdeHora);
			for (RegistroEntrada r : registros) {
				r.setDataUltimaOperacao(dao.findByUltimaAtividade(r.getId()));
			}
		} finally {
			dao.close();
		}

		return registros;
	}

	/**
	 * Retorna os logs que apresentaram erros nos �ltimos minutos. 
	 * @param minutos
	 * @param sistema
	 * @return
	 * @throws DAOException
	 */
	public static Collection<LogOperacao> getAtividadesErro(int minutos, int sistema) throws DAOException {
		RegistroEntradaDAO dao = new RegistroEntradaImpl(sistema);
		Collection<LogOperacao> operacoes = null;

		try {
			operacoes = dao.findLogErro(minutos);
		} finally {
			dao.close();
		}

		return operacoes;
	}

	/**
	 * Retorna os registros de entrada dos usu�rios que realizaram alguma opera��o nos �ltimos minutos. 
	 * @param desdeAtividade
	 * @param sistema
	 * @return
	 * @throws DAOException
	 */
	public static Collection<RegistroEntrada> getLogadosAtividade(int desdeAtividade, int sistema) throws DAOException {
		RegistroEntradaDAO dao = new RegistroEntradaImpl(sistema);
		Collection<RegistroEntrada> registros = null;

		try {
			registros = dao.findByAtividade(desdeAtividade);

			for (RegistroEntrada r : registros) {
				r.setDataUltimaOperacao(dao.findByUltimaAtividade(r.getId()));
				//Para evitar erro de lazy posteriormente
				r.getUsuario().getUnidade().getSigla();
			}
		} finally {
			dao.close();
		}

		return registros;
	}

	/**
	 * Retorna os registros de entrada do usu�rio cujo login foi passado como par�metro.
	 * @param login
	 * @param sistema
	 * @return
	 * @throws DAOException
	 */
	public static Collection<RegistroEntrada> getByLogin(String login, int sistema) throws DAOException {
		RegistroEntradaDAO dao = new RegistroEntradaImpl(sistema);
		Collection<RegistroEntrada> registros = null;

		try {
			registros = dao.findByLogin(login);
		} finally {
			dao.close();
		}

		return registros;
	}

	/** 
	 * Loga a opera��o que o usu�rio est� fazendo 
	 */
	public static void logaOperacao(String url, long tempo, int idRegistroEntrada, String parametros, int sistema, String mensagens ) {
		LogOperacao log = new LogOperacao();
		log.setAction(url);
		log.setIdRegistroEntrada(idRegistroEntrada);
		log.setParameters(parametros);
		log.setTempo(tempo);
		log.setSistema(sistema);
		log.setMensagens(mensagens);

		LogProcessorDelegate.getInstance().writeOperacaoLog(log);
	}
	
	
	/**
	 * Loga a opera��o que o usu�rio est� fazendo
	 */
	public static void logaOperacaoPublica(String url, long tempo, int idAcessoPublico, String parametros, int sistema ) {
		LogOperacao log = new LogOperacao();
		log.setAction(url);
		log.setIdRegistroAcessoPublico(idAcessoPublico);
		log.setParameters(parametros);
		log.setTempo(tempo);
		log.setSistema(sistema);

		LogProcessorDelegate.getInstance().writeOperacaoLog(log);
	}

	/**
	 * Registra uma opera��o que apresentou erro de execu��o.
	 * @param url
	 * @param tempo
	 * @param idRegistroEntrada
	 * @param parametros
	 * @param e
	 * @param sistema
	 */
	public static void logaOperacaoErro(String url, long tempo, int idRegistroEntrada, String parametros, Throwable e, int sistema) {
		LogOperacao log = new LogOperacao();
		log.setAction(url);
		log.setIdRegistroEntrada(idRegistroEntrada);
		log.setParameters(parametros);
		log.setTempo(tempo);
		log.setErro(true);
		log.setSistema(sistema);

		if (e instanceof ArqException) {
			ArqException arqEx = (ArqException) e;
			if (arqEx.getCause() != null)
				e = arqEx.getCause();
		}

		if (e instanceof ServletException) {
			ServletException servletEx = (ServletException) e;
			if ((servletEx.getRootCause() != null))
				e = servletEx.getRootCause();
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter pOut = new PrintWriter(out);
		e.printStackTrace(pOut);

		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(pOut);
			cause = cause.getCause();
		}
		pOut.flush();

		log.setTracing(out.toString());

		LogProcessorDelegate.getInstance().writeOperacaoLog(log);
	}

}