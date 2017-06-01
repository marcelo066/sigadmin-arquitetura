/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 17/12/2008
 */
package br.ufrn.arq.seguranca.autenticacao;

import java.rmi.RemoteException;
import java.util.Date;

import br.ufrn.arq.dao.EmissaoDocumentoAutenticadoDao;
import br.ufrn.arq.dominio.Movimento;
import br.ufrn.arq.dominio.MovimentoCadastro;
import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.arq.negocio.AbstractProcessador;

/**
 * Processador respons�vel pela gera��o da emiss�o de documentos autenticados,
 * como hist�ricos, atestados de matr�cula, etc.
 * 
 * @author Gleydson Lima
 * 
 */
public class ProcessadorGeracaoEmissao extends AbstractProcessador {

	public Object execute(Movimento mov) throws NegocioException, ArqException,
			RemoteException {

		EmissaoDocumentoAutenticadoDao dao = getDAO(
				EmissaoDocumentoAutenticadoDao.class, mov);

		EmissaoDocumentoAutenticado emissao = ((MovimentoCadastro) mov)
				.getObjMovimentado();

		if ( emissao.isEmissaoDocumentoComNumero() ) {
			/* 
			 * Se tiver n�mero de documento, significa que o documento 
			 * j� deve ter tido uma emiss�o anterior, ent�o busca a emiss�o para 
			 * reaproveit�-la e gerar um novo registro
			 */ 
			
			// buscando emiss�o antiga
			EmissaoDocumentoAutenticado emissaoDocumento = dao.findByEmissaoSql(emissao
					.getIdentificador(), emissao.getCodigoSeguranca(), emissao
					.getTipoDocumento(), emissao.getSubTipoDocumento());
			
			if ( emissaoDocumento == null ) {
				emissao.setNumeroDocumento(dao.getNextSeq("numero_documento_emissao_seq") + "");
				dao.create(emissao);
				emissaoDocumento = emissao;
			}
			
			emissaoDocumento.setCodigoSeguranca(emissao.getCodigoSeguranca());
			RegistroEmissaoDocumentoAutenticado registro = criaRegistro(emissaoDocumento, mov);
			dao.create(registro);
			
			emissao = emissaoDocumento;
			
		} else {
			
			// � uma nova emiss�o e ter� tamb�m um novo registro
			dao.create(emissao);
			RegistroEmissaoDocumentoAutenticado registro = criaRegistro(emissao, mov);
			dao.create(registro);
			
		}
		
		dao.close();
		
		return emissao;

	}
	
	/*
	 * Cria um registro de emiss�o de documento autenticado, para indicar
	 * que um determinado documento que j� foi emitido est� sendo emitido novamente.
	 */
	private RegistroEmissaoDocumentoAutenticado criaRegistro(EmissaoDocumentoAutenticado emissao, Movimento mov) {
		MovimentoCadastro cmov = (MovimentoCadastro) mov;
		
		// cria novo registro de emiss�o
		RegistroEmissaoDocumentoAutenticado registro = new RegistroEmissaoDocumentoAutenticado();
		registro.setData(new Date());
		registro.setEmissao(emissao);
		
		if (mov.getUsuarioLogado() != null) {
			registro.setIdRegistroEntrada(cmov.getRegistroEntrada().getId());
		} else {
			registro.setIdRegistroAcessoPublico(cmov.getRegistroAcessoPublico().getId());
		}

		registro.setSistema(mov.getSistema());
		registro.setUsuario(mov.getUsuarioLogado());
		registro.setSistema(mov.getSistema());
		
		return registro;
		
	}

	public void validate(Movimento mov) throws NegocioException, ArqException {
		
	}
	
}
