package org.brandao.loganalyzer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

public class DefaultLogAnalyzer implements LogAnalyzer {

	private LineLogParser parser;
	
	private Map<String,Action> actions;

	private Map<String, VarLogParser> varLogParsers;
	
	public DefaultLogAnalyzer() {
		this.actions = new HashMap<>();
		this.varLogParsers = new HashMap<>();
	}
	
	@Override
	public synchronized void addAction(String name, Action value) {
		actions.put(name, value);
	}
	
	@Override
	public synchronized void removeAction(String name, Action value) {
		actions.remove(name);
	}

	@Override
	public synchronized void addVarParser(String name, VarLogParser value) {
		varLogParsers.put(name, value);
	}
	
	@Override
	public synchronized void removeVarParser(String name, VarLogParser value) {
		varLogParsers.remove(name);
	}
	
	@Override
	public synchronized void setLineLogParser(LineLogParser value) {
		this.parser = value;
	}
	
	private void parserVars(Map<String, String> vars, Map<String, String> r) {
		vars.entrySet().stream().forEach(e->{
			
			String k = e.getKey();
			String v = e.getValue();
			
			if(r.containsKey(k)) {
				return;
			}
			
			VarLogParser parser = varLogParsers.get(k);
			
			if(parser == null) {
				r.put(k, v);
			}
			else {
				try {
				Map<String,String> newVars = parser.parser(v);
				r.putAll(newVars);
				parserVars(newVars, r);
				}
				catch(Throwable ex) {
					throw new IllegalStateException("fail to parse " + k + "=" + v, ex);
				}
			}
			
		});
	}
	
	@Override
	public synchronized void analyze(String line) throws IOException {
		
		if(parser.match(line)) {
			line = line.replaceAll("\t+", " ").replaceAll("\\s+", " ");
			
			Map<String,String> tmpVars = parser.parser(line);
			Map<String,String> vars = new HashMap<>();
			
			try {
				parserVars(tmpVars, vars);
			}
			catch(Throwable ex) {
				throw new IllegalStateException("fail to parse the line: " + line, ex);
			}
			
			for(Action a: actions.values()) {

				Boolean r = MVEL.executeExpression(a.getExpression(), vars, Boolean.class);
				if(r != null && r.booleanValue()) {
					LocalDateTime localDateTime = LocalDateTime.now();
					
					vars.put("day", String.valueOf(localDateTime.getDayOfMonth()));
					vars.put("month", String.valueOf(localDateTime.getMonthValue()));
					vars.put("year", String.valueOf(localDateTime.getYear()));
					vars.put("hour", String.valueOf(localDateTime.getHour()));
					vars.put("minute", String.valueOf(localDateTime.getMinute()));
					vars.put("second", String.valueOf(localDateTime.getSecond()));
					
					Map<String, Object> supportedVars = new HashMap<>(vars);
					String cmd = a.getExecutor().toString((Map<String, Object>)supportedVars);
					//System.out.println(cmd);
					
					Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
					try {
						p.waitFor();
					}
					catch(Throwable e) {
					}
					
				}
			}
		}
		
	}
	
}