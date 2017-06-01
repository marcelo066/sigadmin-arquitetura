/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/11/2007
 */
package br.ufrn.arq.seguranca.autenticacao;

import static br.ufrn.arq.util.ValidatorUtil.validateRequired;

import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import br.ufrn.arq.dao.EmissaoDocumentoAutenticadoDao;
import br.ufrn.arq.negocio.validacao.ListaMensagens;
import br.ufrn.arq.web.jsf.AbstractController;

/**
 * Managed bean para realizar a valida��o de documentos de maneira gen�rica.
 * 
 * @author Gleydson Lima
 * 
 */
public class ValidacaoMBean extends AbstractController {

	/** Define o caminho comum dentro dos contextos do SIGAA e SIGRH para visualiza��o da autenticidade do documento. */
	public static final String AUTENTICO = "/public/autenticidade/autentico.jsp";
	/** Define o caminho comum dentro dos contextos do SIGAA e SIGRH para sele��o do tipo de documento para autentica��o. */
	public static final String SELECIONA_TIPO_DOCUMENTO = "/public/autenticidade/tipo_documento.jsp";
	/** Define o caminho comum dentro dos contextos do SIGAA e SIGRH para valida��o do documento. */
	public static final String INFORMACOES_DOCUMENTO = "/public/autenticidade/form.jsp";

	/** Define a emiss�o do documento j� autenticado. */
	private EmissaoDocumentoAutenticado emissao = new EmissaoDocumentoAutenticado();
	
	/** Indica o texto que deve ser digitado para verfica��o.  */
	private String captcha;

	/** Indica o bundle. */
	private static ResourceBundle bundle;  

	/**
	 * M�todo n�o invocado por JSP's
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	public AutValidator getValidador() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {

		if (bundle == null) {
			bundle = ResourceBundle
					.getBundle("br.ufrn.arq.seguranca.autenticacao.validadores");
		}

		// Tenta pegar pelo subtipo
		String classValidator = null;
		try{
			classValidator = bundle.getString(emissao.getSubTipoDocumento()
					+ "");
		} catch (java.util.MissingResourceException e) {
			// Se n�o existir, procura pelo tipo
			classValidator = bundle.getString(emissao.getTipoDocumento() + "");
		}
		
		AutValidator aut = (AutValidator) Class.forName(classValidator)
				.newInstance();
		return aut;
	}

	/**
	 * Redireciona o usu�rio para p�gina de capta��o das informa��es do
	 * documento selecionado
	 * JSP: /sigaa.war/public/autenticidade/tipo_documento.jsp
	 * @return
	 */
	public String selecionarDocumento() {

		emissao = new EmissaoDocumentoAutenticado();
		emissao.setTipoDocumento(Integer
				.parseInt(getParameter("tipoDocumento")));
		emissao.setSubTipoDocumento(Integer
				.parseInt(getParameter("subTipoDocumento")));

		return forward(INFORMACOES_DOCUMENTO);
	}
	
	/**
	 * Define o tipo e subtipo para o certificado de participante do CIC
	 * e redireciona o usu�rio para p�gina de capta��o das informa��es do
	 * documento selecionado.
	 * JSP: /sigaa.war/public/autenticidade/tipo_documento.jsp
	 * @return
	 */
	public String selecionarDocumentoParticipanteCIC() {
		
		emissao = new EmissaoDocumentoAutenticado();
		emissao.setTipoDocumento(TipoDocumentoAutenticado.CERTIFICADO);
		emissao.setSubTipoDocumento(SubTipoDocumentoAutenticado.CERTIFICADO_PARTICIPANTE_CIC);
		
		return forward(INFORMACOES_DOCUMENTO);
	}
	
	/**
	 * Define o tipo e subtipo para o certificado de avaliador do CIC
	 * e redireciona o usu�rio para p�gina de capta��o das informa��es do
	 * documento selecionado.
	 * JSP: /sigaa.war/public/autenticidade/tipo_documento.jsp
	 * @return
	 */
	public String selecionarDocumentoAvaliadorCIC() {
		
		emissao = new EmissaoDocumentoAutenticado();
		emissao.setTipoDocumento(TipoDocumentoAutenticado.CERTIFICADO);
		emissao.setSubTipoDocumento(SubTipoDocumentoAutenticado.CERTIFICADO_PARTICIPANTE_AVALIADOR_CIC);
		
		return forward(INFORMACOES_DOCUMENTO);
	}

