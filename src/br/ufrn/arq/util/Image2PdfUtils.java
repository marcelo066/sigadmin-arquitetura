package br.ufrn.arq.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Classe utilitária para conversão de arquivos de imagem em PDF.
 * 
 * @author Raphael Medeiros
 *
 */
public class Image2PdfUtils {

	/**
	 * Método responsável por converter uma imagem em um arquivo PDF.
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static File convert(File image) throws IOException, DocumentException {
		Document document = new Document(PageSize.A4, 0f, 0f, 0f, 0f);
		File tmp = File.createTempFile("import" + System.currentTimeMillis(), ".pdf");

	    FileOutputStream fo = null;
	    boolean isDone = false;

	    try {
	    	fo = new FileOutputStream(tmp);
	    	
	    	PdfWriter.getInstance(document, fo);
	    	
	    	document.open();

	    	Image jpg = Image.getInstance(image.getAbsolutePath());

	    	int width = (int) document.getPageSize().getWidth();
	    	int height = (int) document.getPageSize().getHeight();
	    	
	    	byte[] bytesArquivo = FileUtils.readFileToByteArray(image);
	        byte[] resized = UFRNUtils.redimensionaProporcional(bytesArquivo, width, height);
	    	
	    	jpg = Image.getInstance(resized);
	    	
	    	jpg.setAlignment(Element.ALIGN_CENTER);
	    	
	    	document.add(jpg);
	    	document.close();

	    	isDone = true;
	    } finally {
	    	if (fo != null) fo.close();
	    	if (!isDone) tmp.delete();
	    }

	    return tmp;
	}
}
