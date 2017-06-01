/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 25/08/2010
 */
package br.ufrn.arq.dao.dialect;

/**
 * Interface das fun��es espec�ficas que n�o s�o contempladas pelo 
 * padr�o SQL-92 ([11:29:17] William Coelho Rocha: http://www.microsoft.com/express/) 
 * 
 * @author Gleydson Lima
 *
 */
public interface SQLFunctions {
	
	/**
	 * Implementa o m�todo para a fun��o extract onde a faz a extra��o de parte do dado de um campo.
	 * 
	 * @param campo
	 * @param args
	 * @return
	 */
	public String extract(String campo, String...args);
	
	/**
	 * Implementa o valor booleano verdadeiro 
	 *  
	 * @return
	 */
	public String getTrueValue();
	
	/**
	 * Implementa o valor booleano falso
	 * 
	 * @return
	 */
	public String getFalseValue();
	
	/**
	 * Retorna o pr�ximo valor para uma sequ�ncia
	 * 
	 * @param seq
	 * @return
	 */
	public String nextVal(String seq);
	
	/**
	 * Implementa a limita��o do retorno de resultados por uma consulta.
	 * 
	 * @param sql
	 * @param limit
	 * @param offset
	 * @return
	 */
	public String limit(String sql, int limit, int offset);
	
	/**
	 * Implementa a fun��o que retorna a data e hora atual.
	 * 
	 * @return
	 */
	public String now();
	
	/**
	 * M�todo que implementa como deve ser verificado se uma sequ�ncia j� est� criada no banco de dados
	 * @param schema
	 * @param sequencia
	 * @return
	 */
	public String verificarExistenciaSequencia(String schema , String sequencia);
	
	/**
	 * M�todo que implementa como deve ser feita a cria��o de uma sequ�ncia.
	 * 
	 * @param schema
	 * @param sequencia
	 * @return
	 */
	public String criarSequencia(String schema , String sequencia) ;
	
	/**
	 * M�todo que implementa a concatena��o de strings numa consulta
	 * @param s1
	 * @param s2
	 * @return
	 */
	public String concat(String... strings);
	
	/**
	 * Recupera o valor atual de uma sequ�ncia.
	 * 
	 * @param schema
	 * @param sequencia
	 * @return
	 */
	public String getCurrentSeqValue(String schema, String sequencia);
	
	/**
	 * M�todo respons�vel por fazer a convers�o de uma string para ascii em uma consulta.
	 * 
	 * @param texto
	 * @return
	 */
	public String toAscii(String campo,String formato);
	
	/**
	 * Implementa a fun��o que retorna o tempo corrente
	 * @return
	 */
	public String currentTimestamp();
	
	/**
	 * Implementa a fun��o que retorna um intervalo de tempo.
	 * 
	 * @param tempo intervalo tempo
	 * @return
	 */
	public String interval(String tempo);
	
	/**
	 * Retorna novos registros a partir da divis�o de uma string pelo seu delimitador, 
	 * ou seja, ir� gerar novas linhas para cada ocorr�ncias do delimitador.
	 * <br /><br />
	 * Ex: <br />        
	 * SELECT 1, regexp_split_to_table('1,2,3',',')
	 * <br />
	 * Resultado:<br />
	 * | 1 | 1 |<br />
	 * | 1 | 2 |<br />
	 * | 1 | 3 |<br />  
	 * 
	 * @param campo
	 * @return
	 */
	public String regexpSplitToTable(String campo, String delimitador);
	
}