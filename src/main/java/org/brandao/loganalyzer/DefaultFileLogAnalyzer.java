package org.brandao.loganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DefaultFileLogAnalyzer implements FileLogAnalyzer{

	private String filePath;
	
	private Map<String, LogAnalyzer> analizers;
	
	private boolean enabled;
	
	public DefaultFileLogAnalyzer() {
		this.analizers = new HashMap<String, LogAnalyzer>();
		this.enabled = true;
	}
	@Override
	public void setLogFile(String name) {
		this.filePath = name;
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
		return buf != null;
	}
	
	@Override
	public void analyze() {
		try {
			openBufferedReader();
			analyze0();
		}
		catch(Throwable e) {
			e.printStackTrace();
			return;
		}
		finally {
			closeBufferedReader();
		}
		
	}
	
	private BufferedReader buf = null;
	
	private long fileSize = -1;

	private void openBufferedReader() throws IOException {
		File file = new File(filePath);
		buf = new BufferedReader(new FileReader(file));
		fileSize = file.length();
    	System.out.println("opened file: " + filePath);
	}

	private void closeBufferedReader() {
		
		try {
			buf.close();
        	System.out.println("file closed: " + filePath);
        	buf = null;
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
		finally {
			fileSize = -1;
		}
		
	}
	
	private String readLine() throws IOException {
		
		File newFile = new File(filePath);
		
		if(newFile.length() < fileSize) {
			buf.close();
			buf = new BufferedReader(new FileReader(filePath));
        	System.out.println("Reopen file: " + filePath);
		}
		
		return buf.readLine();
	}
	
	public void analyze0() throws FileNotFoundException, InterruptedException {

		enabled = true;
		
        String line = null;
        
        while (enabled) {
        	
        	try {
        		line = readLine();

                if (line == null) {
	        		Thread.sleep(5000);
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
        		e.printStackTrace();
                Thread.sleep(5000);
        	}
        	
        }
        		
	}


}
