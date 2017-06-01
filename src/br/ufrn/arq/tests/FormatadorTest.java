/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 23/05/2008
 */
package br.ufrn.arq.tests;

import junit.framework.TestCase;
import br.ufrn.arq.util.Formatador;

/** 
 * JUnit para testes da classe {@link Formatador}
 * 
 * @author �dipo Elder
 * @author Gleydson Lima
 *
 */
public class FormatadorTest extends TestCase {

	public void testFormataNomePessoa() {
		assertEquals("Aane Daniele da Silva Pessoa", Formatador.getInstance().formataNomePessoa("AANE DANIELE DA SILVA PESSOA"));
		assertEquals("Abel Bezerra J�nior", Formatador.getInstance().formataNomePessoa("ABEL BEZERRA J�NIOR"));
		assertEquals("Abel C�cero Belarmino Amorim de Freitas Neto", Formatador.getInstance().formataNomePessoa("ABEL C�CERO BELARMINO AMORIM DE FREITAS NETO"));
		assertEquals("Abel Lameque Silva Damasceno", Formatador.getInstance().formataNomePessoa("ABEL LAMEQUE SILVA DAMASCENO"));
		assertEquals("Abel Lima Bisneto", Formatador.getInstance().formataNomePessoa("ABEL LIMA BISNETO"));
		assertEquals("�bia Oliveira de Souza", Formatador.getInstance().formataNomePessoa("�BIA OLIVEIRA DE SOUZA"));
		assertEquals("Abigail No�dia Barbalho da Silva", Formatador.getInstance().formataNomePessoa("ABIGAIL NO�DIA BARBALHO DA SILVA"));
		assertEquals("Ab�lio Junior de Medeiros", Formatador.getInstance().formataNomePessoa("AB�LIO JUNIOR DE MEDEIROS"));
		assertEquals("Abimael Esdras Carvalho de Moura Lira", Formatador.getInstance().formataNomePessoa("ABIMAEL ESDRAS CARVALHO DE MOURA LIRA"));
		assertEquals("Abimaelle Silva Chib�rio", Formatador.getInstance().formataNomePessoa("ABIMAELLE SILVA CHIB�RIO"));
		assertEquals("Abimax Marcelino da Silva", Formatador.getInstance().formataNomePessoa("ABIMAX MARCELINO DA SILVA"));
		assertEquals("Abinoan de Carvalho", Formatador.getInstance().formataNomePessoa("ABINOAN DE CARVALHO"));
		assertEquals("Abnele de Queiroz Ramalho", Formatador.getInstance().formataNomePessoa("ABNELE DE QUEIROZ RAMALHO"));
		assertEquals("�bner Alves Moreira", Formatador.getInstance().formataNomePessoa("�BNER ALVES MOREIRA"));
		assertEquals("Abner Carlos Costa de Melo", Formatador.getInstance().formataNomePessoa("ABNER CARLOS COSTA DE MELO"));
		assertEquals("Abner Paulo de Araujo", Formatador.getInstance().formataNomePessoa("ABNER PAULO DE ARAUJO"));
		assertEquals("Abner Pinto de Aguiar Junior", Formatador.getInstance().formataNomePessoa("ABNER PINTO DE AGUIAR JUNIOR"));
		assertEquals("Abra�o Azevedo Lopes Segundo", Formatador.getInstance().formataNomePessoa("ABRA�O AZEVEDO LOPES SEGUNDO"));
		assertEquals("Abra�o Bernardo Coelho da Silva", Formatador.getInstance().formataNomePessoa("ABRA�O BERNARDO COELHO DA SILVA"));
		assertEquals("Abra�o Bruno Lima de Moura", Formatador.getInstance().formataNomePessoa("ABRA�O BRUNO LIMA DE MOURA"));
		assertEquals("Abra�o Ferreira Soares dos Santos", Formatador.getInstance().formataNomePessoa("ABRA�O FERREIRA SOARES DOS SANTOS"));
		assertEquals("Abra�o Jos� Azevedo dos Santos", Formatador.getInstance().formataNomePessoa("ABRA�O JOS� AZEVEDO DOS SANTOS"));
		assertEquals("Abra�o Lima de Sousa", Formatador.getInstance().formataNomePessoa("ABRA�O LIMA DE SOUSA"));
		assertEquals("Abra�o Lucas Ferreira Guimar�es", Formatador.getInstance().formataNomePessoa("ABRA�O LUCAS FERREIRA GUIMAR�ES"));
		assertEquals("Abra�o Lucas Ferreira Guinar�es", Formatador.getInstance().formataNomePessoa("ABRA�O LUCAS FERREIRA GUINAR�ES"));
		assertEquals("Abrah�o Henrique Fernandes", Formatador.getInstance().formataNomePessoa("ABRAH�O HENRIQUE FERNANDES"));
		assertEquals("Absal�o de Paiva Fonseca", Formatador.getInstance().formataNomePessoa("ABSAL�O DE PAIVA FONSECA"));
		assertEquals("Abson Isaque Santos de Albuquerque", Formatador.getInstance().formataNomePessoa("ABSON ISAQUE SANTOS DE ALBUQUERQUE"));
		assertEquals("Abylene Melo da Silva", Formatador.getInstance().formataNomePessoa("ABYLENE MELO DA SILVA"));
		assertEquals("Ac�cia Mikaela Santos de Farias", Formatador.getInstance().formataNomePessoa("AC�CIA MIKAELA SANTOS DE FARIAS"));
		assertEquals("Ac�cia Pierre dos Santos Medeiros", Formatador.getInstance().formataNomePessoa("AC�CIA PIERRE DOS SANTOS MEDEIROS"));
		assertEquals("Ac�cio Emanuel de Oliveira Barbosa", Formatador.getInstance().formataNomePessoa("AC�CIO EMANUEL DE OLIVEIRA BARBOSA"));
		assertEquals("Ac�cio Lopes Borges de Ara�jo", Formatador.getInstance().formataNomePessoa("AC�CIO LOPES BORGES DE ARA�JO"));
		assertEquals("Ac�cio Soares J�nior", Formatador.getInstance().formataNomePessoa("AC�CIO SOARES J�NIOR"));
		assertEquals("Ac�cio Trindade da Silva", Formatador.getInstance().formataNomePessoa("AC�CIO TRINDADE DA SILVA"));
		assertEquals("A�a� Marques do Nascimento", Formatador.getInstance().formataNomePessoa("A�A� MARQUES DO NASCIMENTO"));
		assertEquals("Ac�ssio dos Anjos Ara�jo", Formatador.getInstance().formataNomePessoa("AC�SSIO DOS ANJOS ARA�JO"));
		assertEquals("Acherlin Serafim da Silva", Formatador.getInstance().formataNomePessoa("ACHERLIN SERAFIM DA SILVA"));
		assertEquals("Aciene Leite de Souza", Formatador.getInstance().formataNomePessoa("ACIENE LEITE DE SOUZA"));
		assertEquals("Acsa Sunamita Bertulino da Silva", Formatador.getInstance().formataNomePessoa("ACSA SUNAMITA BERTULINO DA SILVA"));
		assertEquals("Acynelly Dafne da Silva Nunes", Formatador.getInstance().formataNomePessoa("ACYNELLY DAFNE DA SILVA NUNES"));
		assertEquals("Adaiany Vieira da Costa", Formatador.getInstance().formataNomePessoa("ADAIANY VIEIRA DA COSTA"));
		assertEquals("Adailton Barbosa da Costa Junior", Formatador.getInstance().formataNomePessoa("ADAILTON BARBOSA DA COSTA JUNIOR"));
		assertEquals("Adailton de Souza Pereira", Formatador.getInstance().formataNomePessoa("ADAILTON DE SOUZA PEREIRA"));
		assertEquals("Adailton Jos� Mendes de Azev�do", Formatador.getInstance().formataNomePessoa("ADAILTON JOS� MENDES DE AZEV�DO"));
		assertEquals("Adailton Lopes Monteiro", Formatador.getInstance().formataNomePessoa("ADAILTON LOPES MONTEIRO"));
		assertEquals("Adailton Ramos de Abreu", Formatador.getInstance().formataNomePessoa("ADAILTON RAMOS DE ABREU"));
		assertEquals("Adailton Rodrigues Bezerra", Formatador.getInstance().formataNomePessoa("ADAILTON RODRIGUES BEZERRA"));
		assertEquals("Adailza Denize da Silva", Formatador.getInstance().formataNomePessoa("ADAILZA DENIZE DA SILVA"));
		assertEquals("Ada�na Maria da Silva", Formatador.getInstance().formataNomePessoa("ADA�NA MARIA DA SILVA"));
		assertEquals("Adaise Leite Maia Brasil", Formatador.getInstance().formataNomePessoa("ADAISE LEITE MAIA BRASIL"));
	}
}
