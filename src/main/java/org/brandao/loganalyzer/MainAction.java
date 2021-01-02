package org.brandao.loganalyzer;

import java.io.IOException;

public class MainAction {

	private ApplicationManager applicationManager;
	
	private ApplicationManagerServer applicationManagerServer;
	
	private ApplicationManagerClient applicationManagerClient;
	
	public MainAction(MainConfig mainConfig) {
		this.applicationManager = mainConfig.getApplicationManager();
		this.applicationManagerServer = mainConfig.getApplicationManagerServer();
		this.applicationManagerClient = mainConfig.getApplicationManagerClient();
	}

	public void start() throws IOException, LogAnalyzerException {
		applicationManager.start();
		applicationManagerServer.start();
	}
	
	public void stop() throws IOException {
		System.out.print("Sending stop signal...");
		System.out.println(applicationManagerClient.stop()? "success" : "error");
	}
	
	public void reload() throws IOException {
		System.out.print("Sending reload signal...");
		System.out.println(applicationManagerClient.reload()? "success" : "error");
	}
	
	public void help() {
		System.out.println("Real time log analysis.");
		System.out.println("");
		System.out.println("loganalyzer <action> <config>");
		System.out.println("");
		System.out.println("\taction: execute an action.");
		System.out.println("\t\tstart: start analysis.");
		System.out.println("\t\tstop: stop analysis.");
		System.out.println("\t\treload: reload configuration.");
		System.out.println("");
		System.out.println("\tconfig: configuration file path.");
		System.out.println("");
		System.out.println("\thelp: show this help.");
	}
	
}
