/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 05/10/2004
 */
package br.ufrn.arq.dominio;

import static br.ufrn.arq.util.ValidatorUtil.isEmpty;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import br.ufrn.arq.negocio.validacao.ListaMensagens;
import br.ufrn.comum.dominio.CasoUso;
import br.ufrn.comum.dominio.Sistema;
import br.ufrn.comum.dominio.SubSistema;


/**
 * Esta classe representa a classe de param�tros do SIPAC. Esses par�metros tem
 * uma rela��o com algumas regras de neg�cio do sistema.
 * 
 * Os par�metros s�o armazenados na base dados sistemas_comum.
 * 
 * @author Ricardo Alexsandro de Medeiros Valentim
 * @author David Pereira
 * @author Gleydson Lima
 */
public class Parametro {

    /** C�digo que representa o par�metro. */
    private String codigo;

    /** Identifica o sistema ao qual o par�metro pertence */
    private Sistema sistema;
    
    /** Identifica o subsistema ao qual o par�metro pertence */
    private SubSistema subSistema;
    
    /** Casos de uso que utilizam este par�metro */
    private List<CasoUso> casosUso;

    /** Nome do par�metro. Nome da constante no sistema. */
    private String nome;

    /** Valor atribu�do ao par�metro */
    private String valor;

    /** Descri��o da utilidade do par�metro no sistema.*/
    private String descricao;
    
    /** Tempo m�ximo no qual o par�metro fica em cache antes de ser recarregado. */
    private Integer tempoMaximo;
    
    /** Tipo de dados do par�metro. Utilizado para validar o valor digitado. */
    private Integer tipo;
    
    /** Se o tipo de dados for uma String, ou uma data, define o padr�o de formato desse dado. */
    private String padrao;
    
    /** Valor m�nimo permitido para o par�metro, caso ele seja do tipo num�rico. */
    private BigDecimal valorMinimo;
    
    /** Valor m�ximo permitido para o par�metro, caso ele seja do tipo num�rico. */
    private BigDecimal valorMaximo;

    public ListaMensagens validaValor() {
    	ListaMensagens erros = new ListaMensagens();
    	
    	if (tipo != null && !isEmpty(valor)) {
    		switch(tipo) {
    		case Types.BOOLEAN:
    			if (!"true".equalsIgnoreCase(valor.trim()) && !"false".equalsIgnoreCase(valor.trim())) {
    				erros.addErro("Valor inv�lido para par�metro do tipo Boolean. Deve ser \"true\" ou \"false\".");
    			}
    			break;
    		case Types.VARCHAR:
    			if (padrao != null) {
    				try {
    					Pattern pattern = Pattern.compile(padrao);
    					Matcher matcher = pattern.matcher(valor);
    					if (!matcher.find()) {
    						erros.addErro("Valor inv�lido para par�metro do tipo String. Deve estar de acordo com a express�o regular " + padrao + ".");
    					}
    				} catch(PatternSyntaxException e) {
    					erros.addErro("Express�o regular de valida��o inv�lida.");
    				}
    			}

    			break;
    		case Types.INTEGER:
    			if (valorMinimo != null) {
    				long valorLong = 0;
    				
    				try {
    					valorLong = Long.parseLong(valor);
    				} catch(NumberFormatException e) {
    					erros.addErro("Valor inv�lido para par�metro. Deve ser um valor num�rico inteiro.");
    					break;
    				}
    				
    				if (valorMinimo.compareTo(new BigDecimal(valorLong)) > 0) {
    					erros.addErro("Valor inv�lido para par�metro. O menor valor poss�vel � " + valorMinimo + ".");
    				}
    			}
    			if (valorMaximo != null) {
    				if (valorMaximo.compareTo(new BigDecimal(Long.parseLong(valor))) < 0) {
    					erros.addErro("Valor inv�lido para par�metro. O maior valor poss�vel � " + valorMaximo + ".");
    				}
    			}
    			break;
    		case Types.DECIMAL:
    			double valorDouble = 0;
				
				try {
					valorDouble = Double.parseDouble(valor);
				} catch(NumberFormatException e) {
					erros.addErro("Valor inv�lido para par�metro. Deve ser um valor num�rico de ponto flutuante.");
					break;
				}
				
    			if (valorMinimo != null) {
    				if (valorMinimo.compareTo(new BigDecimal(valorDouble)) > 0) {
    					erros.addErro("Valor inv�lido para par�metro. O menor valor poss�vel � " + valorMinimo + ".");
    				}
    			}
    			if (valorMaximo != null) {
    				if (valorMaximo.compareTo(new BigDecimal(valorDouble)) < 0) {
    					erros.addErro("Valor inv�lido para par�metro. O maior valor poss�vel � " + valorMaximo + ".");
    				}
    			}
    			break;
    		case Types.DATE:
    			if (padrao != null) {
    				try {
    					SimpleDateFormat sdf = new SimpleDateFormat(padrao);
    					sdf.parse(valor);
    				} catch(IllegalArgumentException e) {
    					erros.addErro("Padr�o de data inv�lido.");
    				} catch (ParseException e) {
    					erros.addErro("Valor inv�lido para par�metro. A data deve seguir o padr�o " + padrao + ".");
					}
    			}
    			break;
    		}
		}
    	
    	return erros;
    }
    
	public SubSistema getSubSistema() {
		return subSistema;
	}

	public void setSubSistema(SubSistema subSistema) {
		this.subSistema = subSistema;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getTempoMaximo() {
		return tempoMaximo;
	}

	public void setTempoMaximo(Integer tempoMaximo) {
		this.tempoMaximo = tempoMaximo;
	}

	public List<CasoUso> getCasosUso() {
		return casosUso;
	}

	public void setCasosUso(List<CasoUso> casosUso) {
		this.casosUso = casosUso;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getPadrao() {
		return padrao;
	}

	public void setPadrao(String padrao) {
		this.padrao = padrao;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
	
}