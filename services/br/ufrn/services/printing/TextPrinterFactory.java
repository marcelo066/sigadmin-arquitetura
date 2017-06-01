/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 20/06/2008
 */
package br.ufrn.services.printing;

import java.io.IOException;

import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;

/**
 * Factory para criar um TextPrinter.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class TextPrinterFactory {

	private static TextPrinter printer;
	
	private TextPrinterFactory() {
		
	}

	/**
	 * Retorna uma instancia de TextPrinter.
	 */
	public static TextPrinter getPrinter() throws NoSuchPortException, PortInUseException, IOException {
		if (printer == null)
			printer = new TextPrinter();
		return printer;
	}

	/**
	 * Muda a inst�ncia padr�o da f�brica. M�todo �til 
	 * para a realiza��o de testes.
	 */
	public static void setPrinter(TextPrinter _printer) {
		printer = _printer;
	}
	
}
