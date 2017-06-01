/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 18/06/2009
 */
package br.ufrn.arq.mensagens;

/**
 * Classe com constantes que fazem refer�ncia a mensagens 
 * da arquitetura que ser�o exibidas em todos os sistemas.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 *
 */
public interface MensagensArquitetura {

	/** Opera��o realizada com sucesso */
	public static final String OPERACAO_SUCESSO = "3_0_1";
	
	/** N�o h� objeto informado para remo��o */
	public static final String NAO_HA_OBJETO_REMOCAO = "3_0_2";

	/** O objeto selecionado foi removido previamente. */
	public static final String OBJETO_SELECIONADO_FOI_REMOVIDO = "3_0_3";
	
	/** Objeto cadastrado com sucesso. Mensagem aceita um par�metro, que � colocado no lugar de "Objeto". */
	public static final String CADASTRADO_COM_SUCESSO = "3_0_4";
	
	/** Objeto alterado com sucesso. Mensagem aceita um par�metro, que � colocado no lugar de "Objeto". */
	public static final String ALTERADO_COM_SUCESSO = "3_0_5";
	
	/** Mensagem exibida quando se tenta fazer uma busca e n�o se obtem resultados. */
	public static final String BUSCA_SEM_RESULTADOS = "3_0_6";
	
	/** 
	 * Mensagem exibida quando se tenta fazer uma opera��o por�m o usu�rio n�o informou um campo obrigat�rio. Aceita um par�metro
	 * que corresponde ao nome do campo 
	 */	
	public static final String CAMPO_OBRIGATORIO_NAO_INFORMADO = "3_0_7";
	
	/**
	 * Mensagem que informa ao usu�rio que a remo��o de algum PersistDB foi efetuada com sucesso.
	 * Aceita um par�metro que corresponde ao nome do objeto removido.
	 */
	public static final String REMOCAO_EFETUADA_COM_SUCESSO = "3_0_8";

	/**
	 * Informa que um determinado campo de um formul�rio est� em um formato inv�lido. Recebe
	 * como par�metro o nome do campo.
	 */
	public static final String FORMATO_INVALIDO = "3_0_9";

	/**
	 * Utilizado em campos num�ricos. Mensagem informa que um campo deve ser maior que zero. Recebe
	 * como par�metro o nome do campo.
	 */
	public static final String VALOR_MAIOR_ZERO = "3_0_10";
	
	/**
	 * Utilizado em campos num�ricos. Mensagem informa que um campo deve ser maior ou igual a zero. Recebe
	 * como par�metro o nome do campo.
	 */
	public static final String VALOR_MAIOR_IGUAL_ZERO = "3_0_11";
	
	/**
	 * Utilizado em campos num�ricos. Mensagem informa que um campo deve ser maior ou igual a um valor
	 * passado como par�metro. Recebe como par�metro o nome do campo e o valor.
	 */
	public static final String VALOR_MAIOR_IGUAL_A = "3_0_12";
	
	/**
	 * Utilizado em campos num�ricos. Mensagem informa que um campo deve ser menor ou igual a um valor
	 * passado como par�metro. Recebe como par�metro o nome do campo e o valor.
	 */
	public static final String VALOR_MENOR_IGUAL_A = "3_0_13";
	
	/**
	 * Utilizado em campos num�ricos. Mensagem informa que um campo deve ser maior ou igual a um valor
	 * passado como par�metro e menor ou igual a outro par�metro. Recebe como par�metro o nome do campo 
	 * e os valores do intervalo.
	 */
	public static final String VALOR_MAIOR_QUE_MENOR_QUE = "3_0_14";
	
	/**
	 * Utilizado em campos do tipo data. Informa que a data deve ser posterior a um valor
	 * passado como par�metro.
	 */
	public static final String DATA_POSTERIOR_A = "3_0_15";
	
	/**
	 * Utilizado em campos do tipo data. Informa que a data deve ser anterior a um valor
	 * passado como par�metro.
	 */
	public static final String DATA_ANTERIOR_A = "3_0_16";

	/**
	 * Utilizado em campos do tipo data. Informa que a data deve ser anterior a um valor
	 * passado como par�metro e posterior a outro par�metro.
	 */
	public static final String DATA_POSTERIOR_ANTERIOR_A = "3_0_17";
	
	/**
	 * Utilizado em campos do tipo data. Informa que a data deve ser menor que a data de fim
	 * existente no formul�rio.
	 */
	public static final String DATA_INICIO_MENOR_FIM = "3_0_18";
	
	/** Informa que o conte�do de um campo � inv�lido. Recebe como par�metro o nome do campo. */
	public static final String CONTEUDO_INVALIDO = "3_0_19";
	
	/**
	 * Em um campo do tipo String, informa quando o n�mero de caracteres supera o m�ximo. Recebe
	 * como par�metros o nome do campo e o seu tamanho m�ximo.
	 */
	public static final String MAXIMO_CARACTERES = "3_0_20";

	/** Informa que o campo n�o pode conter abrevia��es. Recebe como par�metro o nome do campo. */
	public static final String CAMPO_SEM_ABREVIACOES = "3_0_21";

