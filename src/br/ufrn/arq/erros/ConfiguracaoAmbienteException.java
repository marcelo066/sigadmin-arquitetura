/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 29/04/2010
 */
package br.ufrn.arq.erros;


/**
 * Esta exce��o � emitida sempre que o erro de configura��o no ambiente.
 * Por exemplo: falta de parametros.
 *
 * @author Gleydson Lima
 * @author David Pereira
 */
public class ConfiguracaoAmbienteException extends RuntimeException {

	public ConfiguracaoAmbienteException(Exception e) {
		super(e);
	}

	public ConfiguracaoAmbienteException(String msg) {
		super(msg);
	}
	
	public ConfiguracaoAmbienteException(String msg, Exception e) {
		super(msg, e);
	}

	public ConfiguracaoAmbienteException() {
		super("Erro de configura��o no ambiente");
	}

}