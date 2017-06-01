/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 23/04/2009
 */
package br.ufrn.arq.web.exceptions;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.NonUniqueResultException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.StaleStateException;
import org.hibernate.exception.ConstraintViolationException;

import br.ufrn.arq.erros.DAOException;


/**
 * Classe para tratar as exce��es causadas pelo Hibernate.
 * 
 * @author Gleydson Lima 
 * @author David Pereira
 *
 */
public class HibernateExceptions extends ExceptionChain {

	@Override
	public ExceptionHandlerResult processar(Throwable e, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		if (e instanceof StaleStateException || (e.getCause() != null && e.getCause() instanceof StaleStateException) || (e.getCause() != null && e.getCause().getCause() != null && e.getCause().getCause() instanceof StaleStateException)) {
			addMensagemErro("O elemento selecionado n�o se encontra mais no banco de dados.", req, res);
			return ExceptionHandlerResult.STOP;
		} 
		
		if (e instanceof StaleObjectStateException) {
			addMensagemErro("O elemento selecionado foi modificado por outra pessoa e n�o pode mais ser utilizado. Por favor, reinicie a opera��o.", req, res);
			return ExceptionHandlerResult.STOP;
		}
		
		if (e instanceof ConstraintViolationException || (e instanceof DAOException && e.getCause() != null && e.getCause() instanceof ConstraintViolationException)) {
			addMensagemErro("O elemento selecionado n�o pode ser removido porque ainda est� sendo referenciado pelo sistema.", req, res);
			return ExceptionHandlerResult.STOP;
		}
		
		if (e instanceof NonUniqueResultException || (e instanceof DAOException && e.getCause() != null && e.getCause() instanceof NonUniqueResultException)) {
			String msg = "Caro usu�rio, apenas um resultado era esperado na opera��o realizada por�m, o banco de dados retornou ";
			
			NonUniqueResultException nure = null;
			if (e instanceof NonUniqueResultException) {
				nure = (NonUniqueResultException) e;
			} else {
				nure = (NonUniqueResultException) e.getCause();
			}

			msg += nure.getMessage().split(":")[1] + ". Isto pode ser um problema de migra��o ou inconsist�ncia de dados. Entre em contato com a inform�tica atrav�s do Abrir Chamado.";
			addMensagemErro(msg, req, res);
			return ExceptionHandlerResult.STOP;
		}
		
		if (e instanceof SQLException || (e.getCause() != null && e.getCause() instanceof SQLException)) {
			String message = null;
			if (e instanceof SQLException ) {
				message = e.getMessage();
			} else {
				message = e.getCause().getMessage();
			}
			
			if (message.contains("does not exist")) {
				StackTraceElement[] stackTrace = e.getStackTrace();
				StackTraceElement ultimo = null;
				for (StackTraceElement element : stackTrace) {
					if (element.getClassName().startsWith("br.ufrn.")) {
						ultimo = element;
						break;
					}
				}
				
				if (message.contains("relation")) {
					String relation = message.replaceAll("does not exist", "").replaceAll("ERROR: relation", "");
					String msg = "ERRO: A tabela " + relation + " n�o existe. ";
					if (ultimo != null) msg += "Ver m�todo " + ultimo.getMethodName() + " da classe " + ultimo.getClassName() + ".";
					addMensagemErro(msg, req, res);
					return ExceptionHandlerResult.STOP;
				} else if (message.contains("column")) {
					String column = message.replaceAll("does not exist", "").replaceAll("ERROR: column", "");
					String msg = "ERRO: A coluna " + column + "n�o existe. ";
					if (ultimo != null) msg += "Ver m�todo " + ultimo.getMethodName() + " da classe " + ultimo.getClassName() + ".";
					addMensagemErro(msg, req, res);
					return ExceptionHandlerResult.STOP;
				}
			}
		}
		
		
		
		return ExceptionHandlerResult.CONTINUE;
	}

}
