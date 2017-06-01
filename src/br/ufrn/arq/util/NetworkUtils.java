/*
 * Universidade Federal do Rio Grande do Norte
 * Superintend�ncia de Inform�tica 
 * Diretoria de Sistemas
 * 
 * Criado em: 07/06/2010
 */
package br.ufrn.arq.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Classe com m�todos utilit�rios para acesso a informa��es
 * de rede.
 * 
 * @author David Pereira
 * @author Gleydson Lima
 * 
 */
public class NetworkUtils {

	
	/**
	 * Retorna o endere�o MAC da primeira placa de rede
	 * da m�quina onde o m�todo for executado. 
	 * @return
	 */
	public static String getLocalMacAddress() {
		try {
			return getMacAddress(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
	/**
	 * Retorna o endere�o MAC da placa de rede com endere�o passado como
	 * par�metro.
	 * @param address
	 * @return
	 */
	public static String getMacAddress(InetAddress address) {
		
		try {
			String ip = address.getHostAddress();
			String loopBack = ip.substring(0, 3);
			if (loopBack.equals("127")) {
				ip = getFirstIP();
			}
			
			InetAddress localHost = InetAddress.getByName(ip);
			NetworkInterface netInter = NetworkInterface.getByInetAddress(localHost);
			byte[] macAddressBytes = netInter.getHardwareAddress();
	
			String macAddress = String.format("%1$02x-%2$02x-%3$02x-%4$02x-%5$02x-%6$02x", 
					macAddressBytes[0], macAddressBytes[1], macAddressBytes[2],
					macAddressBytes[3], macAddressBytes[4], macAddressBytes[5])
					.toUpperCase();
	
			return macAddress;
		} catch(SocketException e) {
			return null;
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/*
	 * Retorna o primeiro IP da m�quina onde o m�todo for executado. 
	 */
	private static String getFirstIP() {
		Enumeration<NetworkInterface> ifaces = null;

		try {
			ifaces = NetworkInterface.getNetworkInterfaces();
		} catch (java.net.SocketException e) {
		}

		if (ifaces != null) {
			for ( ; ifaces.hasMoreElements(); ) {
				Enumeration<InetAddress> addrs = ifaces.nextElement().getInetAddresses();
				for (; addrs.hasMoreElements();) {
					InetAddress addr = addrs.nextElement();
					if (!addr.isLoopbackAddress() && !(addr instanceof java.net.Inet6Address))
						return addr.getHostName();
				}
	
			}
		}
		
		return "127.0.0.1";
	}

	/**
	 * Retorna o nome da m�quina em que o m�todo � executado
	 *
	 * @return
	 */
	public static String getLocalName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	/**
	 * Retorna o endere�o da m�quina onde o m�todo foi executado
	 *
	 * @return
	 */
	public static String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}
	
}
