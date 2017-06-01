/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Cria��o em: 14/09/2004
 */
package br.ufrn.arq.web.struts;

/**
 * 
 * Esta classe representa um conjunto de constantes que informam a Action qual
 * opera��o deseja-se fazer no momento.
 * 
 * Essas constantes fazem sentido quando uma determinada Action realiza diversas
 * a��es, ou seja, a cada momento � necess�rio informar qual a a��o que
 * deseja-se.
 * 
 * @author Gleydson Lima
 *  
 */
public class ConstantesActionGeral {

    public static final int INSERIR = 1;

    public static final int REMOVER = 2;

    public static final int ATUALIZAR = 3;

    public static final int ENVIAR = 4;

    public static final int ATENDER = 9;

    public static final int PRE_INSERIR = 5;

    public static final int PRE_REMOVER = 6;

    public static final int PRE_ATUALIZAR = 7;

    public static final int PRE_ENVIAR = 8;

    public static final int PRE_ATENDER = 10;

    public static final int CONFIRMA_ATUALIZAR = 11;

    //SCO tem uma constante dessa com mesmo valor. Se mudar aqui, mudar l� tamb�m
    public static final int VISUALIZAR = 12;

    public static final int BUSCAR = 13;

    public static final int COMPROVANTE = 14;

    public static final int PERMISSAO = 15;
    
    public static final int PRE_NEGAR = 16;
    public static final int NEGAR = 17;
    
    /** Constante para remo��o em de mais de um elemento de uma s� vez */
    public static final int REMOVER_LOTE = 26;

    /**
     * A��o executada quando o usu�rio deseja informar a Action que a view deve
     * ser de impress�o.
     */
    public static final int IMPRIMIR = 18;

    /** A��es de Pagina��o */
    public static final int PROX_PAGINA = 19;

    public static final int RET_PAGINA = 20;

    /** Associar usu�rio a um grupo de material para que este possa cadastr�-lo */
    public static final int ASSOCIACAO_GRUPO = 21;

    /** Atrubuir um restaurante a um usu�rio */
    public static final int ATRIBUI_RESTAURANTE = 28;
    
    /** A��o geral para confirma��es */
    public static final int CONFIRMAR = 22;
    
    public static final int LISTAR = 23;
    
    public static final int CANCELAR = 24;
    
    public static final int RETORNAR_REQ = 25;
    
    public static final int ASSOCIACAO_RESTAURANTE = 27;
    
    /** Consultar lista de fornecedores a partir do menu unidade */
    
    public static final int CONSULTAR_FORNECEDORES = 29;
    
    public static final int VISUALIZAR_FORNECEDOR = 30;
}