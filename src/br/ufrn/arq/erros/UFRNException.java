/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 02/09/2004
 */
package br.ufrn.arq.erros;

/**
 * Exce��o Geral do SIPAC. A exce��o possui
 * um c�digo que pode encapsular um tipo mais
 * especifico de erro mapeado pela mesma exce��o.
 *
 * @author Gleydson Lima
 */
public class UFRNException extends Exception {

    private int codErro;

    // informa se o erro vai ser enviado por e-mail
    private boolean notificavel = true;

    public UFRNException(String msg) {
        super(msg);
    }

    public UFRNException(int codErro, String msg) {
        super(msg);
        this.codErro = codErro;
    }

    public UFRNException(Exception e) {
        super(e);
    }

    public UFRNException(String msg, Exception e) {
    	super(msg, e);
    }

    /**
     * @return Retorna codErro.
     */
    public int getCodErro() {
        return codErro;
    }

    /**
     * @param codErro
     *            The codErro to set.
     */
    public void setCodErro(int codErro) {
        this.codErro = codErro;
    }

    @Override
    public String toString() {
    	return super.toString()
				+ ((codErro < 0) ? "\nCod. Erro: " + codErro : "");
    }

	public boolean isNotificavel() {
		return notificavel;
	}

	public void setNotificavel(boolean notificavel) {
		this.notificavel = notificavel;
	}


}