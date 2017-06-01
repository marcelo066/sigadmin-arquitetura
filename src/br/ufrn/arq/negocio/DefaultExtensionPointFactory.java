package br.ufrn.arq.negocio;

import br.ufrn.arq.erros.ConfiguracaoAmbienteException;
import br.ufrn.arq.parametrizacao.ParametroHelper;


/**
 * Classe padr�o de pontos de extens�o para parametriza��es de regra de neg�cio 
 * atrav�s de plug-ins de c�digo.
 * 
 * @author Gleydson Lima
 *
 */
public class DefaultExtensionPointFactory {

	/**
	 * Retorna a implementa��o do ponto de extens�o parametrizado.
	 * @param parametro
	 * @return
	 * @throws ConfiguracaoAmbienteException
	 */
	public static Object getImplementation(String parametro) throws ConfiguracaoAmbienteException {
		
		String className = ParametroHelper.getInstance().getParametro(parametro);
		try {
			return Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new ConfiguracaoAmbienteException(e);
		}
	}
	
}
