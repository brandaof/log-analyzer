package org.brandao.loganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class DefaultFileLogAnalyzer implements FileLogAnalyzer{

	private File logFile;
	
	private Map<String, LogAnalyzer> analizers;
	
	private boolean enabled;
	
	private BufferedReader stream;
	
	public DefaultFileLogAnalyzer() {
		this.analizers = new HashMap<String, LogAnalyzer>();
		this.enabled = true;
	}
	@Override
	public void setLogFile(File file) {
		this.logFile = file;
	}

	@Override
	public synchronized void addLogAnalyzer(String name, LogAnalyzer value) {
		analizers.put(name, value);
	}

	@Override
	public synchronized void removeLogAnalyzer(String name) {
		analizers.remove(name);
	}

	public void setEnabled(boolean value) {
		this.enabled = value;
	}

	public boolean isEnabled() {
		return stream != null;
	}
	
	@Override
	public void analyze() {
		try {
			analyze0();
		}
		catch(Throwable e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	public void analyze0() throws FileNotFoundException, InterruptedException {

		stream = new BufferedReader(new FileReader(logFile));
		long fileSize = logFile.length();
		enabled = true;
		try{
	        String line = null;
	        
	        while (enabled) {
	        	
	        	try {
	        		line = stream.readLine();

	                if (line == null) {
		        		Thread.sleep(5000);
		        		File newFile = new File(logFile.getAbsolutePath());
		        		
		        		long newFileSize = newFile.length();
		        		
		        		if(newFileSize < fileSize) {
		        			
	                    	System.out.println("Reloading file: " + logFile.getAbsolutePath());
	                    	
		                    try {
		                    	stream.close();
		                    }
		                    catch(Throwable ex) {}
		                    
		                    try {
		                    	stream = new BufferedReader(new FileReader(logFile));
		                    	fileSize = newFileSize;
		                    	logFile = newFile;
		                    }
		                    catch(Throwable ex) {}
		        			
		        		}
	                }
	                else {
	                	synchronized (this) {
							for(LogAnalyzer l: analizers.values()) {
								try {
									l.analyze(line);
								}
								catch(Throwable ex) {
									ex.printStackTrace();
								}
							}
						}
	                }
	        	}
	        	catch(Throwable e) {
                    Thread.sleep(5000);
                    
                    e.printStackTrace();
                    
                	System.out.println("Reopen file: " + logFile.getAbsolutePath());
                    
                    try {
                    	stream.close();
                    }
                    catch(Throwable ex) {}
                    
                    try {
		        		File newFile = new File(logFile.getAbsolutePath());
                    	stream = new BufferedReader(new FileReader(newFile));
                    	logFile = newFile;
                    }
                    catch(Throwable ex) {}
	        	}
	        	
	        }
		}
		finally {
			try {
				stream.close();
			}
			catch(Throwable e) {
			}
			stream = null;
		}
        		
	}


}
