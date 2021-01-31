package org.brandao.loganalyzer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class LogAnalizerManager {

	private Map<String, FileLogAnalyzer> logs;
	
	private Properties properties;
	
	public LogAnalizerManager(){
		this.logs = null;
		
	}
	public void setConfigFile(Properties properties) {
		this.properties = properties;
	}
	
	public synchronized void startAnalyzer() throws LogAnalyzerException{

		if(logs != null) {
			throw new LogAnalyzerException("analyzers has been started");
		}
		
		logs = new HashMap<String, FileLogAnalyzer>();
		Map<String, FileLogAnalyzer> local = load();
		
		local.entrySet().stream().forEach(e->{
			String key = e.getKey();
			FileLogAnalyzer fla = e.getValue();
			try {
				startAnalyzer(key,fla);
			}
			catch(Throwable ex) {
				ex.printStackTrace();
			}
		});
		
	}

	public synchronized void reloadAnalyzer() throws LogAnalyzerException{

		if(logs == null) {
			throw new LogAnalyzerException("analyzers not initialized");
		}
		
		Map<String, FileLogAnalyzer> local = load();
		
		local.entrySet().stream()
			.filter(e->!logs.containsKey(e.getKey()))
			.map(e->e.getKey())
			.forEach(e->{
				try {
					startAnalyzer(e, local.get(e));
				}
				catch(Throwable ex) {
					ex.printStackTrace();
				}
			});
			//.collect(Collectors.toSet());

		local.entrySet().stream()
			.filter(e->logs.containsKey(e.getKey()))
			.map(e->e.getKey())
			.forEach(e->{
				try {
					reloadAnalyzer(e, local.get(e));
				}
				catch(Throwable ex) {
					ex.printStackTrace();
				}
			});
			//.collect(Collectors.toSet());
		
		logs.entrySet().stream()
			.filter(e->!local.containsKey(e.getKey()))
			.map(e->e.getKey())
			.forEach(e->{
				try {
					stopAnalyzer(e, logs.get(e));
				}
				catch(Throwable ex) {
					ex.printStackTrace();
				}
			});
			//.collect(Collectors.toSet());

	}
	
	public synchronized void stopAnalyzer() throws LogAnalyzerException{

		Map<String, FileLogAnalyzer> local = new HashMap<>(logs);
		
		local.entrySet().stream().forEach(e->{
			String key = e.getKey();
			FileLogAnalyzer fla = e.getValue();
			try {
				stopAnalyzer(key,fla);
			}
			catch(Throwable ex) {
				ex.printStackTrace();
			}
		});
		
	}
	
	private void startAnalyzer(String id, FileLogAnalyzer ffa) throws InterruptedException {
		
		Thread thread = new Thread(()->{
			ffa.analyze();
		});
		
		thread.start();
		
		logs.put(id, ffa);
	}

	private void reloadAnalyzer(String id, FileLogAnalyzer ffa) throws InterruptedException {
		
		FileLogAnalyzer started = logs.get(id);
		
		stopAnalyzer(id, started);
		startAnalyzer(id, ffa);
	}
	
	private void stopAnalyzer(String id, FileLogAnalyzer ffa) throws InterruptedException {
		
		ffa.setEnabled(false);
		
		while(ffa.isEnabled()) {
			Thread.sleep(TimeUnit.SECONDS.toMillis(2));
		}
		
		logs.remove(id);
	}
	
	private Map<String, FileLogAnalyzer> load() throws LogAnalyzerException{
		try {
			ConfigurationParser cp = new ConfigurationParser();
			return cp.parser(properties);
		}
		catch(Throwable e) {
			throw new LogAnalyzerException("fail to load config", e);
		}
		
	}
}
