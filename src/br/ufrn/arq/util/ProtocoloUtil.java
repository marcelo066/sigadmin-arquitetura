/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 11/11/2002
 */
package br.ufrn.arq.util;

/**
 * Classe que encapsula funcionalidades de Protocolo. 
 * 
 * @author Gleydson Lima
 */
public class ProtocoloUtil {

	/**
	 * @deprecated Usar radical da unidade.
	 */
	public static final java.lang.String numFixo = "23077";


	/**
	 * M�todo usado para se calcular o d�gito verificador de um protocolo a partir de seu n�mero.
	 * 
	 * @param num
	 * @return
	 */
	public static int calculaDV(long num) {
		int dv1, dv2, k, sum;
		long tmp = num;
		k = 2;
		sum = 0;
		while (num > 0) {
			sum += (num % 10) * k;
			num = num / 10;
			k++;
		}
		dv1 = sum % 11;
		dv1 = (11 - dv1) % 10;
		k = 2;
		sum = 0;
		num = tmp * 10 + dv1;
		while (num > 0) {
			sum += (num % 10) * k;
			num = num / 10;
			k++;
		}
		dv2 = sum % 11;
		dv2 = (11 - dv2) % 10;
		return (dv1 * 10 + dv2);
	}

	/**
	 * M�todo usado para se calcular o d�gito verificador de um protocolo a partir de seu n�mero.
	 * 
	 * @param radical
	 * @param numProtocolo
	 * @param ano
	 * @return
	 */
	public static int calculaDV(int radical, int numProtocolo, int ano) {
		return calculaDV(radical * 10000000000l + numProtocolo * 10000l + ano);
	}

	/**
	 * M�todo utilizado para formatar o d�gito verificador, deixando com 2 d�gitos.
	 * 
	 * @param dv
	 * @return
	 */
	public static String formataDV(int dv) {
		if (dv > 9)
			return String.valueOf(dv);
		else
			return "0" + dv;

	}

	/**
	 * M�todo utilizado para formatar o n�mero, deixando com 6 d�gitos.
	 * 
	 * @param numero
	 * @return
	 */
	public static String formataNumero(int numero) {
		int i, k;
		String numFormatado = String.valueOf(numero);
		if (numero == 0)
			k = -1;
		else
			k = (int) (Math.log(numero) / Math.log(10));
		for (i = 0; i < 5 - k; i++)
			numFormatado = "0" + numFormatado;
		return numFormatado;
	}
	
	/**
	 * M�todo utilizado para formatar o radical, deixando com 5 d�gitos.
	 * 	
	 * @param radical
	 * @return
	 */
    public static String formataRadical(int radical) {
        
    	String radicalFormatado = String.valueOf(radical);
        
        int tamRadical = radicalFormatado.length();
        
        if (tamRadical < 5){
            
        	for (int i = 0; i < 5 - tamRadical; i++)
            	radicalFormatado = "0" + radicalFormatado;
    	}
    
        return radicalFormatado;
    }

    
}