/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 26/06/2009 
 */
package br.ufrn.arq.usuarios;

import javax.servlet.http.HttpServletRequest;

import br.ufrn.arq.erros.ArqException;
import br.ufrn.arq.erros.NegocioException;
import br.ufrn.comum.dominio.UsuarioGeral;

/**
 * Interface que define as estrat�fias de autentica��o dos sistemas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 */
public interface EstrategiaAutenticacao {

	/**
	 * Retorna o id do usu�rio cujo login foi passado como par�metro.
	 * @param req
	 * @param login
	 * @return
	 * @throws ArqException
	 */
	public Integer getIdUsuario(HttpServletRequest req, String login) throws ArqException;
	
	/**
	 * Autentica o usu�rio com o login e senha passados como par�metro.
	 * Retorna true se a autentica��o foi realizada, false caso contr�rio.
	 * @param req
	 * @param login
	 * @param senha
	 * @return
	 * @throws ArqException
	 */
	public boolean autenticaUsuario(HttpServletRequest req, String login, String senha) throws ArqException;
	
	/**
	 * Autentica o usu�rio com o login e senha passados como par�metro. 
	 * Retorna true se a autentica��o foi realizada, false caso contr�rio.
	 * Se o par�metro fazerHash for true, a senha ser� transformada em MD5.
	 * 
	 * @param req
	 * @param login
	 * @param senha
	 * @param fazerHash
	 * @return
	 * @throws ArqException
	 */
	public boolean autenticaUsuario(HttpServletRequest req, String login, String senha, boolean fazerHash) throws ArqException;

	/**
	 * Retorna true se a senha do usu�rio passado como par�metro expirou, false caso contr�rio.
	 * @param req
	 * @param idUsuario
	 * @return
	 * @throws ArqException
	 */
	public boolean senhaExpirou(HttpServletRequest req, int idUsuario) throws ArqException;

	/**
	 * Atualiza a senha do usu�rio cujo id foi passado como par�metro. � necess�rio passar a senha atual
	 * e a nova senha.
	 * @param req
	 * @param idUsuario
	 * @param senhaAtual
	 * @param novaSenha
	 * @throws ArqException
	 */
	public void atualizaSenhaAtual(HttpServletRequest req, int idUsuario, String senhaAtual, String novaSenha) throws ArqException;
	
	/**
	 * Retorna true se o existe um usu�rio mobile com o login passado como par�metro.
	 * @param req
	 * @param login
	 * @return
	 * @throws ArqException
	 */
	public boolean localizaUsuarioMobile(HttpServletRequest req, String login) throws ArqException;
	
	/**
	 * Autentica o usu�rio mobile com o login e senha passados como par�metro.
	 * Retorna true se a autentica��o foi realizada, false caso contr�rio.
	 * 
	 * @param req
	 * @param login
	 * @param senha
	 * @return
	 * @throws ArqException
	 */
	public boolean autenticaUsuarioMobile(HttpServletRequest req, String login, String senha) throws ArqException;

	/**
	 * Atualiza a senha do usu�rio mobile cujo id foi passado como par�metro.
	 * @param idUsuario
	 * @param senhaMobile
	 * @throws ArqException
	 */
	public void atualizaSenhaMobile(int idUsuario, String senhaMobile) throws ArqException;

	/**
	 * Carrega as permiss�es do usu�rio passado como par�metro.
	 * @param req
	 * @param usuario
	 * @throws ArqException
	 */
	public void carregaPermissoes(HttpServletRequest req, UsuarioGeral usuario) throws ArqException;

	/**
	 * Sincroniza os dados do usu�rio com o banco de dados. Utilizado
	 * quando a estrat�gia de autentica��o n�o utiliza os usu�rios
	 * cadastrados no banco de dados, e sim uma base externa (como um LDAP, por exemplo).
	 * @return
	 */
	public boolean sincronizarComBancoDados();
	
	/**
	 * Carrega as informa��es b�sicas do usu�rio cujo login foi passado como par�metro.
	 * @param req
	 * @param login
	 * @return
	 * @throws ArqException
	 */
	public InfoUsuarioDTO carregaInfoUsuario(HttpServletRequest req, String login) throws ArqException;

	/**
	 * Identifica se o usu�rio com id passado como par�metro est� atico no sistema
	 * cujo id foi passado como par�metro.
	 * @param req
	 * @param sistema
	 * @param idUsuario
	 * @return
	 */
	public boolean usuarioAtivo(HttpServletRequest req, int sistema, int idUsuario);

	/**
	 * 
	 * @param login
	 * @return
	 * @throws NegocioException
	 */
	public String validarLogin(String login) throws NegocioException;
	
}
