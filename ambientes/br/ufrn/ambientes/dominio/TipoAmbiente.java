/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 16/04/2010
 */
package br.ufrn.ambientes.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.ufrn.arq.dao.CampoAtivo;
import br.ufrn.arq.dominio.PersistDB;
import br.ufrn.arq.negocio.validacao.annotations.Required;

/**
 * Utilizada para registrar os poss�veis tipos de ambientes
 * (que n�o o de produ��o) que podem ter a seguran�a refor�ada
 * para usu�rios externos.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
@Entity @Table(name="tipo_ambiente", schema="comum")
public class TipoAmbiente implements PersistDB {

	public static final int DESENVOLVIMENTO = 1;
	public static final int TREINAMENTO = 2;
	public static final int PRODUCAO = 3;
	public static final int HOMOLOGACAO = 4;

	@Id
	@GeneratedValue(generator="seqGenerator")
	@GenericGenerator(name="seqGenerator", strategy="br.ufrn.arq.dao.SequenceStyleGenerator",
	           parameters={ @Parameter(name="sequence_name", value="comum.tipo_ambiente_seq") })
	private int id;
	
	/** Nome do ambiente */
	@Required
	private String nome;
	
	/** Se o ambiente est� ativo ou n�o */
	@CampoAtivo
	private boolean ativo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	
}
