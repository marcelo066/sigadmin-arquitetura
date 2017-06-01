/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 22/06/2010
 */
package br.ufrn.arq.seguranca.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.hibernate.Session;

import br.ufrn.arq.dominio.ConstantesParametroGeral;
import br.ufrn.arq.parametrizacao.ParametroHelper;

/**
 * Classe com a finalidade de logar quando uma sess�o do Hibernate
 * foi aberta ou fechada. 
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class SessionLogger {

	private static Map<String, SessionInformation> sessions = new Hashtable<String, SessionInformation>();
	
	private static File file = new File(ParametroHelper.getInstance().getParametro(ConstantesParametroGeral.CAMINHO_LOG_SESSAO));
	
	static {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				FileWriter writer = null;
				
				if ( !file.exists() ) {
					file = new File ( System.getProperty("java.io.tmpdir") + File.separator + "session.log");
				}
				
				try {
					writer = new FileWriter(file, false);
					
					boolean encontrouSessaoAberta = false;
					for (Entry<String, SessionInformation> entry : sessions.entrySet()) {
						long now = System.currentTimeMillis();
						if (now  - entry.getValue().getTempoAbertura() > ParametroHelper.getInstance().getParametroInt(ConstantesParametroGeral.TEMPO_MAXIMO_SESSAO_ABERTA) ) {
							writer.write(getSessionInfo(entry.getValue()) + System.getProperty("line.separator"));
							encontrouSessaoAberta = true;
						}					
					}
					
					// Se n�o encontrar sess�o aberta, informa ao usu�rio para mostrar que a thread est� sendo executada
					if (!encontrouSessaoAberta) {
						writer.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + " - Nenhuma sess�o aberta foi encontrada!");
					}
					
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/*
			 * Monta a String com as informa��es da sess�o
			 */
			private String getSessionInfo(SessionInformation sessionInfo) {
				Thread currentThread = Thread.currentThread();
				return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + " ; " + currentThread  
					+ " ; " + " sess�o: " + System.identityHashCode(sessionInfo.getSession()) + " ; " +  sessionInfo.getStackTraceAbertura();
			}
			
		}, 1 * 60 * 1000, 1 * 60 * 1000);
	}
	
	/**
	 * Loga quando a sess�o do Hibernate passada como par�metro
	 * for aberta (par�metro opening for true) ou fechada (par�metro
	 * opening for false).
	 * 
	 * @param session
	 * @param opening  'O' - Open  'C' - Close  
	 */
	public static void log(Session session, char operacao) {
		
		// Faz a contagem de abertura de sess�o por m�todo �til do StackTrace
		StackTraceElement[] threadElements = filterStackTrace(Thread.currentThread().getStackTrace());
		String stackIdentifier = Arrays.toString(threadElements);
		
		// Adiciona �s sess�es abertas
		if ( operacao == 'O' )
			sessions.put( String.valueOf(System.identityHashCode(session)) , new SessionInformation(session, stackIdentifier) );
		else
			sessions.remove( String.valueOf(System.identityHashCode(session)) );
	}
	
	/*
	 * Filtra os StackTraceElements para deixar apenas aqueles que forem importantes para
	 * a verifica��o do fechamento de sess�es.
	 */
	private static StackTraceElement[] filterStackTrace(StackTraceElement[] elements ) {
		List<StackTraceElement> list = new ArrayList<StackTraceElement>(elements.length);
		
		for (int i = 3; i < elements.length; i++) {
			StackTraceElement element = elements[i];
			
			if (element.getClassName().startsWith("br.ufrn") 
					&& (!element.getClassName().startsWith("br.ufrn.arq") || element.getClassName().contains("AbstractController") || (element.getClassName().contains(".dao.") 
					&& !element.getClassName().contains("Factory"))) && 
					( !element.getClassName().contains("Filter")) || ( element.getClassName().contains("ViewFilter"))) {
				list.add(element);
			}
		}
		
		return list.toArray(new StackTraceElement[list.size()]);
	}
	
}
