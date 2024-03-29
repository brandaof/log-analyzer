package org.brandao.loganalyzer;

public interface LogAnalyzer {

	
	void addAction(String name, Action value);
	
	void removeAction(String name, Action value);
	
	void addVarParser(String name, VarLogParser value);
	
	void removeVarParser(String name, VarLogParser value);

	void addVarMatch(String name, VarMatch value);
	
	void removeVarMatch(String name, VarMatch value);
	
	void setLineLogParser(LineLogParser value);
	
	void analyze(String line) throws Throwable;
	
}
