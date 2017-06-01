/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 08/04/2010
 */
package br.ufrn.arq.erros;

import br.ufrn.arq.seguranca.dominio.TokenAutenticacao;

/**
 * Exce��o para identificar quanto um {@link TokenAutenticacao}
 * n�o foi validado, portanto a autententica��o n�o
 * foi bem sucedida e a a��o n�o deve ser realizada.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class TokenNaoValidadoException extends RuntimeException {

	public TokenNaoValidadoException(String msg) {
        super(msg);
    }

    public TokenNaoValidadoException(Throwable cause) {
        super(cause);
    }

    public TokenNaoValidadoException(String msg, Throwable cause) {
    	super(msg, cause);
    }
	
}
