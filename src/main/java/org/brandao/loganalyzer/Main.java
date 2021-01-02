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
	
	private static MainConfig mainConfig;
	
	public static void main(String[] params) throws Throwable {
		loadConfig(params);
		executeAction(params);
	}

	private static void loadConfig(String[] params) throws FileNotFoundException, IOException {
		Properties prop = getConfig(params);
		mainConfig = new MainConfig();
		mainConfig.configure(prop);
	}
	
	private static void executeAction(String[] params) throws IOException, LogAnalyzerException {
		
		MainAction mainAction = new MainAction(mainConfig);
		String action         = getAction(params);
		
		switch (action) {
		case "start":
			mainAction.start();
			break;
		case "stop":
			mainAction.stop();
			break;
		case "reload":
			mainAction.reload();
			break;
		case "help":
			mainAction.help();
			break;
		}
	}
	
	@SuppressWarnings("serial")
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
