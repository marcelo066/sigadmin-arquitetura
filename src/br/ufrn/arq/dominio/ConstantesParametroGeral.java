/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 06/07/2006
 */
package br.ufrn.arq.dominio;

import br.ufrn.arq.seguranca.log.SessionLogger;

/**
 * Constantes gerais de par�metros utilizados pela arquitetura.
 * 
 * @author Gleydson Lima
 *
 */
public class ConstantesParametroGeral {

	 /** Indica se mensagens devem ser enviadas quando uma exce��o for disparada */
    public static final String EMAIL_DE_ALERTA = "3_1_15";

    /** Email do administrador do sistema para envio de alertas */
    public static final String EMAIL_ALERTAS_ADMISTRADOR = "3_1_2";

    /** Email do administrador do sistema para envio de alertas */
    public static final String EMAIL_ALERTA_ERRO = "3_1_3";

    /** Caminho para recursos no NFS */
	public static final String CAMINHO_RECURSOS = "3_1_4";

	/** Indica se a institui��o est� usando LDAP para sincroniza��o dos usu�rios. */
	public static final String LDAP_ATIVO = "3_1_9";
	
	/**Enviar e-mail quando ocorre alguma exce��o no sipac*/ 
	 public static final String EXCECAO_SIPAC = "1_1_1";
	 
	/**Enviar e-mail quando ocorre alguma exce��o no sigaa*/ 
	 public static final String EXCECAO_SIGAA = "2_1_1";

	/**Enviar e-mail quando ocorre alguma exce��o na arquitetura*/ 
	 public static final String EXCECAO_ARQ = "3_1_15";

	/**Enviar e-mail quando ocorre alguma exce��o no SCO*/ 
	 public static final String EXCECAO_SCO = "4_1_1";

	/**Enviar e-mail quando ocorre alguma exce��o no sistema de protocolo*/ 
	 public static final String EXCECAO_PROTOCOLO = "5_1_1";

	/**Enviar e-mail quando ocorre alguma exce��o no iProject*/ 
	 public static final String EXCECAO_IPROJECT = "6_1_1";

	/**Enviar e-mail quando ocorre alguma exce��o no sigrh*/ 
	 public static final String EXCECAO_SIGRH = "7_1_1";


	/**Enviar e-mail quando ocorre alguma exce��o no sigadmin*/ 
	 public static final String EXCECAO_SIGADMIN = "8_1_1";

	 
	/**
	 * Identifica o remetente padr�o de uma notifica��o gerada pelo SIGAdmin.
	 */
	public static final String REMETENTE_PADRAO_NOTIFICACAO = "8_1_2";
	 
	 
    /**
	 * Id da pessoa do gestor da institui��o (reitor UFRN) na tabela pessoa.
	 */
	public static final String GESTOR_INSTITUICAO = "3_1_10";
	
	
	/**
	 * Id da pessoa do superintendente da funda��o na tabela pessoa.
	 */
	public static final String SUPERINTENDENTE_FUNDACAO = "3_1_11";

	
	/** 
	 * Indica o limite m�ximo de dias que um servidor pode ficar sem ler um memorando eletr�nico recebido.
	 * Ap�s este limite o memorando eletr�nico deve receber uma identifica��o e o usu�rio deve ser informado para que leia o mais r�pido poss�vel.
	 */
	public static final String LIMITE_DIAS_LEITURA_MEMORANDO_ELETRONICO = "3_1_12";
	
	/**
	 * Indica se um aluno � priorit�rio de acordo com o cadastro �nico
	 */
	public static final String ALUNO_PRIORITARIO = "3_1_13";

	/**
	 * Endere�o de reply-to para mensagens enviadas pelo SIPAC.
	 */
	public static final String REPLYTO_SIPAC = "3_1_14";

	/**
	 * Estrat�gia de autentica��o para os sistemas
	 */
	public static final String ESTRATEGIA_AUTENTICACAO = "3_1_16";

