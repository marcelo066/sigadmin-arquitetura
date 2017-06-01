/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 23/05/2008
 */
package br.ufrn.arq.tests;

import java.text.ParseException;
import java.util.Calendar;

import junit.framework.TestCase;
import br.ufrn.arq.util.CalendarUtils;
import br.ufrn.arq.util.NetworkUtils;
import br.ufrn.arq.util.UFRNUtils;

/**
 * Testes para a classe UFRNUtils.
 * 
 * @author Gleydson Lima
 * @author David Pereira
 *
 */
public class UFRNUtilsTest extends TestCase {

	public void testTruncateDouble() {
		// Arredondamento para baixo
		assertEquals(8.123, UFRNUtils.truncateDouble(8.123456789, 3));
		
		// Arredondamento para cima
		assertEquals(8.12346, UFRNUtils.truncateDouble(8.123456789, 5));
		
		// Informando n�mero de casas negativo
		assertEquals(0.0, UFRNUtils.truncateDouble(8.123456789, -8));
	}

	public void testTruncateSemArrendondar() {
		// Truncar em 3 casas decimais
		assertEquals(8.123, UFRNUtils.truncateSemArrendondar(8.123456789, 3));
		
		// Truncar em 5 casas decimais
		assertEquals(8.12345, UFRNUtils.truncateSemArrendondar(8.123456789, 5));
		
		// Truncar em um n�mero negativo de casas
		assertEquals("N�o trata entradas nulas.", 0.0, UFRNUtils.truncateSemArrendondar(8.123456789, -8));
	}

	public void testToSHA1Digest() {
		// Validando devolu��o de string pelo m�todo
		assertNotNull(UFRNUtils.toSHA1Digest("senha"));
		
		// Passando como par�metro uma string vazia
		assertNotNull(UFRNUtils.toSHA1Digest(""));
		
		// Passando como par�metro um valor nulo
		assertNotNull(UFRNUtils.toSHA1Digest(null));
	}

	public void testValidaIP() {
		// A classe alvo do teste est� desabilitada
	}

	public void testDominioToIdArray() {
		// No caso desse teste, o atributo acessado ser� Id. Logo qualquer classe que possua
		// este atributo poder� ser inclu�do. Nesse caso, a classe escolhida foi "Unidade".
//		Collection<Unidade> unidades = new ArrayList<Unidade>();
//		
//		// Adicionando unidades na cole��o a ser testada
//		unidades.add(new Unidade(22));
//		unidades.add(new Unidade(25));
//		unidades.add(new Unidade(26));
//		unidades.add(new Unidade(27));
//		unidades.add(new Unidade(28));
//		
//		// Testando se o array devolvido n�o est� vazio
//		assertTrue(UFRNUtils.dominioToIdArray(unidades).length > 0);
	}

	public void testDominioToId() {
		// No caso desse teste, o atributo acessado ser� Id. Logo qualquer classe que possua
		// este atributo poder� ser inclu�do. Nesse caso, a classe escolhida foi "Unidade".
//		Collection<Unidade> unidades = new ArrayList<Unidade>();
//		
//		// Adicionando unidades na cole��o a ser testada
//		unidades.add(new Unidade(22));
//		unidades.add(new Unidade(25));
//		unidades.add(new Unidade(26));
//		unidades.add(new Unidade(27));
//		unidades.add(new Unidade(28));
//		
//		// Testando se o array devolvido n�o est� vazio
//		assertTrue(UFRNUtils.dominioToId(unidades).size() > 0);
	}

	public void testGetLocalAddress() {
		// Testando retorno de endere�o local
		assertNotNull(NetworkUtils.getLocalAddress());
	}

	public void testGetLocalName() {
		// Testando retorno de endere�o local
		assertNotNull(NetworkUtils.getLocalName());
	}

	public void testCompletaZeros() {
		// Formato com 5 d�gitos
		assertEquals("00036", UFRNUtils.completaZeros(36, 5));
		
		// Passando valores inv�lidos de d�gitos
		assertEquals("36", UFRNUtils.completaZeros(36, 0));			// 0 d�gitos
		assertEquals("36", UFRNUtils.completaZeros(36, -9));		// D�gitos negativos
	}

	public void testGetAnoAtual() {
		// Testando retorno do ano corrente
		assertEquals(2008, CalendarUtils.getAnoAtual());
	}

	public void testGetMesAtual() {
		// Testando retorno do ano corrente
		assertEquals(4, CalendarUtils.getMesAtual());
	}

