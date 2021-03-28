package org.brandao.loganalyzer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class URLVarLogParser implements VarLogParser{

	@Override
	public Map<String, Object> parser(String value) {
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			
			if(value.matches("^.{1,}\\:\\/\\/.*")){
				URL u = new URL(value);
				map.put("protocol", u.getProtocol());
				map.put("host", u.getHost());
				map.put("port", String.valueOf(u.getPort()));
				map.put("path", u.getPath());
				map.put("query", u.getQuery());
			}
			else
			if(value.matches(".*\\:[0-9]{1,}$")) {
				String[] parts = value.split("\\:");
				map.put("host", parts[0]);
				map.put("port", parts[1]);
			}
			else {
				map.put("host", value);
			}
			
			return map;
		}
		catch(Throwable e) {
			throw new IllegalStateException("fail to parse: " + value, e);
		}
	}

}
