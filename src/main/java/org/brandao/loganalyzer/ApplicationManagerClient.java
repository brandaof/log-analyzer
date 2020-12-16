package org.brandao.loganalyzer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class ApplicationManagerClient {

	private Properties config;
	
	public void setConfig(Properties value) {
		this.config = value;
	}

	public boolean stop() throws IOException {
		String response = sendCMD(ApplicationManagerServer.STOP_CMD);
		return ApplicationManagerServer.SUCCESS_RESP.equals(response);
	}
	
	public boolean reload() throws IOException {
		String response = sendCMD(ApplicationManagerServer.RELOAD_CMD);
		return ApplicationManagerServer.SUCCESS_RESP.equals(response);
	}
	
	private String sendCMD(String value) throws IOException {
		
		String portSTR      = config.getProperty("manager-port", "8090");
		Integer port        = Integer.parseInt(portSTR);
		InetAddress address = InetAddress.getLoopbackAddress();
		byte[] buf          = value.getBytes("UTF-8");
		
		DatagramSocket socket = new DatagramSocket();
		socket.setSoTimeout(30000);
		try{
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);		
			socket.send(packet);
			
			buf = new byte[256];
			
			packet = new DatagramPacket(buf, buf.length);
		    socket.receive(packet);
		    return new String(packet.getData(), 0, packet.getLength());
		}
		finally {
			socket.close();
		}
		
	}

}
