/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 06/11/2007
 */
package br.ufrn.arq.web.tags;

import java.util.Collection;

import javax.servlet.jsp.JspException;

/**
 * 
 * Para obter o tamanho da cole��o
 * 
 * @author Gleydson Lima
 * 
 */
public class ColSizeTag extends FormatTag {

	public int doStartTag() throws JspException {

		Object obj = null;

		try {
			if (getScope() != null) {
				if (getScope().compareTo("session") == 0) {
					obj = pageContext.getSession().getAttribute(getName());
				}

				if (getScope().compareTo("request") == 0) {
					obj = pageContext.findAttribute(getName());
				}

			} else if (getName() != null) {
				obj = pageContext.findAttribute(getName());
			}

			if (obj == null) {
				return super.doStartTag();
			}

			pageContext.getOut().print(((Collection<?>) obj).size());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return SKIP_BODY;

	}

}
