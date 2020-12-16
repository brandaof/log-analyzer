package org.brandao.loganalyzer;

import java.util.Map;

public interface VarLogParser {

	Map<String,String> parser(String value);
	
}