	/**
	 * Prazo para a autoriza��o de projetos de pesquisa, monitoria e extens�o. Se n�o for
	 * autorizado dentro do prazo, as requisi��es ficam bloqueadas na unidade.
	 */
	public static final String PRAZO_AUTORIZACAO_PROJETOS = "3_1_17";

	/** Tempo necess�rio para a expira��o de um passaporte de logon */
	public static final String TEMPO_EXPIRACAO_PASSAPORTE = "3_1_18";
	
	/** Unidade de direito global */
	public static final String UNIDADE_DIREITO_GLOBAL = "1_0_1";
	
	/** C�digo da institui��o */
	public static final String CODIGO_INSTITUICAO = "1_0_14";
	
	/** Endere�o do host SMTP para envio de e-mails */
	public static final String EMAIL_AUTOMATICO_HOST = "3_1_20";
	
	/** Usu�rio de autentica��o no host SMTP */
	public static final String EMAIL_AUTOMATICO_HOST_USUARIO = "3_1_21";
	
	/** Senha do usu�rio de autentica��o no host SMTP */
	public static final String EMAIL_AUTOMATICO_HOST_SENHA = "3_1_22";
	
	/** Endere�o de e-mail do FROM */
	public static final String EMAIL_AUTOMATICO_REMETENTE_ENDERECO = "3_1_23";
	
	/** Nome do endere�o de e-mail do FROM */
	public static final String EMAIL_AUTOMATICO_REMETENTE_NOME = "3_1_24";
	
	/** Endere�o de e-mail do REPLY TO */
	public static final String EMAIL_AUTOMATICO_RESPONDER_PARA_ENDERECO = "3_1_25";
	
	/** Nome do endere�o de e-mail do REPLY TO */
	public static final String EMAIL_AUTOMATICO_RESPONDER_PARA_NOME = "3_1_26";
	
	/** Tamanho m�ximo (em bytes) de um arquivo anexado a um chamado */
	public static final String TAMANHO_MAX_ARQUIVO_CHAMADO = "3_1_27";
	
	/** N�mero m�ximo de requisi��es concorrentes por usu�rio */
	public static final String NUMERO_MAXIMO_REQUISICOES_CONCORRENTES = "3_1_28";

	/** Utilizada para defini��o da unidade acad�mica de alunos especiais de gradua��o */
	public static final String UNIDADE_ACADEMICA_ALUNO_ESPECIAL = "3_1_29";
	
	/** Par�metro que guarda usu�rio e senha para o web service de integra��o com o SIGEO (MJ) */
	public static final String CREDENCIAIS_AUTENTICACAO_INTEGRACAO_SIGEO = "3_1_30";

	/** Host para recebimento de e-mails pela arquitetura */
	public static final String HOST_RECEBIMENTO_EMAIL = "3_1_31";
	
	/** Usu�rio para recebimento de e-mails pela arquitetura */
	public static final String USERNAME_RECEBIMENTO_EMAIL = "3_1_32";
	
	/** Senha para recebimento de e-mails pela arquitetura */
	public static final String PASSWORD_RECEBIMENTO_EMAIL = "3_1_33";

	/** Par�metro que armazena a escola de m�sica */
	public static final String ESCOLA_MUSICA = "3_1_34";
	
	/** Permitir a cria��o de usu�rios independente do tipo de unidade(gestora/fato) **/
	public static final String PERMITE_CRIAR_USUARIO_UNIDADE_GESTORA = "1_12000_5";
	
	/** Unidade a qual o usu�rio de coopera��o ser� vinculado. **/
	public static final String UNIDADE_USUARIO_COOPERACAO = "1_12000_6";
	
	/** Quantidade de dias em que a senha dos usu�rio devem expirar ap�s a altera��o */
	public static final String EXPIRA_SENHA = "1_0_2";
	
	/** Indica a quantidade m�nima de memorandos exibidos no painel de memorandos. */
	public static final String QUANTIDADE_MINIMA_PAINEL_MEMORANDOS = "3_1_35";
	