	public void testParseDate() throws ParseException {
		// Informando valores v�lidos de datas
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2008);
		c.set(Calendar.MONTH, Calendar.MAY);
		c.set(Calendar.DAY_OF_MONTH, 30);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		assertEquals(c.getTime(),CalendarUtils.parseDate("2008-05-30"));
	}

	public void testParseDate2() throws ParseException {
		// Informando valores v�lidos
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2008);
		c.set(Calendar.MONTH, Calendar.MAY);
		c.set(Calendar.DAY_OF_MONTH, 30);
		c.set(Calendar.AM_PM, Calendar.AM);
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		assertEquals(c.getTime(),CalendarUtils.parseDate("30/05/2008","dd/MM/yyyy"));
	}

	public void testTrataAspasSimples() {
		// Informando valores v�lidos
		assertEquals("''''''", UFRNUtils.trataAspasSimples("'''"));
		assertEquals("''ok''", UFRNUtils.trataAspasSimples("'ok'"));
	}

	public void testEscapeHTML() {
		// Informando valores v�lidos
		assertEquals("&lt;p&gt;\"ala", UFRNUtils.escapeHTML("<p>\"ala"));
	}

	public void testMain() {
		// A classe alvo n�o possui nenhuma fun��o vital, creio que n�o est� sendo usada
	}

	public void testToArrayList() {
		
	}

	public void testParseCpfCnpj() {
		fail("Not yet implemented");
	}

	public void testEvalProperty() {
		fail("Not yet implemented");
	}

	public void testEvalPropertyObj() {
		fail("Not yet implemented");
	}

	public void testPropertyExists() {
		fail("Not yet implemented");
	}

	public void testIsFKConstraintError() {
		fail("Not yet implemented");
	}

	public void testGetClassName() {
		fail("Not yet implemented");
	}

	public void testCleanTransientObjectsPersistDB() {
		fail("Not yet implemented");
	}

	public void testCleanTransientObjectsPersistDBListOfMethod() {
		fail("Not yet implemented");
	}

	public void testCompletaAno() {
		fail("Not yet implemented");
	}

	public void testGetIcone() {
		fail("Not yet implemented");
	}

	public void testGetAno() {
		fail("Not yet implemented");
	}

	public void testResultSetToHashMap() {
		fail("Not yet implemented");
	}

	public void testResultSetToCSV() {
		fail("Not yet implemented");
	}

	public void testToAsciiUpperUTF8() {
		fail("Not yet implemented");
	}

	public void testToAsciiUTF8() {
		fail("Not yet implemented");
	}

	public void testConvertUtf8Latin9() {
		fail("Not yet implemented");
	}

	public void testConvertUtf8UpperLatin9() {
		fail("Not yet implemented");
	}

	public void testIdentificaBrowserRecomendado() {
		fail("Not yet implemented");
	}

	public void testToMD5() {
		fail("Not yet implemented");
	}

	public void testGeraCaracteres() {
		fail("Not yet implemented");
	}

	public void testIsDentroPeriodo() {
		fail("Not yet implemented");
	}

	public void testGerarStringInIntArray() {
		fail("Not yet implemented");
	}

	public void testGerarStringInCharArray() {
		fail("Not yet implemented");
	}

	public void testGerarStringInCollection() {
		fail("Not yet implemented");
	}

	public void testGerarStringInStringArray() {
		fail("Not yet implemented");
	}

	public void testCompletaEspacos() {
		fail("Not yet implemented");
	}

	public void testGerarStringInObjectArray() {
		fail("Not yet implemented");
	}

	public void testRedimensionaJPG() {
		fail("Not yet implemented");
	}

	public void testAdicionaUmDia() {
		fail("Not yet implemented");
	}

	public void testDeepCopy() {
		fail("Not yet implemented");
	}

	public void testVerificaEntropiaSenha() {
		fail("Not yet implemented");
	}

	public void testIsServidorProducao() {
		fail("Not yet implemented");
	}

	public void testIsServidorTestes() {
		fail("Not yet implemented");
	}

	public void testToIntArray() {
		fail("Not yet implemented");
	}

	public void testGetMesAbreviadoDate() {
		fail("Not yet implemented");
	}

	public void testGetMesByData() {
		fail("Not yet implemented");
	}

	public void testGetMesAbreviadoInt() {
		fail("Not yet implemented");
	}

	public void testGetMaximoDia() {
		fail("Not yet implemented");
	}

	public void testToTreeSet() {
		fail("Not yet implemented");
	}

	public void testToHashSet() {
		fail("Not yet implemented");
	}

	public void testGenerateAcronymTag() {
		fail("Not yet implemented");
	}

	public void testDiferencaDias() {
		fail("Not yet implemented");
	}

	public void testDataMaisProxima() {
		fail("Not yet implemented");
	}

	public void testDiferencaMeses() {
		fail("Not yet implemented");
	}

	public void testIsNotSecure() {
		fail("Not yet implemented");
	}

	public void testToList() {
		fail("Not yet implemented");
	}

	public void testToSet() {
		fail("Not yet implemented");
	}

	public void testContains() {
		fail("Not yet implemented");
	}

}