	/**
	 * Valida dados de entrada necess�rias para valida��o e emiss�o de documentos.
	 */
	private ListaMensagens validarDadosEmissao() {

		ListaMensagens lista = new ListaMensagens();

		if ((emissao.getTipoDocumento() == TipoDocumentoAutenticado.CERTIFICADO)
				|| (emissao.getTipoDocumento() == TipoDocumentoAutenticado.DECLARACAO_COM_NUMDOCUMENTO)) {
			validateRequired(emissao.getNumeroDocumento(), "N�mero do Documento", lista);
		}else{
			validateRequired(emissao.getIdentificador(), "Identificador", lista);
		}
		validateRequired(emissao.getDataEmissao() , "Data de Emiss�o", lista);
		validateRequired(emissao.getCodigoSeguranca(), "C�digo de Verifica��o", lista);
		if(!validaCaptcha(captcha))
			lista.addErro("Imagem: Valor inv�lido.");
							
		return lista;
	}
		
	/**
	 * Efetua as valida��es necess�rias e redireciona de acordo com o contexto.
	 * Verifica se o documento � v�lido
	 * <br />
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * <ul>
	 * 	<li>/sigaa.war/public/autenticidade/form.jsp</li>
	 *  <li>/sigrh.war/public/autenticidade/form.jsp</li>
	 * </ul> 
	 * @return
	 * @throws Exception
	 */
	public String validar() throws Exception {
		
		erros = validarDadosEmissao();
		
		if (hasErrors()) {
			return null;
		}
		
		EmissaoDocumentoAutenticadoDao dao = new EmissaoDocumentoAutenticadoDao();

		EmissaoDocumentoAutenticado emissaoObj = null;

		if ((emissao.getTipoDocumento() == TipoDocumentoAutenticado.CERTIFICADO)
				|| (emissao.getTipoDocumento() == TipoDocumentoAutenticado.DECLARACAO_COM_NUMDOCUMENTO)) {

			emissaoObj = dao.findByEmissao(emissao.getNumeroDocumento(),
					emissao.getCodigoSeguranca(), new java.sql.Date(emissao
							.getDataEmissao().getTime()), emissao
							.getTipoDocumento(), emissao
							.getSubTipoDocumento());

		} else {
			emissaoObj = dao.findByEmissao(emissao.getIdentificador(),
					emissao.getCodigoSeguranca(), new java.sql.Date(emissao
							.getDataEmissao().getTime()), emissao
							.getTipoDocumento());
		}

		if (emissaoObj == null) {
				addMensagemErro("Esta emiss�o n�o foi encontrada no sistema e portanto n�o foi validada.");
				return null;
		} else {
			AutValidator aut = getValidador();
			boolean aindaValido = aut.validaDigest(emissaoObj); // verifica
			// se o documento ainda esta va'lido
			if (aindaValido) {
				emissao = emissaoObj;
				return forward(AUTENTICO);
			} else {
				addMensagemErro("Documento Vencido. Foi detectado uma altera��o em seu estado desde sua emiss�o at� a data de hoje.");
				return null;
			}
		}
	} 

	/**
	 * Visualiza o documento validado
	 * <br />
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * <ul>
	 * 	<li>/sigaa.war/public/autenticidade/autentico.jsp</li>
	 *  <li>/sigrh.war/public/autenticidade/autentico.jsp</li>
	 * </ul>
	 * @return
	 * @throws Exception
	 */
	public String visualizarEmissao() throws Exception {

		AutValidator aut = getValidador();
		aut.exibir(emissao, getCurrentRequest(), getCurrentResponse());
		FacesContext.getCurrentInstance().responseComplete();

		return null;
	}

	/**
	 * <br />
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * <ul>
	 * 	<li>/sigaa.war/public/autenticidade/form.jsp</li>
	 *  <li>/sigrh.war/public/autenticidade/form.jsp</li>
	 * </ul>
	 * @return
	 */
	public EmissaoDocumentoAutenticado getEmissao() {
		return emissao;
	}

	/**
	 * <br />
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * <ul>
	 * 	<li>/sigaa.war/public/autenticidade/form.jsp</li>
	 *  <li>/sigrh.war/public/autenticidade/form.jsp</li>
	 * </ul>
	 * @param emissao
	 */
	public void setEmissao(EmissaoDocumentoAutenticado emissao) {
		this.emissao = emissao;
	}

	/**
	 * M�todo n�o invocado por JSP's.
	 * @return
	 */
	public String getCaptcha() {
		return captcha;
	}

	/**
	 * <br />
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * <ul>
	 * 	<li>/sigaa.war/public/autenticidade/form.jsp</li>
	 *  <li>/sigrh.war/public/autenticidade/form.jsp</li>
	 * </ul>
	 * @param captcha
	 */
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	/**
	 * Informa, na p�gina de autentica��o qual o tipo de documento selecionado
	 * na p�gina de sele��o de documentos
	 * M�todo chamado pela(s) seguinte(s) JSP(s):
	 * @return
	 */
	public String getDocumentoSelecionado() {
		String result = "";

		switch (emissao.getTipoDocumento()) {
		case TipoDocumentoAutenticado.ATESTADO:
			result = "ATESTADO";
			break;

		default:
			break;
		}
		return result;
	}
}
