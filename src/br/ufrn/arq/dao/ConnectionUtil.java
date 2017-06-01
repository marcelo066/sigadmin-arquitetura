/*
 * Universidade Federal do Rio Grande do Norte
 * Superintendência de Informática 
 * Diretoria de Sistemas
 * 
 * Criado em: 24/11/2004
 */
package br.ufrn.arq.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe Utilitária para conexão com bancos externos aos dos sistemas.
 *
 * @author Gleydson Lima
 * @author David Pereira
 */
public class ConnectionUtil {

	public static Connection getGraduacaoCon() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		return DriverManager.getConnection("jdbc:oracle:thin:@gaspar.info.ufrn.br:1521:ufrn", "system", "systemdba817");
	}

	public static Connection getPosConnection() throws Exception {
		return DriverManager.getConnection("jdbc:oracle:thin:@gimenez.info.ufrn.br:1521:ufrn", "system", "systemdba817");
	}

	public static Connection getPesquisaConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection("jdbc:postgresql://desenvolvimento.info.ufrn.br/pesquisa", "sigaa", "sigaa");
	}

	public static Connection getCorrreiosConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:correios", "correios", "correios");
	}
	
	public static Connection getDMPConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:galisteu", "aluizio", "wizard");
	}

	public static Connection getFaturasConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbFatura", "", "");
	}

	public static Connection getSupinfraConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbSam", "", "");
	}

	public static Connection getRUConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbRU", "", "");
	}

	public static Connection getCCHLAConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCCHLA", "", "");
	}

	public static Connection getMaternidadeConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbMaternidade", "", "");
	}

	public static Connection getMaternidade2Connection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbMaternidade2", "", "");
	}

	public static Connection getMaternidade3Connection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbMaternidade3", "", "");
	}

	public static Connection getCCETConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCCET", "", "");
	}

	public static Connection getCTConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCT", "", "");
	}

	public static Connection getCBConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCB", "", "");
	}

	public static Connection getCCSConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCCS", "", "");
	}

	public static Connection getCCSAConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbCCSA", "", "");
	}

	public static Connection getBolsasConnection() throws Exception {
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		return DriverManager.getConnection("jdbc:odbc:dbBolsas", "", "");
	}

	public static Connection getFUNPECConnection() throws Exception {
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection("jdbc:postgresql://10.4.65.18:5432/sigap", "sipac", "patrimonio");
	}
	
	public static Connection getRHNetConnection() throws Exception {
		Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		return DriverManager.getConnection( "jdbc:microsoft:sqlserver://10.3.224.28:1433;DatabaseName=RHnet", "GeralSaf", "Saf");
	}

}