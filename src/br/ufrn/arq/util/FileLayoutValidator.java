/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 07/04/2010
 */
package br.ufrn.arq.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.ufrn.arq.negocio.validacao.ListaMensagens;

/**
 * Classe utilit�ria com o objetivo de realizar valida��es em layouts de arquivos.
 * O layout do arquivo pode ser passado de duas formas: atrav�s de um arquivo de layout
 * ou de uma String que descreve o layout das linhas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class FileLayoutValidator {

	/**
	 * Realiza a valida��o do arquivo passado como primeiro par�metro atrav�s do layout definido no arquivo passado
	 * como segundo par�metro. Os erros s�o armazenados na lista de mensagens passada como terceiro par�metro.
	 * @param theFile
	 * @param layoutFile
	 * @throws IOException 
	 */
	public static void validateFileLayout(File theFile, File layoutFile, ListaMensagens lista) throws IOException {
		FileLayout layout = new FileLayout(layoutFile);
		validateLayout(theFile, lista, layout);
	}

	/**
	 * Realiza a valida��o do arquivo passado como primeiro par�metro atrav�s do layout definido na String passada
	 * como segundo par�metro. Os erros s�o armazenados na lista de mensagens passada como terceiro par�metro.
	 * Na String, deve-se utilizar uma sequ�ncia de caracteres em que N significa que, naquela posi��o, deve haver
	 * um caracter num�rico e A significa que, naquela posi��o, deve haver um caracter alfanum�rico.
	 * 
	 * @param theFile
	 * @param layoutString
	 * @param lista
	 * @throws IOException
	 */
	public static void validateFileLayout(File theFile, String layoutString, ListaMensagens lista) throws IOException {
		FileLayout layout = new FileLayout(layoutString);
		validateLayout(theFile, lista, layout);
	}
	
	/*
	 * Dado o arquivo e um objeto da classe FileLayout, valida se o conte�do do arquivo
	 * est� de acordo com o layout, independentemente se o layout foi definido por outro arquivo
	 * ou por uma String.
	 */
	@SuppressWarnings("unchecked")
	private static void validateLayout(File theFile, ListaMensagens lista, FileLayout layout) throws IOException {
		List<String> fileLines = FileUtils.readLines(theFile);
		int currentLine = 0;
		for (String line : fileLines) {
			int cursor = 0;
			currentLine++;
			
			try {
				for (LayoutPart part : layout.getParts()) {
					int nextPos = cursor + part.length;
					String parte = line.substring(cursor, nextPos);
					
					if (part.isNumeric()) {
						Long.parseLong(parte);
					}
					
					cursor = nextPos;
				}
			} catch(Exception e) {
				lista.addErro("Erro ao processar o arquivo \"" + theFile.getName() + "\" na linha " + currentLine + ". O arquivo n�o est� no formato requerido. O formato da linha deveria ser \""
						+ layout.getLayoutString() + "\" e o seu conte�do atualmente � \"" + line + "\".");
			}
		}
	}

}

/**
 * Classe auxiliar para armazenar as informa��es das partes do layout de um arquivo.
 * Pode receber como par�metro do construtor um arquivo de layout um uma String
 * que define o formato do layout.
 * 
 * @author David Pereira
 *
 */
class FileLayout {
	private List<LayoutPart> parts = new ArrayList<LayoutPart>();
	StringBuilder layoutString = new StringBuilder();
	
	/**
	 * Construtor que define que o layout ser� criado a partir de um
	 * arquivo de layout.
	 * @param file
	 * @throws IOException
	 */
	public FileLayout(File file) throws IOException {
		@SuppressWarnings("unchecked")
		List<String> layoutLines = FileUtils.readLines(file);
		
		for (String line : layoutLines) {
			String[] partes = line.split(" ");
			LayoutPart layoutPart = new LayoutPart(partes[partes.length - 2], Integer.parseInt(partes[partes.length - 1]));
			
			for (int i = 0; i < layoutPart.length; i++) {
				layoutString.append(layoutPart.type);
			}
			
			parts.add(layoutPart);
		}
	}
	
	/**
	 * Construtor que define que o layout ser� criado a partir de uma
	 * String com o formato do layout.
	 * @param layout
	 */
	public FileLayout(String layout) {
		char current = '\0';
		LayoutPart part = null;
		
		for (int i = 0; i < layout.length(); i++) {
			char c = layout.charAt(i);
			
			if (c != current) {
				if (part != null) {
					parts.add(part);
				}
				
				part = new LayoutPart(String.valueOf(c), 1);
				current = c;
			} else {
				if (part != null)
					part.length++;
			}
		}
		
		if (part != null)
			parts.add(part);
	}
	
	public List<LayoutPart> getParts() {
		return parts;
	}
	
	public String getLayoutString() {
		return layoutString.toString();
	}
}

/**
 * Classe auxiliar que define as partes do layout de acordo com o seu tipo
 * (num�rico ou alfanum�rico) e tamanho.
 * 
 * @author David Pereira
 *
 */
class LayoutPart {
	
	public String type;
	public int length;
	
	public LayoutPart(String type, int length) {
		this.type = type;
		this.length = length;
	}

	/**
	 * Define se a parte � num�rica ou n�o.
	 * @return
	 */
	public boolean isNumeric() {
		return "N".equals(type);
	}
	
}