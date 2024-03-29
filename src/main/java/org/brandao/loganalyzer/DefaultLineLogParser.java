package org.brandao.loganalyzer;

import java.util.HashMap;
import java.util.Map;

import org.brandao.string.StringTemplate;

public class DefaultLineLogParser implements LineLogParser{

	private String match;
	
	private StringTemplate parser;
	
	public DefaultLineLogParser(String match, StringTemplate parser) {
		this.match = match;
		this.parser = parser;
	}

	@Override
	public boolean match(String value) {
		return value.matches(match);
	}

	@Override
	public Map<String, Object> parser(String line) {
		return new HashMap<String,Object>(parser.getParameters(line));
	}

}
