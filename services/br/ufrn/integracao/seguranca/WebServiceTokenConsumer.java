/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 08/04/2011
 */
package br.ufrn.integracao.seguranca;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import br.ufrn.arq.erros.TokenNaoValidadoException;
import br.ufrn.arq.seguranca.autenticacao.TokenGenerator;

/**
 * Interceptor para acrescentar aos web services a necessidade de verificar se 
 * um token de autentica��o foi gerado por um cliente para poder executar. Necess�rio 
 * para melhorar o aspecto da seguran�a da integra��o entre os sistemas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class WebServiceTokenConsumer extends AbstractPhaseInterceptor<Message> implements ApplicationContextAware {

	private TokenGenerator token;
	
	private ApplicationContext ac;
	
	public WebServiceTokenConsumer() {
		super(Phase.RECEIVE);
	}
	
	private TokenGenerator generator() {
		if (token == null) token = (TokenGenerator) ac.getBean("tokenGenerator");
		return token;
	}
	
	@Override
	public void handleMessage(Message message) throws Fault {
		String params = (String) message.get(Message.QUERY_STRING);
		String[] paramsArr = params.split("\\|");
		String key = paramsArr[0];
		
		Integer idToken = -1;
		
		try {
			idToken = Integer.valueOf(paramsArr[1].toString());
		} catch(Exception e) {
			// Tentativa de acessar sem ser pelo TokenAuthHttpInvokerProxyFactoryBean
			e.printStackTrace();
		}

		if (!generator().isTokenValid(idToken, key)) {
			throw new TokenNaoValidadoException("N�o � poss�vel acessar o servi�o porque n�o foi encontrado um token de autentica��o para execut�-lo.");
		}

		generator().invalidateToken(idToken);
	}

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		this.ac = ac;
	}
	
}
