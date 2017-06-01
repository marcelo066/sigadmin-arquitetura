/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 11/08/2007
 */
package br.ufrn.arq.web.jsf;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.PropertyNotWritableException;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.ufrn.arq.dao.DAOFactory;
import br.ufrn.arq.dao.GenericDAO;
import br.ufrn.arq.dao.GenericDAOImpl;
import br.ufrn.arq.dao.GenericSharedDBDao;
import br.ufrn.arq.dao.PagingInformation;
import br.ufrn.arq.dominio.Comando;
import br.ufrn.arq.dominio.Movimento;
import br.ufrn.arq.dominio.RegistroAcessoPublico;
import br.ufrn.arq.dominio.RegistroEntrada;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.DAOException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.erros.SegurancaException;
import br.ufrn.arq.negocio.ArqListaComando;
import br.ufrn.arq.negocio.FacadeDelegate;
import br.ufrn.arq.negocio.validacao.ListaMensagens;
import br.ufrn.arq.negocio.validacao.MensagemAviso;
import br.ufrn.arq.negocio.validacao.TipoMensagemUFRN;
import br.ufrn.arq.seguranca.dominio.PassaporteLogon;
import br.ufrn.arq.seguranca.log.SessionLogger;
import br.ufrn.arq.tasks.TarefaAssincrona;
import br.ufrn.arq.util.CalendarUtils;
import br.ufrn.arq.util.ErrorUtils;
import br.ufrn.arq.util.ReflectionUtils;
import br.ufrn.arq.util.RequestUtils;
import br.ufrn.arq.util.UFRNUtils;
import br.ufrn.arq.web.captcha.Captcha;
import br.ufrn.arq.web.tags.SelectAnoTag;
import br.ufrn.comum.dominio.Sistema;
import br.ufrn.comum.dominio.SubSistema;
import br.ufrn.comum.dominio.UnidadeGeral;
import br.ufrn.comum.dominio.UsuarioGeral;

import com.octo.captcha.service.CaptchaServiceException;

/**
 * Classe que implementa o padr�o Layer Supertype
 * (http://martinfowler.com/eaaCatalog/layerSupertype.html) e que
 * possui o comportamento que � comum a todos os controladores
 * dos sistemas institucionais.deta
 *
 * A implementa��o do Serializable � para utilizar replica��o
 * ou t�cnicas de manuten��o de requests como o a4j:keepAlive.
 *
 * @author Gleydson Lima
 * @author David Pereira
 * 
 */
public class AbstractController implements Serializable {

	
	/** Chave para atributo que cont�m o �ltimo comando realizado. */
	private static final String LAST_COMMAND = "lastCommand";

	/** Cole��o de SelectItems padr�o para campos de ano. */
	private static List<SelectItem> anos = new ArrayList<SelectItem>();

	/** Cole��o de SelectItems padr�o com os meses do ano. */
	private static List<SelectItem> meses = new ArrayList<SelectItem>();
	
	private static List <SelectItem> horas = new ArrayList <SelectItem> ();
	
	private static List <SelectItem> minutos = new ArrayList <SelectItem> ();

	/** Cole��o de SelectItems padr�o com os valores sim (true) e n�o (false). */
	private static List<SelectItem> simNao = new ArrayList<SelectItem>();

	/** Cole��o de SelectItems padr�o com os valores Masculino (M) e Feminino (F). */
	private static List<SelectItem> mascFem = new ArrayList<SelectItem>();

	/** Tamanho de uma p�gina em uma listagem de dados. */
	private int tamanhoPagina = 0;

	/** N�mero total de p�ginas em uma listagem de dados. */
	private int totalPaginas;
	
	/** Cole��o de erros para serem exibidos ao usu�rio. */
	protected ListaMensagens erros = new ListaMensagens();

	static {

		meses.add(new SelectItem("1", "Janeiro"));
		meses.add(new SelectItem("2", "Fevereiro"));
		meses.add(new SelectItem("3", "Mar�o"));
		meses.add(new SelectItem("4", "Abril"));
		meses.add(new SelectItem("5", "Maio"));
		meses.add(new SelectItem("6", "Junho"));
		meses.add(new SelectItem("7", "Julho"));
		meses.add(new SelectItem("8", "Agosto"));
		meses.add(new SelectItem("9", "Setembro"));
		meses.add(new SelectItem("10", "Outubro"));
		meses.add(new SelectItem("11", "Novembro"));
		meses.add(new SelectItem("12", "Dezembro"));

		simNao.add(new SelectItem(String.valueOf(true), "Sim"));
		simNao.add(new SelectItem(String.valueOf(false), "N�o"));

		mascFem.add(new SelectItem("F", "Feminino"));
		mascFem.add(new SelectItem("M", "Masculino"));

		for (int i = SelectAnoTag.ANO_INCIO_PADRAO; i <= CalendarUtils.getAnoAtual() + 4; i++) {
			anos.add(new SelectItem(String.valueOf(i), String.valueOf(i)));
		}
		
		for (int i = 0; i < 24; i++)
			horas.add(new SelectItem(""+i, ""+i));
		
		for (int i = 0; i < 60; i++)
			minutos.add(new SelectItem(""+i, ""+i));
	}

	public String getMes(int mes) {
		return meses.get(mes).getLabel();
	}

	public String getPadraoData() {
		return "dd/MM/yyyy";
	}

	public String getPadraoNumero() {
		return "#,##0.00";
	}
	
	public List<SelectItem> getMeses() {
		return meses;
	}
	
	public List<SelectItem> getSimNao() {
		return simNao;
	}

	public List<SelectItem> getMascFem() {
		return mascFem;
	}
	
	public List<SelectItem> getAnos() throws Exception {
		return anos;
	}
	
	public List<SelectItem> getHoras() {
		return horas;
	}

