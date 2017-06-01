/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 06/11/2007
 */
package br.ufrn.arq.web.tags;

/**
 * Tag para exibir um fomul�rio html com campos hidden
 * adicionais para navega��o em struts.
 *  
 * @author Gleydson Lima
 *
 */
public class FormTag extends org.apache.struts.taglib.html.FormTag {

	@Override
	protected String renderFormStartElement() {
		String pai = super.renderFormStartElement();
		String dispatch = "<input type=\"hidden\" name=\"dispatch\"  id=\"dispatch\">";
		String view = "<input type=\"hidden\" name=\"view\" value=\"\" id=\"view\">";

		return pai + dispatch + view;
	}

}
