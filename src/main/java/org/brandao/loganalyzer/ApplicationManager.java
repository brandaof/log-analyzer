package org.brandao.loganalyzer;

public class ApplicationManager {

	private LogAnalizerManager manager;

	public void setManager(LogAnalizerManager value) {
		this.manager = value;
	}

	public void start() throws LogAnalyzerException{
		manager.startAnalyzer();
	}

	public void stop() throws LogAnalyzerException {
		manager.stopAnalyzer();
	}

	public void reload() throws LogAnalyzerException {
		manager.reloadAnalyzer();
	}

}
