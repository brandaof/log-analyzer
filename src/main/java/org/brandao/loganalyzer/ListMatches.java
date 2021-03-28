package org.brandao.loganalyzer;

import java.util.List;

public class ListMatches implements VarMatch{

	private List<String> list;
	
	public ListMatches(List<String> list) {
		this.list = list;
	}

	@Override
	public boolean matches(String value) {
		
		for(String p: list) {
			if(value.matches(p)) {
				return true;
			}
		}
		
		return false;
	}

}
