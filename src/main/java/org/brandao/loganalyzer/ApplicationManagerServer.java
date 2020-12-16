package org.brandao.loganalyzer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Properties;

public class ApplicationManagerServer {

	public static final String STOP_CMD     = "stop";
	
	public static final String RELOAD_CMD   = "reload";
	
	public static final String SUCCESS_RESP = "ok";
	
	public static final String ERROR_RESP   = "error";
	
	private Properties config;
	
	private LogAnalizerManager manager;
	
	private DatagramSocket socket;
	
	public ApplicationManagerServer() {
		enabled = false;
		socket = null;
	}
	
	public void setConfig(Properties value) {
		this.config = value;
	}
	
	public void setManager(LogAnalizerManager value) {
		this.manager = value;
	}
	
    private boolean enabled;
    
    public boolean isEnabled() {
    	return enabled;
    }
    
	public void start() throws IOException {
		
		String portSTR = config.getProperty("manager-port", "8090");
		Integer port   = Integer.parseInt(portSTR);
	    byte[] buf     = new byte[256];
	    
		socket = new DatagramSocket(port);
		enabled = true;

		while (enabled) {
		    DatagramPacket packet = new DatagramPacket(buf, buf.length);
		    socket.receive(packet);
		    
		    String received = new String(packet.getData(), 0, packet.getLength());
		    
		    try {
			    switch (received) {
				case STOP_CMD:
					stopCMD(packet.getAddress(), packet.getPort());
					break;
				case RELOAD_CMD:
					reloadCMD(packet.getAddress(), packet.getPort());
					break;
				default:
				}
		    }
		    catch(Throwable ex) {
		    	ex.printStackTrace();
		    }

		}
		
		socket.close();
	        
	}

	public void stop() {
		this.enabled = false;
	}
	
	private void stopCMD(InetAddress host, int port) throws LogAnalyzerException, IOException {
		
		try {
			System.out.println("Received stop signal.");
			manager.stopAnalyzer();
			stop();
		}
		catch(Throwable e) {
			e.printStackTrace();
			send(ERROR_RESP, host, port);
			return;
		}
		
		send(SUCCESS_RESP, host, port);
	}

	private void reloadCMD(InetAddress host, int port) throws LogAnalyzerException, IOException {
		
		try {
			System.out.println("Received reload signal.");
			manager.reloadAnalyzer();
		}
		catch(Throwable e) {
			e.printStackTrace();
			send(ERROR_RESP, host, port);
			return;
		}
		
		send(SUCCESS_RESP, host, port);
		
	}

	private void send(String value, InetAddress host, int port) throws IOException {
		byte[] buf = value.getBytes("UTF-8");
		DatagramPacket packet = new DatagramPacket(buf, buf.length, host, port);		
		socket.send(packet);
	}
	
}
