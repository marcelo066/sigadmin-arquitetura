/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 01/10/2004
 */
package br.ufrn.comum.dominio;

import java.util.Date;

import br.ufrn.arq.dominio.PersistDB;


/**
 * Classe de dom�nio que mapeia as permiss�es. Uma permiss�o faz o relacionamento entre um usu�rio e um papel.
 *
 * @author Andre
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public class Permissao implements PersistDB{

    protected int id;

    /** Papel associado a permiss�o */
    protected Papel papel;

    /** Usu�rio que possui a permiss�o */
    private UsuarioGeral usuario;

    /** Unidade para a qual a permiss�o � v�lida. Utilizada apenas em papeis cuja coluna exige_unidade for true. */
    private UnidadeGeral unidadePapel;

    /** Dados de quem cadastrou a permiss�o*/
    private UsuarioGeral usuarioCadastro;

    /** Data da cria��o da permiss�o */
    protected Date dataCadastro;

    /** Se for um papel tempor�rio, � a data em que a permiss�o deixa de ser v�lida */
    protected Date dataExpiracao;

    /** Sistema da permiss�o */
    private int sistema;

    /** N�mero do chamado que ocasionou a cria��o da permiss�o */
    private Integer numeroChamado;

    /** Motivos pelos quais o usu�rio solicitou a permiss�o. O que ele deseja utilizar no sistema e por que. */
    private String motivo;

    /** Se a permiss�o j� foi autorizada pelo gestor de permiss�es. */
    private boolean autorizada;

    /** Designa��o associada a permiss�o */
    private Integer designacao;
    
    /** Date de expira��o da Designa��o associada a permiss�o */
    protected Date dataExpiracaoDesignacao;
    
    public Permissao() {

    }

    public Permissao(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Papel getPapel() {
        return papel;
    }

    public void setPapel(Papel papel) {
        this.papel = papel;
    }

    public UsuarioGeral getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioGeral usuario) {
        this.usuario = usuario;
    }

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UsuarioGeral getUsuarioCadastro() {
		return usuarioCadastro;
	}

	public void setUsuarioCadastro(UsuarioGeral usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
	}

	public Date getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(Date dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public int getSistema() {
		return sistema;
	}

	public void setSistema(int sistema) {
		this.sistema = sistema;
	}

	public UnidadeGeral getUnidadePapel() {
		return unidadePapel;
	}

	public void setUnidadePapel(UnidadeGeral unidadePapel) {
		this.unidadePapel = unidadePapel;
	}

	public Integer getNumeroChamado() {
		return numeroChamado;
	}

	public void setNumeroChamado(Integer numeroChamado) {
		this.numeroChamado = numeroChamado;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public boolean isAutorizada() {
		return autorizada;
	}

	public void setAutorizada(boolean autorizada) {
		this.autorizada = autorizada;
	}
	
	public boolean isExpirada() {
		return dataExpiracao != null && dataExpiracao.before(new Date());
	}

	/**
	 * Verifica se esta permiss�o � v�lida para a unidade especificada.
	 * @param unidade
	 * @return
	 */
	public boolean permiteUnidade(UnidadeGeral unidade) {
		return unidadePapel == null || (unidadePapel != null && unidadePapel.getId() == unidade.getId());
	}

	/**
	 * Verifica se esta permiss�o � v�lida para a unidade especificada, considerando sua hierarquia.
	 * @param unidade
	 * @return
	 */
	public boolean permiteUnidadeHierarquia(UnidadeGeral unidade) {
		if(unidadePapel == null)
			return true;
		else if(unidade == null || unidade.getHierarquiaOrganizacional() == null)
			return false;
		else
			return unidade.getHierarquiaOrganizacional().contains("."+unidadePapel.getId()+".");
	}

	public Integer getDesignacao() {
		return designacao;
	}

	public void setDesignacao(Integer designacao) {
		this.designacao = designacao;
	}

	public Date getDataExpiracaoDesignacao() {
		return dataExpiracaoDesignacao;
	}

	public void setDataExpiracaoDesignacao(Date dataExpiracaoDesignacao) {
		this.dataExpiracaoDesignacao = dataExpiracaoDesignacao;
	}
}
