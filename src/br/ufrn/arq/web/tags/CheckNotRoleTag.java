/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 06/11/2007
 */
package br.ufrn.arq.web.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Tag para verificar se um usu�rio n�o possui nenhum
 * dos pap�is passados como par�metro. Se possuir algum dos
 * pap�is, n�o exibe o conte�do.
 * 
 * @author Gleydson Lima
 *
 */
public class CheckNotRoleTag extends TagSupport {
	
	private UsuarioGeral usuario;
	
	private int papel = -1;
	
	private int[] papeis;
	
	@Override
	public int doStartTag() throws JspException {
		if (usuario == null)
			usuario = (UsuarioGeral)pageContext.getSession().getAttribute("usuario");
		
		if (usuario ==  null)
			return EVAL_BODY_INCLUDE;
		
		if (papel > 0)
			papeis = new int[] { papel };
		else if (papeis == null)
			throw new JspException("DEFINA UM PAPEL");
			
		int retorno = -1;
		if (usuario.isUserInRole(papeis))
			retorno = SKIP_BODY;
		else
			retorno = EVAL_BODY_INCLUDE;
		
		usuario = null;
		
		return retorno;
	}
	
	@Override
	public void release() {
		papel = -1;
		papeis = null;
		super.release();
	}

	public int[] getPapeis() {
		return papeis;
	}

	public void setPapeis(int[] papeis) {
		this.papeis = papeis;
	}

	public int getPapel() {
		return papel;
	}

	public void setPapel(int papel) {
		this.papel = papel;
	}

	public UsuarioGeral getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioGeral usuario) {
		this.usuario = usuario;
	}

}
