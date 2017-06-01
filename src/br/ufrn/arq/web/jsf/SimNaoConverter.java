/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 19/09/2008
 */
package br.ufrn.arq.web.jsf;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

/**
 * Conversor de um boolean em uma String Sim ou N�o e vice versa
 * 
 * @author Raphaela Galhardo
 * @author Gleydson Lima
 */
public class SimNaoConverter implements Converter{

	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) throws ConverterException {

		if (value == null || value.trim().equals("")){
			value = "N�o";
		}
		
		if (value.equals("Sim"))
			return true;
		
		return false;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) throws ConverterException {

		Boolean valor = (Boolean)value;
		
		if (valor.equals(Boolean.TRUE))
			return "Sim";
		
		return "N�o";
	}

}


