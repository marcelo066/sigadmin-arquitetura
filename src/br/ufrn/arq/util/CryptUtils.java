/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 07/04/2010
 */
package br.ufrn.arq.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.security.Provider;
import java.security.Security;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import br.ufrn.arq.dao.Database;
import br.ufrn.arq.dao.JdbcTemplate;
import br.ufrn.arq.erros.ConfiguracaoAmbienteException;

/**
 * Classe utilit�ria com m�todos para criptografar e descriptografar
 * informa��es de acordo com um conjunto de algoritmos pr�-definidos.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 */
public class CryptUtils {

	public static final int BLOWFISH = 1;
	
	/**
	 * M�todo utilizado para encriptar uma informa��o passando a chave de encripta��o.
	 * @param cleartext
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String crypt(String msg, int algoritmo) {
		try {
	       	return crypt(msg, algoritmo, Cipher.ENCRYPT_MODE);
	   	} catch (Exception ex) {
	   		throw new ConfiguracaoAmbienteException("Imposs�vel encriptar os dados.");
	   	}		
	}
	
	
	/**
	 * M�todo utilizado para desencriptar uma informa��o passando a chave de encripta��o.
	 * @param ciphertext
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String msg, int algoritmo) {
		try {
			return crypt(msg, algoritmo, Cipher.DECRYPT_MODE);
		} catch (Exception e) {
			throw new ConfiguracaoAmbienteException("Imposs�vel desencriptar os dados.");
		}
	}

	/*
	 * M�todo respons�vel pelo encripta��o e desencripta��o da informa��o
	 * de acordo com a chave e o algoritmo passados como par�metro.
	 */
	private static String crypt(String input, int algoritmo, int mode) throws Exception {
		Map<String, String> infoAlgoritmo = getInfoAlgoritmo(algoritmo);
		String key = lerArquivo(infoAlgoritmo.get("key"));
		String name = infoAlgoritmo.get("name");
		String transformation = infoAlgoritmo.get("transformation");
		
		// Install SunJCE provider
		Provider sunJce = new com.sun.crypto.provider.SunJCE();
		Security.addProvider(sunJce);
			
		byte[] raw = key.getBytes();
		SecretKeySpec skeySpec = new SecretKeySpec(raw, name);
			
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(mode, skeySpec);
			
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes());
		CipherOutputStream cos = new CipherOutputStream(bos, cipher);
			
		int length = 0;
		byte[] buffer = new byte[8192];
			
		while ((length = bis.read(buffer)) != -1) {
			cos.write(buffer, 0, length);
		}
			  
		bis.close();
		cos.close();
			
		return bos.toString();
	}
	
	
	/*
	 * M�todo respons�vel por retornar a chave de encripta��o armazenada em um arquivo de chaves.
	 */
	private static String lerArquivo(String arquivo) {
		File f = new File(arquivo);
		String key = "";
		try {
			if(!f.exists())	{
				return null;
			}
			BufferedReader in = new BufferedReader(new FileReader(f));
			String linha;
			while((linha = in.readLine())!=null){
				key = linha;
			}
			in.close();
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally{
			
		}
	}
	
	/*
	 * Busca informa��es no banco de dados do algoritmo passado como par�metro.
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getInfoAlgoritmo(int algoritmo) {
		return new JdbcTemplate(Database.getInstance().getComumDs()).queryForMap("select * from infra.algoritmo_criptografia where codigo = ?", new Object[]{algoritmo});
	}
	
}
