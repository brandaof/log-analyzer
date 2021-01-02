package org.brandao.loganalyzer;

import java.util.Properties;

public class MainConfig {

	private LogAnalizerManager logAnalizerManager;

	private ApplicationManager applicationManager;
	
	private ApplicationManagerClient applicationManagerClient;
	
	private ApplicationManagerServer applicationManagerServer;
	
	public void configure(Properties prop) {
		
		this.logAnalizerManager = new LogAnalizerManager();
		this.logAnalizerManager.setConfigFile(prop);

		this.applicationManager = new ApplicationManager();
		applicationManager.setManager(logAnalizerManager);

		this.applicationManagerClient = new ApplicationManagerClient();
		this.applicationManagerClient.setConfig(prop);
		
		this.applicationManagerServer = new ApplicationManagerServer();
		this.applicationManagerServer.setConfig(prop);
		this.applicationManagerServer.setManager(logAnalizerManager);
	}

	public LogAnalizerManager getLogAnalizerManager() {
		return logAnalizerManager;
	}

	public ApplicationManager getApplicationManager() {
		return applicationManager;
	}

	public ApplicationManagerClient getApplicationManagerClient() {
		return applicationManagerClient;
	}

	public ApplicationManagerServer getApplicationManagerServer() {
		return applicationManagerServer;
	}
	
}