	/** Define se � Permitido consultar um servidor em qualquer busca com qualquer parte do nome. */ 
	public static final String PERMITE_CONSULTA_SERVIDOR_QUALQUER_PARTE_NOME = "7_100500_1"; //Servidor (Funcional)
	
	/** Indica se o sistema realiza a gera��o de matr�cula para servidores internamente 
	 * (al�m de utilizar a matr�cula SIAPE) e permite utlizar/exibir esta matr�cula em consultas, relat�rios, etc. */
	public static final String UTILIZA_CONCEITO_MATRICULA_INTERNA = "7_100500_3"; //Servidor (Funcional)

	/** Unidade pagadora de bolsas de extens�o. */
	public static final String UNIDADE_BOLSAS_EXTENSAO = "2_10900_6";

	/** Unidade pagadora de bolsas de monitoria.*/
	public static final String UNIDADE_BOLSAS_MONITORIA = "2_11000_8";

	/** Unidade da Secretaria de Educa��o a Dist�ncia.*/
	public static final String UNIDADE_SEDIS = "2_11200_1";

	/** Unidade da Se��o de Atendimento do DAP.*/
	public static final String SECAO_ATENDIMENTO_DAP = "7_100100_125";

	/** Unidade do Departamento de Administra��o Pessoal.*/
	public static final String DEPARTAMENTO_ADMINISTRACAO_PESSOAL = "7_100100_124";

	/** Indica a unidade respons�vel por realizar os treinamentos dos servidores da institui��o em cursos de capacita��o profissional.*/
	public static final String ID_UNIDADE_DIVISAO_DE_TREINAMENTO_E_DESENVOLVIMENTO_DE_RH = "7_100200_1";

	/** Unidade do Centro de Ensino Superior do Serid�.*/
	public static final String UNIDADE_CERES = "2_13000_1";

	/** Se a base de log ser� replicada ou n�o (booleano) */
	public static final String REPLICAR_BASE_LOGS = "3_1_38";

	/** Par�metro contendo o caminho para o arquivo de log da sess�o do Hibernate */
	public static final String CAMINHO_LOG_SESSAO = "3_1_39";

	/** Tempo m�ximo que uma sess�o fica aberta antes de ser logada pelo {@link SessionLogger} */
	public static final String TEMPO_MAXIMO_SESSAO_ABERTA = "3_1_40";
	
	/** Os valores poss�veis s�o: ENTRADA, MOSTRAR_NAO_LIDAS. No caso do parametro ser mostrar lidas o sistema n�o 
	 * deve redirecionar para a caixa postal e ir direto para o menu mostrando o total de mensagens n�o lidas ao 
	 * lado do �cone da caixa postal no cabe�alho. */
	public static final String MODO_OPERACAO_CAIXA_POSTAL = "3_1_41";
	
	/**
	 * Indica se a unidade do usu�rio que ser� utilizada � a de lota��o/fun��o ou a unidade j� definida.
	 * Neste caso, se o par�metro tiver true, o sistema define a unidade do usu�rio para a unidade de lota��o ou da fun��o. A fun��o tem prioridade sobre a lota��o.
	 */
	public static final String UTILIZA_UNIDADE_LOTACAO_COMO_UNIDADE_USUARIO  = "3_1_42";

	/**
	 * Endere�o de e-mail padr�o para resposta dos e-mails enviados pelos sistemas.
	 */
	public static final String DEFAULT_REPLY_TO = "3_1_43";

	
	/**
	 * Este param�tro mapeia os IDs das atividades (rh.atividade) que representam a atividade de chefe de departamento.
	 */
	public static final String ATIVIDADES_CHEFE_DEPARTAMENTO = "3_1_44";
	
	/**
	 * Este param�tro mapeia os IDs das atividades (rh.atividade) que representam a atividade de diretor de centro.
	 */
	public static final String ATIVIDADES_DIRETOR_CENTRO = "3_1_45";
	
