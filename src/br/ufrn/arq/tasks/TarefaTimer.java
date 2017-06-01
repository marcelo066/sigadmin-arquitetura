package br.ufrn.arq.tasks;

import java.util.concurrent.Future;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.ufrn.arq.negocio.FacadeDelegate;

/**
 * Classe base das tarefas. As tarefas nos casos de uso devem herdar essa classe
 * e definir os tempos de execu��o no registro do m�todo
 * TarefaScheduler.registrar
 *
 * @author Gleydson Lima
 *
 */
public abstract class TarefaTimer implements Runnable {

	/** Fachada para chamar o modelo */
	protected FacadeDelegate facade;
	
	/** Application context do Spring */
	protected ApplicationContext ac;
	
	/** Status da transa��o na qual a tarefa est� executando */
	protected TransactionStatus status;
	
	/** Id da execu��o atual da tarefa */
	protected int idExecucao;
	
	/** Id da tarefa */
	protected int idTarefa;

	/** Objeto para controle da execu��o da tarefa, permitindo o seu cancelamento, por exemplo. */
	protected Future<?> future;
	
	/** Indica que a tarefa foi cancelada */
	protected boolean cancelada;
	
	/** Express�o do CRON que indica quando a tarefa dever� ser executada. */
	protected String expressaoCron;

	/** Gerenciador de transa��es. Possibilita que o m�todo transacao() seja executado de forma transacional. */
	protected PlatformTransactionManager tx;

	/** Gerenciador de execu��o de tarefas para registro de logs */
	private GerenciadorExecucaoTarefas gerenciador;

	/**
	 * M�todo que adiciona transa��o � execu��o do m�todo execucaoTransacional().
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	public Object transacao(Object... args) throws Exception {
		status = tx.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		
		try {
			Object result = execucaoTransacional(args);
			tx.commit(status);
			return result;
		} catch(Exception e) {
			tx.rollback(status);
			throw e;
		}
		
	}
	
	/**
	 * M�todo que deve ser sobrescrito pelas classes filhas e cujo conte�do
	 * ser� executado dentro de uma transa��o.
	 * @param args
	 * @return
	 */
	public Object execucaoTransacional(Object... args) throws Exception {
		return null;
	}
	
	/**
	 * Interrompe a execu��o da tarefa.
	 */
	public void interrupt() {
		cancelada = true;
		future.cancel(true);
	}
	
	/**
	 * Registra um log de informa��o para a execu��o da tarefa.
	 * @param texto
	 */
	protected void info(String texto) {
		getGerenciador().info(this, texto);
	}
	
	/**
	 * Registra um log de debug para a execu��o da tarefa.
	 * @param texto
	 */
	protected void debug(String texto) {
		getGerenciador().debug(this, texto);	
	}
	
	/**
	 * Registra um log de aviso para a execu��o da tarefa.
	 * @param texto
	 */
	protected void warn(String texto) {
		getGerenciador().warn(this, texto);
	}
	
	/**
	 * Registra um log de erro para a execu��o da tarefa.
	 * @param t
	 */
	protected void error(Throwable t) {
		getGerenciador().error(this, t);
	}
	
	private GerenciadorExecucaoTarefas getGerenciador() {
		if (gerenciador == null)
			gerenciador = (GerenciadorExecucaoTarefas) ac.getBean("gerenciadorExecucaoTarefas");
		return gerenciador;
	}
	
}
