/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 05/01/2009
 */
package br.ufrn.arq.web.jsf;

import javax.faces.model.SelectItem;

/**
 * SelectItem Orden�vel
 * 
 * @author Gleydson Lima
 *
 */
public class OrderedSelectItem extends SelectItem implements Comparable<OrderedSelectItem> {

	public OrderedSelectItem(String id, String value) {
		super(id,value);
	}

	public int compareTo(OrderedSelectItem o) {

		try {
			if (o.getValue() instanceof String) {
				return (getLabel()).compareTo(o.getLabel());
			} else {
				return 0;
			}
		} catch (Exception e) {
			return 0;
		}
	}
	
	public SelectItem toSelectItem() {
		return new SelectItem(getValue(),getLabel());
	}

}