	public List<SelectItem> getMinutos() {
		return minutos;
	}

	/**
	 * Retorna ano atual
	 * @deprecated Usar CalendarUtils.getAnoAtual()
	 * @return
	 */
	@Deprecated
	public int getAnoAtual() {
		return CalendarUtils.getAnoAtual();
	}

	/**
	 * retorna o per�odo atual
	 * caso seja um m�s superior a junho (6) retorna 2
	 * sen�o retorna 1
	 * @return
	 */
	public int getPeriodoAtual() {
		return (getMesAtual() + 1) < 7 ? 1 : 2;
	}

	/**
	 * Retorna m�s atual
	 * @deprecated Usar CalendarUtils.getMesAtual()
	 * @return
	 */
	@Deprecated
	public int getMesAtual() {
		return CalendarUtils.getMesAtual();
	}

	/**
	 * Retorna m�s atual como string ex: janeiro, fevereiro, etc
	 *
	 * @return
	 */
	public String getMesAtualString() {
		return getMes(CalendarUtils.getMesAtual());
	}
	

	/**
	 * Retorna o Delegate do Usu�rio e cria caso ainda n�o possua
	 *
	 * @param req
	 * @return
	 */
	protected FacadeDelegate getUserDelegate(HttpServletRequest req) throws ArqException {
		HttpSession session = req.getSession(true);
		if (session == null) {
			throw new ArqException("Sess�o n�o ativada");
		} else {
			FacadeDelegate facade = (FacadeDelegate) session.getAttribute("userFacade");
			
			if (facade == null) {
				String jndiName = (String) req.getSession().getServletContext().getAttribute("jndiName");
				facade = new FacadeDelegate(jndiName);
				session.setAttribute("userFacade", facade);
			}
			
			return facade;
		}
	}

