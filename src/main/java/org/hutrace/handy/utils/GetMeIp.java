package org.hutrace.handy.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class GetMeIp extends Logger {
	
	public String outerNet() {
		String sysType = System.getProperties().getProperty("os.name");
		return sysType.toLowerCase().startsWith("win")
				? Command.instance().windows("curl icanhazip.com")
				: Command.instance().linux("curl icanhazip.com");
	}

	public String intranet() {
		String sysType = System.getProperties().getProperty("os.name");
		if (sysType.toLowerCase().startsWith("win")) {
			String localIP = null;
			try {
				localIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException var4) {
				var4.printStackTrace();
			}

			return localIP;
		} else {
			return this.getIpByEth0();
		}
	}

	private String getIpByEth0() {
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface
					.getNetworkInterfaces();

			while (true) {
				NetworkInterface netInterface;
				do {
					if (!allNetInterfaces.hasMoreElements()) {
						return "172.0.0.1";
					}

					netInterface = (NetworkInterface) allNetInterfaces
							.nextElement();
				} while (!"eth0".equals(netInterface.getName()));

				Enumeration<InetAddress> addresses = netInterface.getInetAddresses();

				while (addresses.hasMoreElements()) {
					InetAddress ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						return ip.getHostAddress();
					}
				}
			}
		} catch (SocketException var5) {
			this.log.error(var5.getMessage(), var5);
			return "172.0.0.1";
		}
	}
	
	private static GetMeIp instance;

	public static GetMeIp instance() {
		if(instance == null) {
			instance = new GetMeIp();
		}
		return instance;
	}
}