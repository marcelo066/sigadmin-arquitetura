/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 04/10/2007
 */
package br.ufrn.arq.erros;

/**
 * Exce��o disparada quando uma consulta retorna um n�mero
 * maior de resultados que o permitido, exigindo que o usu�rio
 * restrinja os termos da busca.
 * 
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class LimiteResultadosException extends DAOException {

    public LimiteResultadosException(String msg) {
        super(msg);
        setNotificavel(false);
    }

	public LimiteResultadosException() {
		super("O limite de resultados foi ultrapassado. Por favor, refine a consulta.");
		setNotificavel(false);
	}
    public LimiteResultadosException(Exception e) {
        super("O limite de resultados foi ultrapassado. Por favor, refine a consulta.");
        setNotificavel(false);
    }

    public LimiteResultadosException(String mensagem, Exception e) {
    	super(mensagem, e);
    	setNotificavel(false);
    }

    public LimiteResultadosException(int codErro, String msg) {
        super(codErro, msg);
        setNotificavel(false);
    }
}