	/** Informa que um campo do tipo autocomplete � obrigat�rio. Recebe como par�metro o nome do campo. */
	public static final String AUTOCOMPLETE_OBRIGATORIO = "3_0_22";
	
	/** A data de nascimento n�o pode ser do ano passado como par�metro. */
	public static final String DATA_NASCIMENTO_DIFERENTE_ANO = "3_0_23";
	
	/** A p�gina inicial deve ser maior que a final. */
	public static final String PAGINA_INICIAL_MAIOR_FINAL = "3_0_24";

	/**
	 * Em um campo do tipo String, informa quando o n�mero de caracteres � inferior ao m�nimo. Recebe
	 * como par�metros o nome do campo e o seu tamanho m�nimo.
	 */
	public static final String MINIMO_CARACTERES = "3_0_25";
	
	/**
	 * Utilizado quando ocorre a tentativa de remo��o de um objeto que est� associado a outros registro na base de dados.
	 */
	public static final String REMOCAO_OBJETO_ASSOCIADO = "3_0_26";
	
	/**
	 * Informa que objeto que est� sendo cadastrado j� foi cadastrado anteriormente. Recebe como par�metro o nome da classe em quest�o. 
	 */
	public static final String OBJETO_JA_CADASTRADO = "3_0_27";
	
	/**
	 * Informa quando o n�mero de d�gitos � inferior ao m�nimo. Recebe
	 * como par�metros o nome do campo e o seu tamanho m�nimo.
	 */
	public static final String MINIMO_DIGITOS = "3_0_28";
	
	/**
	 * Informa quando o n�mero de d�gitos supera o m�ximo. Recebe
	 * como par�metros o nome do campo e o seu tamanho m�ximo.
	 */
	public static final String MAXIMO_DIGITOS = "3_0_29";

	/**
	 * Informa quando o procedimento j� foi processado anteriormente.
	 * 
	 * Conte�do: O procedimento que voc� tentou realizar j� foi processado anteriormente. Para realiz�-lo novamente, reinicie o processo utilizando os links oferecidos pelo sistema.
	 */
	public static final String PROCEDIMENTO_PROCESSADO_ANTERIORMENTE = "3_0_30";
	
	/**
	 * Informa quando o usu�rio deve selecionar pelo menos uma das op��es em uma busca.
	 * 
	 * Conte�do: Selecione uma das op��es para a busca.
	 */
	public static final String SELECIONE_OPCAO_BUSCA = "3_0_31";
	
	/**
	 * Informa quando dois campos devem ter o mesmo conte�do. Recebe como par�metro os dois nomes dos campos correpondentes.
	 * 
	 * Conte�do: Os campos %s e %s devem ser iguais.
	 */
	public static final String CAMPOS_IGUAIS = "3_0_32";
	
	/**
	 * Informa quando j� foi executada uma a��o em determinado item. Recebe como par�metro o item em quest�o e o verbo na 
	 * conjuga��o Mais-que-perfeito. Ex: Esse(a) conv�nio j� foi cadastrado anteriormente.
	 * 
	 * Conte�do: Esse(a) %s j� foi %s anteriormente.
	 */
	public static final String ACAO_JA_EXECUTADA = "3_0_33";
	
	/**
	 * Informa que o uma busca excedeu o n�mero m�ximo de resultados e deve ser refinada. Recebe como par�metro o n�mero m�ximo de resultado.
	 * Conte�do: A busca excedeu o n�mero m�ximo de %s resultados. Favor refine a sua busca.
	 */
	public static final String BUSCA_MAXIMO_RESULTADOS = "3_0_34";

	/**
	 * Informa que o uma URL informada n�o possui um formato v�lido. Recebe
	 * como par�metro o nome do campo.
	 */
	public static final String URL_INVALIDA = "3_0_35";

	/**
	 * Informa que a data passada como par�metro deve ser anterior ou
	 * igual a outra data passada como par�metro.
	 */
	public static final String DATA_ANTERIOR_IGUAL = "3_0_36";
	
	/**
	 * Mensagem utilizada para informar ao usu�rio que o arquivo enviado � inv�lido.
	 * 
	 * Conte�do: O arquivo enviado � inv�lido!
	 */
	public static final String ARQUIVO_UPLOAD_INVALIDO = "3_0_37";
	
	/**
	 * Mensagem utilizada para informar ao usu�rio que um sitema(SIGAA,SIPAC ou outro) n�o
	 * est� ativo. Logo, tal opera��o n�o pode ser realizada.
	 * 
	 * Conte�do: Esta opera��o n�o poder� ser realizada, pois o %s n�o est� ativo.
	 */
	public static final String SISTEMA_DESATIVADO = "3_0_40";
	
	/**
	* Mensagem exibida ao iniciar uma opera��o que espera um parametro ou objeto j� populado.. 
	* Conte�do: %s n�o foi selecionado(a). 
	* Tipo: ERROR. <br>	
	*/
	public static final String OBJETO_NAO_FOI_SELECIONADO = "3_0_41";
	
}