	/**
	 * Este param�tro mapeia os IDs das atividades (rh.atividade) que representam a atividade de vice-diretor de centro.
	 */
	public static final String ATIVIDADES_VICE_DIRETOR_CENTRO = "3_1_46";
	
	/**
	 * Este param�tro mapeia os IDs das atividades (rh.atividade) que representam a atividade de diretor de unidade.
	 */
	public static final String ATIVIDADES_DIRETOR_UNIDADE = "3_1_47";
	
	/**
	 * Este param�tro mapeia os IDs das atividades (rh.atividade) que representam a atividade de diretor de museu.
	 */
	public static final String ATIVIDADES_DIRETOR_MUSEU = "3_1_48";
	
	/**
	 * Forma de exibi��o das unidades na �rvore de unidades.
	 * 'S', 'N' -> SIGLA OU NOME COMPLETO.
	 */
	public static final String ARVORE_UNIDADES_FORMA_EXIBICAO = "3_1_49";

	/**
	 * Salt utilizado para fazer o hash das senhas dos usu�rios. Para n�o
	 * usar salt, deixar o par�metro null.
	 */
	public static final String SALT_SENHAS_USUARIOS = "3_1_50";

	/**
	 * Tamanho m�ximo da imagem que pode ser enviada atrav�s do editor TinyMCE.
	 */
	public static final String TAMANHO_MAXIMO_UPLOAD_IMAGEM_TINYMCE = "3_1_51";
	
	
	/**
	 * id da UF (Unidade Federativa, tabela: comum.unidade_federativa padr�o a ser utilizado nos sistemas, nos formul�rios de cadastro de dados pessoais e outros.. 
	 */
	public static final String UF_PADRAO = "3_1_52";
	
	/**
	 * Id do Munic�pio (tabela comum.municipio) padr�o a ser utilizado nos sistemas, nos formul�rios de cadastro de dados pessoais e outros.. 
	 */
	public static final String MUNICIPIO_PADRAO = "3_1_53";
	
	/**
	 * CEP padr�o a ser utilizado nos sistemas, nos formul�rios de cadastro de dados pessoais e outros.. 
	 */
	public static final String CEP_PADRAO = "3_1_54";
	
	/**
	 * DDD padr�o a ser utilizado nos sistemas, nos formul�rios de cadastro de dados pessoais e outros.. 
	 */
	public static final String DDD_PADRAO = "3_1_55";
	
	/**
	 * usu�rio utilizado para opera��es autom�ticas e rotinas de migra��o
	 * onde eh necess�rio registrar o usu�rio que realizou a opera��o.
	 */
	public static final String USUARIO_SISTEMA = "3_1_56";
	
	/**
	 * usu�rio utilizado para opera��es realizadas dentro de um timer
	 */
	public static final String TIMER_SISTEMA = "3_1_57";

	/**
	 * Indica se o envio de e-mails pela arquitetura deve usar SSL.
	 */
	public static final String EMAIL_AUTOMATICO_USAR_SSL = "3_1_58";

	/**
	 * Indica a porta do servidor SMTP para envio de e-mails.
	 */
	public static final String EMAIL_AUTOMATICO_PORTA_SMTP = "3_1_59";
	
	/** Par�metro contendo todas as configura��es necess�rias para o envio de e-mails pelos sistemas. */
	public static final String CONFIGURACOES_ENVIO_EMAIL = "3_1_60";

	/**
	 * Identifica se deve ser registrado no banco de dados o nome do m�todo de um DAO
	 * que est� realizando a intera��o com o banco.
	 */
	public static final String REGISTRAR_METODO_INVOCADOR_SESSAO = "3_1_61";

	/** Dura��o dos caches do componente de �rvore de unidades, em minutos. */
	public static final String ARVORE_UNIDADES_DURACAO_CACHE = "3_1_62";
	
}
