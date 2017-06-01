/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.seguranca.autenticacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface usada para valida��o dos documentos.
 *
 * @author Gleydson Lima
 *
 */
public interface AutValidator {

	/**
	 * Valida se o documento emitido � o mesmo do atual
	 * @param comprovante
	 * @return
	 */
	public boolean validaDigest(EmissaoDocumentoAutenticado comprovante);

	/**
	 * Segundo passo, exibir depois de validar
	 * @param comprovante
	 * @param req
	 * @param res
	 */
	public void exibir(EmissaoDocumentoAutenticado comprovante,
			HttpServletRequest req, HttpServletResponse res);



}