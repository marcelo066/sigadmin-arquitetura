/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 09/08/2005
 */
package br.ufrn.comum.dominio;

import java.util.Date;

import br.ufrn.arq.dominio.PersistDB;


/**
 * Classe de dom�nio que representa associa��o entre usu�rio e unidade. Serve
 * para modelar o relacionamento de um usu�rio que possui mais de uma unidade.
 *
 * @author Gleydson Lima
 *
 */
public class UsuarioUnidade implements PersistDB {

    protected int id;

    /** Usu�rio que possui as unidades */
    private UsuarioGeral usuario;

    /** Unidade associada ao usu�rio */
    private UnidadeGeral unidade;

    /** Usu�rio que realizou a associa��o */
    private UsuarioGeral usuarioCadastro;

    /** Data da associa��o */
    private Date dataCadastro;
    
    /** Id da responsabilidade de unidade, caso o v�nculo do usu�rio com a unidade
     * tenha sido feito pelo caso de uso de cadastrar responsabilidade de unidade
     * do SIGAdmin. Nesse caso, o v�nculo usuario-unidade vai valer enquanto a 
     * responsabilidade estiver ativa. */
    private Integer responsabilidadeUnidade;

    public UsuarioUnidade() {
        usuario = new UsuarioGeral();
        unidade = new UnidadeGeral();
    }

    public UsuarioUnidade(UsuarioGeral usuario, UnidadeGeral unidade) {
    	this.usuario = usuario;
    	this.unidade = unidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UnidadeGeral getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeGeral unidade) {
        this.unidade = unidade;
    }

    public UsuarioGeral getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioGeral usuario) {
        this.usuario = usuario;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioUnidade other = (UsuarioUnidade) obj;
		if (unidade == null) {
			if (other.unidade != null)
				return false;
		} else if (!unidade.equals(other.unidade))
			return false;
		return true;
	}

	/**
	 * @return the dataCadastro
	 */
	public Date getDataCadastro() {
		return dataCadastro;
	}

	/**
	 * @param dataCadastro the dataCadastro to set
	 */
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	/**
	 * @return the usarioCadastro
	 */
	public UsuarioGeral getUsuarioCadastro() {
		return usuarioCadastro;
	}

	/**
	 * @param usarioCadastro the usarioCadastro to set
	 */
	public void setUsuarioCadastro(UsuarioGeral usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Integer getResponsabilidadeUnidade() {
		return responsabilidadeUnidade;
	}

	public void setResponsabilidadeUnidade(Integer responsabilidadeUnidade) {
		this.responsabilidadeUnidade = responsabilidadeUnidade;
	}
	
}