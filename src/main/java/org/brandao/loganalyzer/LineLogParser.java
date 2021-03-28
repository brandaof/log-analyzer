package org.brandao.loganalyzer;

import java.util.Map;

public interface LineLogParser {

	boolean match(String value);
	
	Map<String, Object> parser(String line);
	
}
