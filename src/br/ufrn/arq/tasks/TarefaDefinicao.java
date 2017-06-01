/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 30/08/2011
 */
package br.ufrn.arq.tasks;

import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import br.ufrn.arq.util.AmbienteUtils;
import br.ufrn.arq.util.NetworkUtils;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Classe contendo as defini��es dos timers que est�o registrados
 * nos sistemas. 
 * 
 * @author David Pereira
 *
 */
public final class TarefaDefinicao {

	Logger logger = Logger.getLogger(getClass());
	
	public static final char TEMPO = 'T';

	public static final char DIARIO = 'D';

	public static final char SEMANAL = 'S';

	public static final char MENSAL = 'M';
	
	/** Identificador */
	int id;
	
	/** Classe que implementa a tarefa a ser executada. Essa classe deve estender TarefaTimer. */
	String classe;

	/** 
	 * Express�o do CRON que define quando a tarefa dever� ser executada. 
	 * Uma vis�o geral da constru��o de express�es do CRON pode ser encontrada nos
	 * seguintes endere�os: 
	 * 
	 * http://en.wikipedia.org/wiki/Cron#CRON_expression
	 * http://sanjaal.com/java/tag/cron-expression-every-minute/ 
	 */
	String expressaoCron;

	/**
	 * Servidor em que a tarefa ser� executada
	 */
	String servidorRestricao;

	// usado pelo escalonador
	/**
	 * Armazena a data e hora da �ltima execu��o do timer. 
	 */
	DateTime ultimaExecucao;
	
	/**
	 * Executar a tarefa compulsoriamente na pr�xima execu��o do TarefaScheduler
	 */
	boolean executarAgora;

	
	
	public UsuarioGeral getUsuario() {
		return new UsuarioGeral(UsuarioGeral.TIMER_SISTEMA);
	}
	
	/**
	 * A tarefa deve ser executada no servidor que est� rodando o Scheduler no momento.
	 * 
	 * @return
	 */
	public boolean deveExecutarNesteServidor() {
		String servidorRestricao = AmbienteUtils.getInstanceName();
		
		if (servidorRestricao == null)
			servidorRestricao = NetworkUtils.getLocalName();
		
		return this.servidorRestricao == null || servidorRestricao != null && servidorRestricao.equals(this.servidorRestricao);
	}

	/**
	 * A tarefa est� na hora de ser executada.
	 * @return
	 */
	public boolean estaNaHoraDeExecutar() {
		if (executarAgora) {
			return true;
		} else {
			Date dataAtual = new Date();
			
			try {
			
				CronExpression cron = new CronExpression(expressaoCron);
				
				logger.debug("Executando tarefa '" + classe + "' -- Express�o: " + cron.getExpressionSummary()
						+ " / Satisfeita pela data atual? " + cron.isSatisfiedBy(dataAtual)
						+ " / Pr�xima data v�lida: " + cron.getNextValidTimeAfter(dataAtual));

				return cron.isSatisfiedBy(dataAtual);
				
			} catch (ParseException e) {
				logger.error("N�o foi poss�vel executar a tarefa '" + classe + "' porque a express�o cron utilizada (" + expressaoCron + ") � inv�lida.");
				return false;
			}			
			
		}
	}

	/**
	 * Indica que a tarefa j� est� em execu��o neste servidor
	 * @return
	 */
	public boolean estaSendoExecutada() {
		return TarefaScheduler.EM_EXECUCAO.containsKey(classe);
	}

}