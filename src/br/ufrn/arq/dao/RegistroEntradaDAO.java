/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 13/07/2006
 */
package br.ufrn.arq.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.ufrn.arq.dominio.RegistroEntrada;
import br.ufrn.arq.dominio.RegistroEntradaDevice;
import br.ufrn.arq.seguranca.log.LogOperacao;

/**
 * DAO para a realiza��o de buscas associadas � entidade RegistroEntrada
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public interface RegistroEntradaDAO extends GenericDAO {

	/**
	 * Busca por registros de entradas que tiveram atividades nos ultimos
	 * minutos
	 *
	 * @param minutos
	 * @return
	 */
	public Collection<RegistroEntrada> findByAtividade(int minutos);

	/**
	 * Busca por logs de opera��o de acordo com as datas passadas como par�metro.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @return
	 */
	public Collection<LogOperacao> findByDatas(Date dataInicio, Date dataFim);

	/**
	 * Busca por logs de opera��o de acordo com as datas passadas como par�metro. Pode retornar
	 * apenas os logs que possuem erro, caso o par�metro apenasErros seja passado como true.
	 * @param dataInicio
	 * @param dataFim
	 * @param apenasErros
	 * @return
	 */
	public Collection<LogOperacao> findByDatas(Date dataInicio, Date dataFim, boolean apenasErros);
	
	/**
	 * Busca todos os registros de entrada que possu�rem o IP passado como par�metro.
	 * @param ip
	 * @return
	 */
	public Collection<RegistroEntrada> findByIP(String ip);
	
	/**
	 * Busca todos os registros de entrada do usu�rio cujo login foi passado como par�metro.
	 * @param login
	 * @return
	 */
	public Collection<RegistroEntrada> findByLogin(String login);
	
	/**
	 * Busca os �ltimos n registros de entrada do usu�rio cujo login foi passado como par�metro, onde
	 * n � o par�metro maxResults.
	 * @param login
	 * @param maxResults
	 * @return
	 */
	public Collection<RegistroEntrada> findByLogin(String login, int maxResults);

	/**
	 * Busca um log de opera��o por id;
	 * @param idOperacao
	 * @return
	 */
	public LogOperacao findByOperacao(int idRegistroEntrada);
	
	/**
	 * Busca registros de entrada por um per�odo (data inicial e data final).
	 * @param periodoInicio
	 * @param periodoFim
	 * @return
	 */
	public Collection<RegistroEntrada> findByPeriodo(Date periodoInicio, Date periodoFim);

	/**
	 * Busca os registros de entrada que foram abertos nos �ltimos minutos.
	 * @param minutos
	 * @return
	 */
	public Collection<RegistroEntrada> findByTempo(int minutos);

	/**
	 * Busca pela hora da ultima atividade realizada pelo usu�rio
	 * cujo registro de entrada foi passado como par�metro. 
	 * @param idRegistroEntrada
	 * @return
	 */
	public Date findByUltimaAtividade(int idRegistroEntrada);

	/**
	 * Busca os log de opera��es do usu�rio de acordo com
	 * o registro de entrada passado como par�metro.
	 *
	 * @param idRegistroEntrada
	 * @return
	 */
	public Collection<LogOperacao> findLogByRegistro(int idRegistroEntrada);

	/**
	 * Busca os logs de opera��o em que ocorreram erros nos �ltimos minutos.
	 * @param minutos
	 * @return
	 */
	public Collection<LogOperacao> findLogErro(int minutos);

	/**
	 * Busca logs de opera��es de acordo com os par�metros passados.
	 * @param sistemaSelecionado
	 * @param data
	 * @param url
	 * @param idRegistroEntrada
	 * @param idRegistroAcessoPublico
	 * @param excecao
	 * @return 
	 */	
	public List<LogOperacao> findLogOperacoes(int sistemaSelecionado, Date data, String url, Integer idRegistroEntrada, Integer idRegistroAcessoPublico, String excecao);

	/**
	 * Busca um registro de entrada pelo seu id.
	 * @param idRegistro
	 * @return
	 */
	public RegistroEntrada findRegistroById(int idRegistro);
	
	/**
	 * Busca por registros de entrada que possuam referencia aos par�metros informados em seu log de opera��es
	 * @param parametros
	 * @param periodoInicio
	 * @param periodoFim
	 * @return
	 */
	public Collection<RegistroEntrada> findByParametros(String parametros, Date periodoInicio, Date periodoFim);
	
	/**
	 * Busca por registros de entrada que possuam referencia a action informada em seu log de opera��es
	 * @param parametros
	 * @param periodoInicio
	 * @param periodoFim
	 * @return
	 */
	public Collection<RegistroEntrada> findByAction(String action, Date periodoInicio, Date periodoFim);

	/**
	 *
	 * Busca quantidade de requisi��es por minuto usando como o log opera��o como base.
	 *
	 * @param data
	 * @param sistemas
	 * @return
	 */
	public Map<Integer, Double> findRequisicoesDiario(Date data, List<Integer> sistemas);

	/**
	 * Busca quantidade de requisi��es por m�s usando como o log opera��o como base.
	 * 
	 * @param ano
	 * @param sistema
	 * @return
	 */
	public Map<Integer, Double> findRequisicoesAnual(Integer ano, List<Integer> sistemas);

	/**
	 * Busca quantidade de requisi��es por dia em um m�s usando como o log opera��o 
	 * como base.
	 * 
	 * @param mes
	 * @param ano
	 * @param sistema
	 * @return
	 */
	public Map<Integer, Double> findRequisicoesMensal(Integer mes, Integer ano, List<Integer> sistemas);

	/**
	 * Busca quantidade de requisi��es por semana usando como o log opera��o 
	 * como base.
	 * 
	 * @param dataInicio
	 * @param dataFim
	 * @param sistema
	 * @return
	 */
	public Map<Integer, Double> findRequisicoesSemanal(Date dataInicio, Date dataFim, List<Integer> sistemas);

	/**
	 * Busca quantidade de requisi��es por minuto em uma hora de um dia usando como o log opera��o 
	 * como base.
	 * 
	 * @param data
	 * @param hora
	 * @param sistema
	 * @return
	 */
	public Map<Integer, Double> findRequisicoesHorario(Date data, Integer hora, List<Integer> sistemas);
	
	
	/**
	 * M�todo criado para persistir informa��es do dispositivo m�vel.
	 * @param registroDevice
	 */
	public void registrarDeviceInfo(RegistroEntradaDevice registroDevice);
}
