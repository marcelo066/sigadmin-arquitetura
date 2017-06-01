/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 23/04/2009
 */
package br.ufrn.arq.web.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;

import javax.faces.application.ViewExpiredException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajax4jsf.resource.ResourceNotFoundException;


/**
 * Classe para tratar as exce��es causadas pelo java server faces.
 * 
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class JsfExceptions extends ExceptionChain {

	@Override
	public ExceptionHandlerResult processar(Throwable e, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		Boolean device =  (Boolean) req.getSession().getAttribute("device");

		/*
		 * Erro de digita��o de endere�o inv�lido
		 */
		if (e != null && e.getMessage() != null && e.getMessage().indexOf("Cannot find FacesContext") != -1) {
			if(device == null) {
				req.getSession().setAttribute("alertErro", "O endere\\347o digitado n\\343o \\351 v\\341lido. Por favor, reinicie o processo utilizando os links oferecidos pelo sistema.");
				redirectSubSistema(req, res);
			}
			
			return ExceptionHandlerResult.STOP;
		}
		
		/*
		 * Exibir mensagem ao usu�rio no caso de Target Unreachable
		 */
		if (e != null && e.getMessage() != null && (e.getMessage().indexOf("Target Unreachable") != -1)) {
			if(device == null) {
				req.getSession().setAttribute("alertErro", "Voc\\352 utilizou o bot\\343o voltar do navegador, o que n\\343o \\351 recomendado. Por favor, reinicie o processo utilizando os links oferecidos pelo sistema.");
				redirectSubSistema(req, res);
			}
			
			return ExceptionHandlerResult.STOP;
		}
		
		/*
		 * ViewExpiredException
		 */
		if ((e != null && e instanceof ViewExpiredException) || (e != null && e.getCause() != null && e.getCause() instanceof ViewExpiredException)) {
			if(device == null) {
				req.getSession().setAttribute("alertErro", "A sua sess\\343o expirou ou voc\\352 utilizou o bot\\343o voltar do navegador, o que n\\343o \\351 recomendado. Por favor, reinicie o processo utilizando os links oferecidos pelo sistema.");
				redirectSubSistema(req, res);
			}
			
			return ExceptionHandlerResult.STOP;
		}
		
		/*
		 * NoSuchElementException 
		 */ 
		if (e != null && (e instanceof NoSuchElementException || (e.getCause() != null && e.getCause() instanceof NoSuchElementException))) {
			if (e.getCause() != null)
				e = e.getCause();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String stacktrace = sw.toString();
		
			if (stacktrace.indexOf("SelectItemsIterator") != -1 ) {
				if(device == null) {
					req.getSession().setAttribute("alertErro", "Voc\\352 utilizou o bot\\343o voltar do navegador, o que n\\343o \\351 recomendado. Por favor, reinicie o processo utilizando os links oferecidos pelo sistema.");
					redirectSubSistema(req, res);
				}
				
				return ExceptionHandlerResult.STOP;
			}
		}
		
		/*
		 * Ignorar "Colors cannot be null", que acontece com barras de progresso do RichFaces. 
		 */
		if (e != null && e instanceof NullPointerException && (e.getMessage() != null && e.getMessage().indexOf("Colors cannot be null") != -1)) {
			return ExceptionHandlerResult.IGNORE;
		}
		
		/*
		 * Erro de ResourceNotFoundException (Calendar do RichFaces). D� erro em virtude
		 * do cache do browser, mas o erro n�o � vis�vel ao usu�rio. A URL utilizada para
		 * buscar o calend�rio � interceptada pelo view filter e o erro acontece, mas o browser
		 * acaba usando o cache e o usu�rio n�o percebe. Segundo 
		 * http://seamframework.org/Community/RichFacesIssueResourceNotRegistered,
		 * o problema s� � resolvido com uma limpeza no cache do browser.
		 */
		if (e instanceof ResourceNotFoundException) {
			return ExceptionHandlerResult.IGNORE;
		}
		
		return ExceptionHandlerResult.CONTINUE;
	}
	
}
