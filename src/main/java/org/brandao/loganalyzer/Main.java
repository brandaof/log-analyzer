package org.brandao.loganalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class Main {
	
	public static void main(String[] s) throws Throwable {
		
		Properties prop = getConfig(s);
		String action   = getAction(s);
		
		LogAnalizerManager lam = new LogAnalizerManager();
		lam.setConfigFile(s[1]);

		ApplicationManager app = new ApplicationManager();
		app.setManager(lam);

		ApplicationManagerClient amc = new ApplicationManagerClient();
		amc.setConfig(prop);
		
		ApplicationManagerServer ams = new ApplicationManagerServer();
		ams.setConfig(prop);
		ams.setManager(lam);
		
		switch (action) {
		case "start":
			app.start();
			ams.start();
			break;
		case "stop":
			System.out.print("Sending stop signal...");
			System.out.println(amc.stop()? "success" : "error");
			break;
		case "reload":
			System.out.print("Sending reload signal...");
			System.out.println(amc.reload()? "success" : "error");
			break;
		case "help":
			printHelp();
			break;
		}
		
	}

	private static final Set<String> supportedActions = new HashSet<String>() {{
		add("start");
		add("stop");
		add("reload");
		add("help");
	}};
	
	private static String getAction(String[] params){
		String value = params == null || params.length < 1? null : params[0];
		
		if(value == null || value.isEmpty()) {
			System.out.println("action must be informed");
			System.exit(-1);
		}
		
		if(!supportedActions.contains(value)) {
			System.out.println("invalid action: " + value);
			System.exit(-1);
		}
		
		return value;
	}

	private static void printHelp(){
		System.out.println("Real time log analysis.");
		System.out.println("");
		System.out.println("loganalyzer <action> <config>");
		System.out.println("");
		System.out.println("\taction: execute an action.");
		System.out.println("\t\tstart: start analysis.");
		System.out.println("\t\tstop: stop analysis.");
		System.out.println("\t\treload: reload configuration.");
		System.out.println("");
		System.out.println("\tconfig: configuration file path.");
		System.out.println("");
		System.out.println("\thelp: show this help.");
		System.exit(0);
	}
	
	private static Properties getConfig(String[] params) throws FileNotFoundException, IOException {
		
		String value = params == null || params.length < 2? null : params[1];
		
		if(value == null || value.isEmpty()) {
			System.out.println("configuration file must be informed");
			System.exit(-1);
		}

		File f = new File(value);
		
		if(!f.exists() || !f.canRead()) {
			System.out.println("configuration file not found: " + value);
			System.exit(-1);
		}
		
		Properties prop = new Properties();

		try(BufferedReader br = new BufferedReader(new FileReader(f));) {
			prop.load(br);
		}
		
		return prop;
	}
}
