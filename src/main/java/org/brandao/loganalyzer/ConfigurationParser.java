package org.brandao.loganalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.brandao.string.StringTemplate;
import org.mvel2.MVEL;

public class ConfigurationParser {

	private static final String FILES_PREFIX = "analizers.files.";
	
	private static final String ANALIZERS_PREFIX = "analizers.analizer.";
	
	public Map<String, FileLogAnalyzer> parser(Properties value){
		return getFileLogAnalizer(value);
	}
	
	private Map<String, FileLogAnalyzer> getFileLogAnalizer(Properties value){
		
		Map<String, FileLogAnalyzer> r = new HashMap<String, FileLogAnalyzer>();
		
		Set<String> filesID = getItens(value, FILES_PREFIX);

		for(String fileID: filesID) {
			FileLogAnalyzer fa = new DefaultFileLogAnalyzer();
			fa.setLogFile(value.getProperty(FILES_PREFIX + fileID));
			r.put(fileID, fa);
		}
		
		Set<String> analizersID = getItens(value, ANALIZERS_PREFIX);
			
		for(String analizerID: analizersID) {
			
			String match  = value.getProperty(ANALIZERS_PREFIX + analizerID + ".match");
			String parser = value.getProperty(ANALIZERS_PREFIX + analizerID + ".parser");
			String file   = value.getProperty(ANALIZERS_PREFIX + analizerID + ".file");
			
			LogAnalyzer la = new DefaultLogAnalyzer();
			la.setLineLogParser(new DefaultLineLogParser(match, new StringTemplate(parser)));
			
			Set<String> actionsID = getItens(value, ANALIZERS_PREFIX + analizerID + ".actions.");
			
			for(String actionID: actionsID) {
				String cmp   = value.getProperty(ANALIZERS_PREFIX + analizerID + ".actions." + actionID + ".cmp");
				String event = value.getProperty(ANALIZERS_PREFIX + analizerID + ".actions." + actionID + ".event");
				String run   = value.getProperty(ANALIZERS_PREFIX + analizerID + ".actions." + actionID + ".action");
				Action action = new Action(actionID, MVEL.compileExpression(cmp), event, new StringTemplate(run));
				la.addAction(actionID, action);
			}

			Set<String> varParsersID = getItens(value, ANALIZERS_PREFIX + analizerID + ".var_parsers.");
			
			for(String varParserID: varParsersID) {
				String varParserClass   = value.getProperty(ANALIZERS_PREFIX + analizerID + ".var_parsers." + varParserID);
				VarLogParser varParser = getVarLogParserClass(varParserClass);
				la.addVarParser(varParserID, varParser);
			}

			Set<String> listsID = getItens(value, ANALIZERS_PREFIX + analizerID + ".list.");
			
			for(String varParserID: listsID) {
				String fileList    = value.getProperty(ANALIZERS_PREFIX + analizerID + ".list." + varParserID);
				VarMatch varParser = getList(fileList);
				la.addVarMatch(varParserID, varParser);
			}
			
			FileLogAnalyzer fa = r.get(file);
			
			if(fa == null) {
				throw new IllegalStateException("file analyzer not found: " + file);
			}
			
			fa.addLogAnalyzer(analizerID, la);
		}
		
		return r;
		
	}
	
	private VarLogParser getVarLogParserClass(String typeName){
		try {
			@SuppressWarnings("unchecked")
			Class<? extends VarLogParser> type = (Class<? extends VarLogParser>) Class.forName(typeName);
			return type.newInstance();
		}
		catch(Throwable e) {
			throw new IllegalStateException(e);
		}
	}

	private VarMatch getList(String file){
		
		List<String> list = new ArrayList<>();
		
		try (Stream<String> stream = Files.lines(Paths.get(file))) {
            list = stream.collect(Collectors.toList());
            return new ListMatches(list);
        }
		catch (IOException e) {
			throw new IllegalStateException(e);
        }
	}
	
	@SuppressWarnings("unchecked")
	private Set<String> getItens(Properties value, String prefix){
		
		Set<String> r = new HashSet<>();
		
		Enumeration<String> names = (Enumeration<String>) value.propertyNames();
		
		String regex = prefix.replace(".", "\\.") + "([_a-z0-9]+)(\\..*)?";
		Pattern pattern = Pattern.compile(regex);
		
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
			    String key = matcher.group(1);
				r.add(key);
			}
		}
		
		return r;
	}
	
}
