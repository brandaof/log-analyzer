package org.brandao.loganalyzer;

import java.util.Map;

public interface VarLogParser {

	Map<String,Object> parser(String value);
	
}
