/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 25/10/2004
 */
package br.ufrn.arq.erros;


/**
 * Exce��o disparada para falhas de seguran�a
 *
 * @author Gleydson Lima
 *
 */
public class SegurancaException extends ArqException {

	private int[] papeis;
	
	public SegurancaException(String msg) {
        super(msg);
        setNotificavel(false);
    }
	
    public SegurancaException(String msg, int[] papeis) {
        super(msg);
        setNotificavel(false);
        this.papeis = papeis;
    }

    public SegurancaException() {
        super("Usu�rio n�o autorizado a realizar esta opera��o");
        setNotificavel(false);
    }

    public SegurancaException(Exception e) {
        super(e);
        setNotificavel(false);
    }

	public int[] getPapeis() {
		return papeis;
	}

}
