package org.brandao.loganalyzer;

import java.io.File;

public interface FileLogAnalyzer {

	void setLogFile(File file);
	
	void addLogAnalyzer(String name, LogAnalyzer value);
	
	void removeLogAnalyzer(String name);
	
	void analyze();
	
	void setEnabled(boolean value);
	
	boolean isEnabled();
	
}