	/**
	 * Chama o processador baseado no movimento passado
	 *
	 * @param mov
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 *
	 */
	@SuppressWarnings("unchecked")
	public <V> V execute(Movimento mov, HttpServletRequest req) throws NegocioException, ArqException {
		forceCloseSession();
		mov.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext()));
		return (V) getUserDelegate(req).execute(mov, req);
	}

	/**
	 * Chama o processador baseado no movimento passado
	 *
	 * @param mov
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 * @throws RemoteException
	 */
	public <V> V execute(Movimento mov) throws NegocioException, ArqException {
		return execute(mov, getCurrentRequest());
	}
	
	/**
	 * Chama o processador utilizando o facade passado como par�metro.
	 * @param mov
	 * @param facade
	 * @param usuario
	 * @param sistema
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 */
	@SuppressWarnings("unchecked")
	public <V> V execute(Movimento mov, FacadeDelegate facade, UsuarioGeral usuario, Integer sistema) throws NegocioException, ArqException  {
		forceCloseSession();
		return (V) facade.execute(mov, usuario, sistema);
	}

	/**
	 * Chama o processador sem fechar a conex�o. 
	 * Deprecated: utilizar o execute.
	 * 
	 * @param mov
	 * @param req
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 */
	@SuppressWarnings("unchecked")
	public <V> V executeWithoutClosingSession(Movimento mov, HttpServletRequest req) throws NegocioException, ArqException {
		mov.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext()));
		return (V) getUserDelegate(req).execute(mov, req);
	}

	/**
	 * Chama o processador sem fechar a conex�o. 
	 * Deprecated: utilizar o execute.
	 * 
	 * @param mov
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 */
	public <V> V executeWithoutClosingSession(Movimento mov) throws NegocioException, ArqException {
		return executeWithoutClosingSession(mov, getCurrentRequest());
	}

	/**
	 * Chama o processador sem fechar a conex�o. 
	 * Deprecated: utilizar o execute.
	 * 
	 * @param mov
	 * @param facade
	 * @param usuario
	 * @param sistema
	 * @return
	 * @throws NegocioException
	 * @throws ArqException
	 */
	public Object executeWithoutClosingSession(Movimento mov, FacadeDelegate facade, UsuarioGeral usuario, Integer sistema) throws NegocioException, ArqException  {
		return facade.execute(mov, usuario, sistema);
	}

	public final void forceCloseSession() {
		Session session = getSessionRequest();
		if (session != null) {
			try {
				session.close();
				SessionLogger.log(session, 'C');
			} catch (Exception e) {

			}
		}
	}

	// m�todo usado pelas JSTL e contextos n�o Faces para criar Managed Beans
	public String getCreate() {
		return "";
	}

	/**
	 * Desbloqueia um comando para que ele possa ser executado.
	 * @param comando
	 * @throws ArqException
	 */
	public void prepareMovimento(Comando comando) throws ArqException {
		prepareMovimento(comando, getCurrentRequest());
	}

	/**
	 * Desbloqueia um comando para que ele possa ser executado.
	 * @param comando
	 * @param req
	 * @throws ArqException
	 */
	public void prepareMovimento(Comando comando, HttpServletRequest req) throws ArqException {
		try {
			req.getSession().setAttribute(LAST_COMMAND, comando);
			getUserDelegate(req).prepare(comando.getId(), req);
		} catch (NegocioException e) {
			// essa exce��o n�o vem no prepare
		}
	}

	/**
	 * Desbloqueia um comando para que ele possa ser executado.
	 * @param comando
	 * @param facade
	 * @param usuario
	 * @param sistema
	 * @throws ArqException
	 */
	public void prepareMovimento(Comando comando, FacadeDelegate facade, UsuarioGeral usuario, Integer sistema) throws ArqException {
		try {
			facade.prepare(comando.getId(), usuario, sistema);
		} catch (NegocioException e) {
			// essa exce��o n�o vem no prepare
		}
	}

	public void inicializarTarefaAssincrona(TarefaAssincrona<?> tarefa, HttpServletRequest req) throws ArqException {
		tarefa.initialize(getUserDelegate(req), (UsuarioGeral) req.getSession(true).getAttribute("usuario"), (Integer) req.getAttribute("sistema"));
	}

	/**
	 * Retorna o subsistema que est� sendo utilizado pelo usu�rio.
	 */
	public SubSistema getSubSistema() {
		SubSistema subsistema = null;

		if (getCurrentSession().getAttribute("subsistema") instanceof String) {
			GenericSharedDBDao genDAO = null;
			String idSubsistema = (String) getCurrentSession().getAttribute("subsistema");

			try {
				genDAO = new GenericSharedDBDao();
				subsistema = genDAO.findByPrimaryKey(Integer.parseInt(idSubsistema), SubSistema.class);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if (genDAO != null) genDAO.close();
			}

		} else {
			subsistema = (SubSistema) getCurrentSession().getAttribute("subsistema");
		}
		
		return subsistema;
	}


	

	/**
	 * Informa o diret�rio base de redirecionamento
	 *
	 * @param dirBase
	 */
	public void setDirBase(String dirBase) {
		getCurrentSession().setAttribute("dirBase", dirBase);
	}

	

	/**
	 * Seta o subsistema atual na sess�o
	 */
	public void setSubSistemaAtual(SubSistema sub) {
		getCurrentSession().removeAttribute("subsistema");
		getCurrentSession().setAttribute("subsistema", sub);
	}
	

	/**
	 * Retorna o sistema atual (SIPAC, SIGAA, SIGRH, etc).
	 * @see Sistema
	 */
	public final int getSistema() {
		return (Integer) getCurrentRequest().getAttribute("sistema");
	}

	/**
	 * Adiciona uma linha no cabe�alho de um relat�rio
	 */
	@SuppressWarnings("unchecked")
	public void addComplementoPrint(String mensagem, HttpServletRequest req) {

		ArrayList<String> mensagens = null;
		if (req.getAttribute("complemento") == null) {
			mensagens = new ArrayList<String>();
			req.setAttribute("complemento", mensagens);
		} else {
			mensagens = (ArrayList<String>) req.getAttribute("complemento");
		}
		mensagens.add(mensagem);
	}


	/**
	 * Verifica se o conte�do digitado pelo usu�rio � igual a imagem de um Captcha
	 */
	public boolean validaCaptcha(String captcha) {
		try {
			return Captcha.getInstance().getService().validateResponseForID(getCurrentSession().getId(), captcha);
		} catch(CaptchaServiceException e) {
			e.printStackTrace();
			// Esta exce��o ocorre quando o usu�rio perde a sess�o. Assim, o id da sess�o n�o � v�lido. Tratado para, nesses casos, retornar false.  
			return false;
		}
	}



	/**
	 * Define uma opera��o ativa durante a execu��o do caso de uso.
	 *
	 * @param operacao -
	 *            Opera�ao executada no momento, pode ser definida partir de uma
	 *            classe de constantes, por exemplo:
	 *            br.ufrn.sigrh.avaliacao.dominio.OperacoesAvaliacao.
	 * @param mensagemOpAtiva -
	 *            Mensagem que ser� exibida ao usu�rio caso n�o a opera��o tenha
	 *            sido finalizada.
	 * @param session
	 */
	public void setOperacaoAtiva(Integer operacao, String mensagemOpAtiva) {

		getCurrentSession().setAttribute("operacaoAtiva", operacao);
		getCurrentSession().setAttribute("mensagemOperacaoAtiva", mensagemOpAtiva);
	}

	public void setOperacaoAtiva(Integer operacao) {
		setOperacaoAtiva(operacao, "A opera��o j� havia sido conclu�da. Por favor, reinicie os procedimentos.");
	}

	/**
	 * M�todo para remo��o da opera��o ativa da sess�o para evitar erros de
	 * nullpointer devido aos usu�rios utilizarem o VOLTAR do browser
	 */
	public void removeOperacaoAtiva() {
		getCurrentSession().removeAttribute("operacaoAtiva");
	}


	/** 
	 * Verifica se h� ou n�o uma opera��o ativa, dentro da lista de opera��es passadas como par�metro. O
	 * redirect deve ser dado ap�s a chamado e se o resultado por false.
	 * @param operacoes
	 * @return false, caso n�o tenha opera��o ativa setada, ou caso a opera��o ativa n�o seja nenhuma das passadas como par�metro.
	 */
	public boolean checkOperacaoAtiva(Integer ...operacoes) {
		
		boolean ativa = isOperacaoAtiva(operacoes);
		
		if (!ativa) {
			String msgSessao = (String) getCurrentSession().getAttribute("mensagemOperacaoAtiva");
			if( msgSessao != null ) {
				addMensagemErro(msgSessao);
				getCurrentSession().removeAttribute("mensagemOperacaoAtiva");
			} else {
				addMensagemErro("A opera��o j� havia sido conclu�da. Por favor, reinicie os procedimentos.");
			}
			return false;
		}
		
		return true;
	}

	/**
	 * Identifica se um subsistema passado como par�metro est� ativo ou n�o.
	 * @param subsistema
	 * @return
	 * @throws DAOException
	 */
	public boolean isSubSistemaAtivo(int subsistema) throws DAOException {
		GenericDAO dao = new GenericSharedDBDao(Sistema.COMUM);
		try {
			SubSistema ss = dao.findByPrimaryKey(subsistema, SubSistema.class);
			return ss.isAtivo();
		} finally {
			dao.close();
		}
	}
	
	/**
	 * Apenas verifica se as opera��es passadas como par�metro s�o ativas. <br>
	 * <b>N�o insere nenhuma mensagem de erro na lista de erros!</b>
	 * @return false, caso n�o tenha opera��o ativa setada, ou caso a opera��o ativa n�o seja nenhuma das passadas como par�metro.
	 * @param operacoes
	 * @return
	 */
	public boolean isOperacaoAtiva(Integer ...operacoes) {
		boolean ativa = false;

		Integer operacaoAtiva = (Integer) getCurrentSession().getAttribute("operacaoAtiva");
		if (operacaoAtiva != null) {
			for (Integer operacao : operacoes) {
				if (operacaoAtiva.equals(operacao)) {
					ativa = true;
					break;
				}
			}
		}
		
		return ativa;
	}
	
	public Comando getUltimoComando() {
		return (Comando) getCurrentSession().getAttribute(LAST_COMMAND);
	}

	/**
	 * Transforma um mapa em uma cole��o de SelectItem. A key de uma Entry
	 * ser� utilizado como value do SelectItem e o value de uma Entry 
	 * como label.
	 */
	public static List<SelectItem> toSelectItems(Map<?, ?> map) {
		List<SelectItem> itens = new ArrayList<SelectItem>();

		for (Object name : map.entrySet()) {
			Entry<?, ?> entry = (Entry<?, ?>) name;
			itens.add(new SelectItem(entry.getKey().toString(), entry.getValue().toString()));
		}
		return itens;
	}

	 /**
	  * Transforma uma cole��o de objetos em uma lista de SelectItems
	  * @param col Cole��o a ser transformada em SelectItems
	  * @param value Atributo que ser� utilizado no value do option
	  * @param showText Valor que ser� exibido ao usu�rio
	  */
	public static List<SelectItem> toSelectItems(Collection<?> col, String value, String showText) {

		ArrayList<OrderedSelectItem> itensOrdenaveis = new ArrayList<OrderedSelectItem>();
		ArrayList<SelectItem> itens = new ArrayList<SelectItem>();

		try {
			if (col != null) {
				for (Object obj : col) {
					Object id = ReflectionUtils.evalProperty(obj, value);
					Object text = ReflectionUtils.evalProperty(obj, showText);
					if (text == null) {
						text = "";
					}
					OrderedSelectItem item = new OrderedSelectItem(id.toString(), text
							.toString());
					itensOrdenaveis.add(item);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(itensOrdenaveis);
		for ( OrderedSelectItem item : itensOrdenaveis ) {
			itens.add(item.toSelectItem());
		}

		return itens;

	}

	/**
	  * Transforma uma cole��o de objetos em uma lista de SelectItems utilizando
	  * o pr�prio objeto como valor do SelectItem.
	  * @param col Cole��o a ser transformada em SelectItems
	  * @param showText Valor que ser� exibido ao usu�rio
	  */
	public static List<SelectItem> toSelectItems(Collection<?> col, String showText) {
		List<SelectItem> items = new ArrayList<SelectItem>();
		
		try {
			if (col != null) {
				for (Object obj : col) {
					items.add(new SelectItem(obj, BeanUtils.getProperty(obj, showText)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return items;
	}
	
	
	/**
	 * M�todo para entrar no sistema comum
	 *
	 * @param url
	 * @param sistemaOrigem
	 * @param usuario
	 * @throws ArqException
	 * @throws NegocioException
	 * @throws IOException
	 */
	public void logarSistema(String url, int sistemaOrigem, int sistemaDestino,
			UsuarioGeral usuario, HttpServletRequest req,
			HttpServletResponse res) throws NegocioException, ArqException,
			IOException {

		PassaporteLogon passaporte = new PassaporteLogon();
		passaporte.setHora(new Date());
		passaporte.setSistemaAlvo(sistemaDestino);
		passaporte.setSistemaOrigem(sistemaOrigem);
		req.setAttribute("sistema", sistemaOrigem);

		passaporte.setLogin(usuario.getLogin());
		prepareMovimento(ArqListaComando.CADASTRAR_PASSAPORTE, req);
		passaporte.setCodMovimento(ArqListaComando.CADASTRAR_PASSAPORTE);
		execute(passaporte, req);

		res.sendRedirect(url);
	}

	/**
	 * ----------------------------------------------------
	 * - MANIPULA��O DE MANAGED BEANS
	 * ----------------------------------------------------
	 */
	
	/**
	 * Remove do container o managed bean que chamar o m�todo.
	 */
	public void resetBean() {
		/** removendo opera��o ativa */
		getCurrentSession().removeAttribute("operacaoAtiva");
		
		Component anotComponent = getClass().getAnnotation(Component.class);
		if (anotComponent != null) {
			String mbeanName = anotComponent.value();
			if (isEmpty(mbeanName)) mbeanName = StringUtils.uncapitalize(getClass().getSimpleName());
			resetBean(mbeanName);
		}
	}


	/**
	 * Remove do container um managed bean cujo nome for passado como par�metro.
	 */
	public static void resetBean(String name) {
		if (!isEmpty(name)) {
			Object mbean = getMBean(name);
			if(mbean != null) {
				try {
					FacesContext fc = FacesContext.getCurrentInstance();
					fc.getApplication().getExpressionFactory().createValueExpression(fc.getELContext(), "#{" + name + "}", getMBean(name).getClass()).setValue(fc.getELContext(), null);
				} catch(PropertyNotWritableException e) {
					Object clean = ReflectionUtils.instantiateClass(mbean.getClass());
					ReflectionUtils.copyProperties(clean, mbean);
				}
			}
		}
	}
	
	/**
	 * Retorna um managed-bean existente no container do JavaServer Faces.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getMBean(String mbeanName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		return (T) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, mbeanName);
	}

	/**
	 * Cria um Managed Bean no container do JavaServer Faces com o nome e objeto passados
	 * como par�metro. 
	 * @deprecated
	 */
	@Deprecated
	public void createMBean(String mBeanName, Object mbeanObject) {
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getApplication().createValueBinding("#{" + mBeanName + "}").setValue(fc, mbeanObject);
	}

	/**
	 * Remove um managed bean do container.
	 */
	@Deprecated
	public static void destroyMBean(String mBeanName) {
		resetBean(mBeanName);
	}
	
	/**
	 * limpa um managed bean do container.
	 */
	public void clearMBean(String mBeanName, Object mbeanObject) {
		resetBean(mBeanName);
		createMBean(mBeanName, mbeanObject);
	}
	
	
	/**
	 * ----------------------------------------------------
	 * - M�TODOS RELACIONADOS A DAOS
	 * ----------------------------------------------------
	 */
	public GenericDAO getGenericDAO() {
		return DAOFactory.getInstance().getDAO(GenericDAOImpl.class, getCurrentRequest());
	}
	
	/**
	 * Retorna uma inst�ncia de um DAO com a sess�o que est� ativa
	 * em request (OpenSessionInView).
	 */
	public <U extends GenericDAO> U getDAO(Class<U> daoClass, Integer sistema) {
		return DAOFactory.getInstance().getDAO(daoClass, sistema == null ? -1 : sistema, getCurrentRequest());
	}

	/**
	 * Retorna uma inst�ncia de um DAO com a sess�o que est� ativa
	 * em request (OpenSessionInView), setando nele o sistema passado como par�metro.
	 */
	public <U extends GenericDAO> U getDAO(Class<U> daoClass) {
		return getDAO(daoClass, null);
	}
	
	/**
	 * Retorna a sessao do hibernate existente em request. Implementa��o
	 * do padr�o OpenSessionInView. 
	 * @param sistema 
	 */
	public Session getSessionRequest(Integer sistema) {
		return DAOFactory.getInstance().getSessionRequest(sistema, getCurrentRequest());
	}
	
	/**
	 * Retorna a sessao do hibernate existente em request. Implementa��o
	 * do padr�o OpenSessionInView. 
	 * @param sistema 
	 */
	public Session getSessionRequest() {
		return DAOFactory.getInstance().getSessionRequest(getSistema(), getCurrentRequest());
	}
	
	/**
	 * Retorna uma lista de SelectItem com todos os objetos da classe passada.
	 */
	public Collection<SelectItem> getAll(Class<?> classe, String value, String text) {
		GenericDAO dao = null;
		try {
			dao = new GenericDAOImpl(getSistema());
			return toSelectItems(dao.findAll(classe, text, "asc"), value, text);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<SelectItem>();
		} finally {
			if (dao != null)
				dao.close();
		}
	}

	/**
	 * Retorna uma lista de SelectItem com todos os objetos da classe passada que
	 * possuirem o atributo ativo = true. 
	 */
	public Collection<SelectItem> getAllAtivo(Class<?> classe, String value, String text) {
		GenericDAO dao = null;
		try {
			dao = new GenericDAOImpl(getSistema());
			return toSelectItems(dao.findByExactField(classe, "ativo", Boolean.TRUE, null, text, "asc"), value, text);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<SelectItem>();
		} finally {
			if (dao != null) dao.close();
		}
	}

	/**
	 * Retorna o findAll do GenericDAO
	 */
	public <T> Collection<T> getAllObj(Class<T> classe, int sistema) {
		GenericDAO dao = null;
		try {
			dao = new GenericDAOImpl(sistema);
			return dao.findAll(classe);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		} finally {
			if (dao != null) dao.close();
		}
	}
	
	/**
	 * Retorna a informa��o de pagina��o. A pagina��o � um MBean com
	 * escopo de sess�o.
	 */
	public PagingInformation getPaginacao() {

		PagingInformation paginacao = (PagingInformation) getMBean("paginacao");
		// Tamanho de pagina setado no MBean. Se o programador nao definir, pega
		// o valor padrao
		if (tamanhoPagina > 0) {
			paginacao.setTamanhoPagina(tamanhoPagina);
		}
		
		return paginacao;
	}
	
	public int getTamanhoPagina() {
		return tamanhoPagina;
	}

	public void setTamanhoPagina(int tamanhoPagina) {
		this.tamanhoPagina = tamanhoPagina;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public void setTotalPaginas(int totalPaginas) {
		this.totalPaginas = totalPaginas;
	}
	
	
	/**
	 * ----------------------------------------------------
	 * - M�TODOS PARA EXIBI��O DE MENSAGENS
	 * ----------------------------------------------------
	 */
	
	/**
	 * Coloca os erros em sess�o
	 */
	public void attachErrorInSession() {
		HttpSession session = getCurrentSession();

		if (session.getAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION) == null)
			session.setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		else {
			ListaMensagens errosAnt = (ListaMensagens) session.getAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION);
			if (errosAnt != null) {
				erros.addAll(errosAnt.getMensagens());
				session.setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
			}
		}
	}
	
	/**
	 * Teste se tem erros na lista de mensagens em sess�o 
	 * para determinar valida��o. 
	 */
	public boolean hasErrors() {
		
		attachErrorInSession();
		
		boolean hasErros = erros.isErrorPresent();
		erros = new ListaMensagens();
		return hasErros;
	}

	/**
	 * Limpa a lista de mensagens em sess�o.
	 */
	public void clearMensagens() {
		getCurrentSession().removeAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION);
	}

	/**
	 * Retorna true apenas se a lista de mensagens em sess�o 
	 * tiver mensagens do tipo TipoMensagemUFRN.ERROR
	 */
	public boolean hasOnlyErrors() {

		attachErrorInSession();

		boolean hasErros = false;
		for (MensagemAviso msg : erros.getMensagens()) {
			if (msg.getTipo() == TipoMensagemUFRN.ERROR) {
				hasErros = true;
				break;
			}
		}
		erros = new ListaMensagens();
		return hasErros;
	}
	
	/**
	 * Adiciona uma mensagem ao sistema buscando-a no banco de dados,
	 * de acordo com o c�digo passado como par�metro.
	 * @param codigo
	 */
	public void addMensagem(String codigo, Object... params) {
		MensagemAviso msg = UFRNUtils.getMensagem(codigo, params);
		erros.addMensagem(msg);
		hasErrors();
	}
	
	/**
	 * Adiciona uma mensagem ao sistema buscando-a no banco de dados,
	 * de acordo com o c�digo passado como par�metro e um tipo.
	 * @param codigo
	 */
	public void addMensagem(String codigo, TipoMensagemUFRN tipo, Object... params) {
		MensagemAviso msg = UFRNUtils.getMensagem(codigo, params);
		addMessage(msg.getMensagem(), tipo);
	}

	/**
	 * Adiciona a msg de erro padr�o do sistema para exce��es que nao sejam de 
	 * neg�cio na lista de msg para ser exibido ao usu�rio
	 */
	public void addMensagemErroPadrao() {
		addMensagemErro("Um erro ocorreu durante a execu��o desta opera��o. "
				+ "Por favor, contacte o suporte atrav�s do \"Abrir Chamado\".");
	}

	/**
	 * Adiciona uma mensagem de erro para ser exibida no sistema.
	 */
	public void addMensagemErro(String mensagem) {
		addMessage(mensagem, TipoMensagemUFRN.ERROR);
	}

	/**
	 * Adiciona uma mensagem de aviso para ser exibida no sistema. 
	 */
	public void addMensagemWarning(String mensagem) {
		addMessage(mensagem, TipoMensagemUFRN.WARNING);
	}

	/**
	 * Adiciona uma mensagem de informa��o para ser exibida no sistema.
	 */
	public void addMensagemInformation(String mensagem) {
		addMessage(mensagem, TipoMensagemUFRN.INFORMATION);
	}

	/**
	 * Adiciona uma mensagem de erro de um tipo especificado para ser exibida no sistema.
	 * @param mensagem Mensagem a ser exibida
	 * @param tipo Tipo da Mensagem
	 */
	public void addMessage(String mensagem, TipoMensagemUFRN tipo) {
		erros.addMensagem(new MensagemAviso(mensagem, tipo));
		hasErrors();
	}
	
	/**
	 * Adiciona uma cole��o de objetos MensagemAviso para que as
	 * mensagens sejam exibidas no sistema.
	 */
	@Deprecated
	public void addMensagens(Collection<MensagemAviso> mensagens) {
		for (MensagemAviso msg : mensagens)
			erros.addMensagem(msg);
		hasErrors();
	}

	/**
	 * Adiciona um objeto ListaMensagem para que as
	 * mensagens sejam exibidas no sistema.
	 */
	public boolean addMensagens(ListaMensagens listaMensagens) {
		for (MensagemAviso msg : listaMensagens.getMensagens())
			erros.addMensagem(msg);
		return hasErrors();
	}
	
	/**
	 * Adiciona um objeto ListaMensagem para as que
	 * mensagens sejam exibidas no sistema em p�ginas que usam Ajax.
	 */
	public void addMensagensAjax(ListaMensagens listaMensagens) {
		erros.addAll(listaMensagens);
		getCurrentRequest().setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		erros = new ListaMensagens();
	}
	
	
	/**
	 *  Adiciona uma mensagem de informa��o para as p�ginas que usam ajax. Para funcionar � preciso incluir 
	 *  /WEB-INF/jsp/include/errosAjax.jsp 
	 *
	 * @param mensagem
	 */
	public void addMensagemInfoAjax(String mensagem){
		erros.addInformation(mensagem);
		getCurrentRequest().setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		erros = new ListaMensagens();
	}
	
	/**
	 * Adiciona uma mensagem de warning para as p�ginas que usam ajax. Para funcionar � preciso incluir 
	 *  /WEB-INF/jsp/include/errosAjax.jsp 
	 *  
	 * @param mensagem
	 */
	public void addMensagemWarningAjax(String mensagem){
		erros.addWarning(mensagem);
		getCurrentRequest().setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		erros = new ListaMensagens();
	}
	
	
	/**
	 * Adiciona uma mensagem de erro para as p�ginas que usam ajax. Para funcionar � preciso incluir 
	 *  /WEB-INF/jsp/include/errosAjax.jsp 
	 *  
	 * @param mensagem
	 */
	public void addMensagemErroAjax(String mensagem){
		erros.addErro(mensagem);
		getCurrentRequest().setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		erros = new ListaMensagens();
	}
	
	/**
	 * Adiciona uma mensagem ao sistema buscando-a no banco de dados,
	 * de acordo com o c�digo passado como par�metro.
	 * @param codigo
	 */
	public void addMensagemAjax(String codigo, Object... params) {
		MensagemAviso msg = UFRNUtils.getMensagem(codigo, params);
		
		erros.addMensagem(msg);
		getCurrentRequest().setAttribute(ListaMensagens.LISTA_MENSAGEM_SESSION, erros);
		erros = new ListaMensagens();
	}
	
	/**
	 * ----------------------------------------------------
	 * - M�TODOS PARA TRATAMENTO DE EXCE��ES
	 * ----------------------------------------------------
	 */
	
	/**
	 * Analisa a exce��o e notifica o erro por e-mail
	 */
	public void analisaExcecao(Throwable e, HttpServletRequest req,
			HttpServletResponse res) throws IOException, ServletException {
		ErrorUtils.enviaAlerta(e, req);
	}
	
	/**
	 * Notifica o erro em produ��o atrav�s do envio de e-mails.
	 */
	public void notifyError(Exception e) {

		try {
			analisaExcecao(e, getCurrentRequest(), getCurrentResponse());
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		e.printStackTrace();
	}
	
	/**
	 * M�todo que faz o tratamento padr�o de exce��es no controlador
	 * pode personalizar a msg do usu�rio atrav�s do par�metro msg.
	 * Adiciona mensagem de erro padr�o e envia e-mail de erro.
	 */
	public String tratamentoErroPadrao(Exception e, String msg){
		notifyError(e);
		if( isEmpty(msg) )
			addMensagemErroPadrao();
		else
			addMensagemErro(msg);
		e.printStackTrace();
		return null;
	}
	
	/**
	 * M�todo que faz o tratamento padr�o de exce��es do controlador.
	 * Adiciona mensagem de erro padr�o e envia e-mail de erro.
	 */
	public String tratamentoErroPadrao(Exception e){
		return tratamentoErroPadrao(e, null);
	}
	
	
	/**
	 * ----------------------------------------------------
	 * - M�TODOS PARA NAVEGA��O ENTRE P�GINAS
	 * ----------------------------------------------------
	 */
	
	/**
	 * Redireciona o usu�rio para uma url concatenando
	 * o contexto da aplica��o.
	 */
	public String redirect(String url) {
		String context = getContextPath();
		if (!url.startsWith(context) && !url.startsWith("http://"))
			url = context + url;
		return redirectSemContexto(url);
	}

	/**
	 * Redireciona o usu�rio para uma url sem concatenar
	 * o contexto.
	 */
	public String redirectSemContexto(String url) {
		
		try {
			getCurrentResponse().sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FacesContext.getCurrentInstance().responseComplete();
		return null;
	}
	
	/**
	 * Redireciona para p�ginas concatenando o contexto
	 * e renomeando a extens�o de jsp para jsf.
	 */
	public String redirectJSF(String url) {
		
		if (url.indexOf(getContextPath()) == -1)
			url = getContextPath() + "/"+ url;
		if ( url.endsWith("jsp") )
			url = url.substring(0,url.length() -3 ) + "jsf";
		
		redirectSemContexto(url);
		return null;
	}
	
	/**
	 * Redireciona para a mesma p�gina carregada.
	 */
	public String redirectMesmaPagina() {
		redirectJSF(getCurrentURL());
		return null;
	}
	
	/**
	 * Realiza um forward para a jsp passada como par�metro.
	 */
	public String forward(String url) {
		FacesContext context = FacesContext.getCurrentInstance();
		Application app = context.getApplication();
		UIViewRoot view = app.getViewHandler().createView(context, url);
		context.setViewRoot(view);
		context.renderResponse();

		// Retorna null para evitar o return null no action do Managed Bean
		return null;
	}

	/**
	 * Remove o bean da sess�o e redireciona o usu�rio para
	 * a p�gina inicial do subsistema atual.
	 */
	public String cancelar() {
		// removendo da sessao
		resetBean();
		
		try {
			redirectJSF(getSubSistema().getLink());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * ----------------------------------------------------
	 * - M�TODOS PARA ACESSO � OBJETOS DA API DE SERVLETS -
	 * ----------------------------------------------------
	 */
	
	
	/**
	 * Possibilita o acesso ao HttpServletRequest.
	 */
	public HttpServletRequest getCurrentRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	/**
	 * Possibilita o acesso ao HttpServletResponse.
	 */
	public HttpServletResponse getCurrentResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	/**
	 * Possibilita o acesso ao HttpSession.
	 */
	public HttpSession getCurrentSession() {
		return getCurrentRequest().getSession(true);
	}

	/**
	 * Retorna o contexto da aplica��o WEB
	 */
	public String getContextPath() {
		return RequestUtils.getContextPath(getCurrentRequest());
	}

	/**
	 * Retorna a URL atual
	 */
	public String getCurrentURL() {
		return RequestUtils.getCurrentURL(getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request
	 * @param param Nome do par�metro
	 * @return valor do par�metro em request 
	 */
	public String getParameter(String param) {
		return RequestUtils.getParameter(param, getCurrentRequest());
	}

	/**
	 * Retorna um array com o conjunto de valores para um par�metro em request
	 * @param param Nome do par�metro
	 * @return array com todos os valores do par�metro.
	 */
	public String[] getParameterValues(String param) {
		return RequestUtils.getParameterValues(param, getCurrentRequest());
	}
	
	/**
	 * Retorna um array de inteiros com o conjunto de valores 
	 * para um par�metro em request.
	 */
	public Integer[] getParameterIntValues(String param) {
		return RequestUtils.getParameterIntValues(param, getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request e converte para Integer. Caso
	 * o par�metro n�o exista, retorna null.
	 */
	public Integer getParameterInt(String param) {
		return RequestUtils.getParameterInt(param, getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request e converte para Integer. Caso
	 * o par�metro n�o exista, retorna o valor passado como valor padr�o.
	 * @param param Nome do par�metro
	 * @param defaultValue Valor padr�o, caso o par�metro seja null.
	 */
	public Integer getParameterInt(String param, Integer defaultValue) {
		return RequestUtils.getParameterInt(param, defaultValue, getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request e converte para Long. Caso
	 * o par�metro n�o exista, retorna null.
	 */
	public Long getParameterLong(String param) {
		return RequestUtils.getParameterLong(param, getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request e converte para Long. Caso
	 * o par�metro n�o exista, retorna o valor passado como valor padr�o.
	 * @param param Nome do par�metro
	 * @param defaultValue Valor padr�o, caso o par�metro seja null.
	 */
	public Long getParameterLong(String param, Long defaultValue) {
		return RequestUtils.getParameterLong(param, defaultValue, getCurrentRequest());
	}

	/**
	 * Pega um par�metro em request e converte para char. Se
	 * o par�metro for null, retorna espa�o.
	 */
	public char getParameterChar(String param){
		return RequestUtils.getParameterChar(param, getCurrentRequest());
	}
	
	/**
	 * Pega um par�metro em request e converte para char. Se
	 * o par�metro for null, retorna o char passado como par�metro.
	 */
	public char getParameterChar(String param, char defaultValue){
		return RequestUtils.getParameterChar(param, defaultValue, getCurrentRequest());
	}
	
	/**
	 * Pega um par�metro em request e converte para Boolean. Retorna
	 * true se o valor do par�metro for a String "true" (n�o case sensitive)
	 * e false para qualquer outro caso, inclusive null.
	 */
	public Boolean getParameterBoolean(String param) {
		return RequestUtils.getParameterBoolean(param, getCurrentRequest());
	}

	/**
	 * Remove todos os atributos da sess�o, com exce��o do usu�rio logado e
	 * do Acesso Menu.
	 */
	@SuppressWarnings("unchecked")
	protected void removerAtributosSessao() {
		Enumeration<String> atributos = getCurrentSession().getAttributeNames();
		while(atributos.hasMoreElements()) {
			String atributo = atributos.nextElement();
			if (!"usuario".equals(atributo) && !"acesso".equals(atributo)) {
				getCurrentSession().removeAttribute(atributo);
			}
		}
	}
	
	/**
	 * Acessa o external context do JavaServer Faces
	 **/
	private ExternalContext getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	/**
	 * Retorna o usu�rio logado.
	 * @param <V>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <V extends UsuarioGeral> V getUsuarioLogado() {
		return (V) getCurrentRequest().getSession().getAttribute("usuario");
	}
	
	/**
	 * Verifica se o usu�rio logado tem o papel passado no par�metro
	 * 
	 * @param papel
	 * @throws SegurancaException
	 */
	public void checkRole(int papel) throws SegurancaException {
		UFRNUtils.checkRole(getUsuarioLogado(), papel);
	}

	/**
	 * Verifica se o usu�rio logado tem algum dos pap�is passados nos par�metros
	 * 
	 * @param papeis
	 * @throws SegurancaException
	 */
	public void checkRole(int[]... papeis) throws SegurancaException {
		
		int[] res = ArrayUtils.EMPTY_INT_ARRAY;
		
		for (int[] is : papeis) {
			res = ArrayUtils.addAll(res, is);
		}
		
		UFRNUtils.checkRole(getUsuarioLogado(), res);
	}
	
	
	/**
	 * Verifica se o usu�rio logado tem algum dos pap�is passados nos par�metros
	 * 
	 * @param papeis
	 * @throws SegurancaException
	 */
	public void checkRole(int... papeis) throws SegurancaException {
		UFRNUtils.checkRole(getUsuarioLogado(), papeis);
	}
	
	/**
	 * Verifica se o usu�rio logado tem os pap�is passados como par�metro
	 * e se esses pap�is s�o v�lidos para a unidade do par�metro.
	 * @throws SegurancaException 
	 */
	public void checkRole(UnidadeGeral unidade, int... papeis) throws SegurancaException {
		UFRNUtils.checkRole(getUsuarioLogado(), unidade, papeis);
	}
	
	/**
	 * Verifica se o usu�rio logado tem os pap�is passados como par�metro
	 * e se esses pap�is s�o v�lidos para a unidade (considerando a hierarquia) do par�metro.
	 * @param unidade
	 * @param papeis
	 * @throws SegurancaException
	 */
	public void checkHierarchyRole(UnidadeGeral unidade, int... papeis) throws SegurancaException {
		UFRNUtils.checkHierarchyRole(getUsuarioLogado(), unidade, papeis);
	}

	/**
	 * Retorna true se usu�rio possuir alguns dos papeis passados como
	 * par�metro.
	 * @param papeis
	 * @return
	 * @throws SegurancaException
	 * @throws ArqException
	 */
	public boolean isUserInRole(int[]... papeis) {

		int[] res = ArrayUtils.EMPTY_INT_ARRAY;
		
		for (int[] is : papeis) {
			res = ArrayUtils.addAll(res, is);
		}
		
		UsuarioGeral user = getUsuarioLogado();
		for (int papel : res) {
			if (user.isUserInRole(papel))
				return true;
		}

		return false;
	}
	
	/**
	 * Retorna true se usu�rio possuir alguns dos papeis passados como
	 * par�metro.
	 * @param papeis
	 * @return
	 * @throws SegurancaException
	 * @throws ArqException
	 */
	public boolean isUserInRole(int... papeis) {

		UsuarioGeral user = getUsuarioLogado();
		for (int papel : papeis) {
			if (user.isUserInRole(papel))
				return true;
		}

		return false;
	}

	/**
	 * Retorna o registro de entrada do usu�rio logado.
	 * @return
	 */
	public RegistroEntrada getRegistroEntrada() {
		UsuarioGeral usuarioLogado = getUsuarioLogado();
		if (usuarioLogado != null)
			return usuarioLogado.getRegistroEntrada();
		else
			return null;
	}
	
	/**
	 * Retorna o registro de acesso p�blico da pessoa que est�
	 * acessando a �rea p�blica dos sistemas.
	 * @return
	 */
	public RegistroAcessoPublico getAcessoPublico() {
		return (RegistroAcessoPublico) getCurrentSession().getAttribute("REGISTRO_ACESSO_PUBLICO");
	}
	
	/**
	 * Possibilita alterar o tempo de dura��o da sess�o para o usu�rio
	 * logado. O tempo deve ser informado em minutos.
	 * 
	 * @param tempoMinutos
	 */
	public void setTempoSessao(int tempoMinutos) {
		getCurrentSession().setMaxInactiveInterval(tempoMinutos * 60);
	}
	
}