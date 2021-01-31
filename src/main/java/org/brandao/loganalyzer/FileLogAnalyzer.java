package org.brandao.loganalyzer;

public interface FileLogAnalyzer {

	void setLogFile(String name);
	
	void addLogAnalyzer(String name, LogAnalyzer value);
	
	void removeLogAnalyzer(String name);
	
	void analyze();
	
	void setEnabled(boolean value);
	
	boolean isEnabled();
	
}
