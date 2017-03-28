package com.tactfactory.mongodbutil.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.mongodb.MongoClient;

public class NetworkUtil {
	private Enumeration<NetworkInterface> networkInterfaces = null;
	private Enumeration<InetAddress> networkAddresses = null;

	/**
	 * @return the networkInterfaces
	 */
	public Enumeration<NetworkInterface> getNetworkInterfaces() {
		return networkInterfaces;
	}

	/**
	 * @param networkInterfaces
	 *            the networkInterfaces to set
	 */
	public void setNetworkInterfaces(
			Enumeration<NetworkInterface> networkInterfaces) {
		this.networkInterfaces = networkInterfaces;
	}

	/**
	 * @return the networkAddresses
	 */
	public Enumeration<InetAddress> getNetworkAddresses() {
		return networkAddresses;
	}

	/**
	 * @param networkAddresses
	 *            the networkAddresses to set
	 */
	public void setNetworkAddresses(Enumeration<InetAddress> networkAddresses) {
		this.networkAddresses = networkAddresses;
	}

	public ArrayList<String> reponseFromIpsForMongo(byte[] address, int port) {

		ArrayList<String> result = new ArrayList<String>();

		InetSocketAddress inetSock;
		InetAddress inet;

		try {
			for (byte i = 1; i < 20; i++) {

				//inetSock = InetSocketAddress.createUnresolved(address + "."+ i , port);


				inet = InetAddress.getByAddress(new byte[] { address[0], address[1], address[2], i });
				System.out.println("Sending Ping Request to " + inet);
				if (inet.isReachable(5000)) {
					try {
						byte[] currentAddress = inet.getAddress();
						String followAddress = currentAddress[0] + "." + currentAddress[1] + "." + currentAddress[2] + "." +currentAddress[3];
						MongoClient client = new MongoClient(followAddress);
						client.listDatabaseNames();
						result.add(followAddress);
					} catch (Exception e) {
						// TODO: handle exception
					}

					System.out.println("Host is reachable");
				}else {
					System.out.println("Host is NOT reachable");
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	// public ArrayList<String> reponseFromIps1() {
	//
	// ArrayList<String> result = new ArrayList<String>();
	//
	// try {
	// while (true) {
	// if (this.networkInterfaces == null) {
	// networkInterfaces = NetworkInterface.getNetworkInterfaces();
	// }
	// if (networkAddresses == null
	// || !networkAddresses.hasMoreElements()) {
	// if (networkInterfaces.hasMoreElements()) {
	// NetworkInterface networkInterface = networkInterfaces
	// .nextElement();
	// networkAddresses = networkInterface.getInetAddresses();
	// } else {
	// networkInterfaces = null;
	// }
	// } else {
	// if (networkAddresses.hasMoreElements()) {
	// String address = networkAddresses.nextElement()
	// .getHostAddress();
	// if (address.contains(".")) // IPv4 address
	// {
	// result.add(address);
	// }
	// break;
	// } else {
	// networkAddresses = null;
	// }
	// }
	// }
	// } catch (SocketException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

}
