/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 02/09/2004
 */
package br.ufrn.arq.erros;

import java.util.Collection;

import br.ufrn.arq.negocio.validacao.ListaMensagens;
import br.ufrn.arq.negocio.validacao.MensagemAviso;
import br.ufrn.arq.negocio.validacao.TipoMensagemUFRN;

/**
 * Esta exce��o � emitida sempre que o erro em quest�o for um erro de regra de
 * neg�cio.
 *
 * @author Gleydson Lima
 */
public class NegocioException extends UFRNException {

	/**
	 * Esta cole��o serve para armazenar um conjunto de objetos que se dejeja
	 * que o cliente conhe�a.
	 */
	private Collection<?> objetos;

	private ListaMensagens listaMensagens = new ListaMensagens();

	/** Caso queira-se passar apenas um objeto ou em conjunto com a cole��o acima */
	private Object objeto;

	public NegocioException(Exception e) {
		super(e);
		setNotificavel(false);
	}

	public NegocioException(String msg) {
		super(msg);
		addErro(msg);
		setNotificavel(false);
	}

	public NegocioException() {
		super("");
		setNotificavel(false);
	}

	public NegocioException(int codErro, String msg) {
		super(codErro, msg);
		setNotificavel(false);
	}

	public NegocioException(int codErro, Exception e) {
		super(e);
		setCodErro(codErro);
		setNotificavel(false);
	}
	
	public NegocioException(ListaMensagens listaMensagens) {
		super("");
		setListaMensagens(listaMensagens);
	}

	/**
	 * @return Retorna objetos.
	 */
	public Collection<?> getObjetos() {
		return objetos;
	}

	/**
	 * @param objetos
	 *            The objetos to set.
	 */
	public void setObjetos(Collection<?> objetos) {
		this.objetos = objetos;
	}

	/**
	 * @return Returns the objeto.
	 */
	public Object getObjeto() {
		return objeto;
	}

	/**
	 * @param objeto
	 *            The objeto to set.
	 */
	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}

	public void addErro(String erro) {
		listaMensagens.addMensagem(new MensagemAviso(erro,
				TipoMensagemUFRN.ERROR));
	}

	public void addMensagens(Collection<MensagemAviso> mensagens) {
		listaMensagens.addAll(mensagens);
	}

	public ListaMensagens getListaMensagens() {
		return listaMensagens;
	}

	public void setListaMensagens(ListaMensagens listaMensagens) {
		this.listaMensagens = listaMensagens;
	}


	@Override
	public String getMessage() {
		if ( listaMensagens != null && listaMensagens.getMensagens().size() > 0 ) {
			String result = "";
			for ( MensagemAviso msg : listaMensagens.getMensagens() ) {
				result += msg.getMensagem() + "\n";
			}
			return result;
		} else {
			return super.getMessage();
		}
	}
}