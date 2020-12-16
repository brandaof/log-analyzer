package org.brandao.loganalyzer;

import java.io.Serializable;

import org.brandao.string.StringTemplate;

public class Action implements CharSequence{

	private String id;
	
	private Serializable expression;
	
	private String eventID;
	
	private StringTemplate executor;

	public Action(String id, Serializable expression, String eventID, StringTemplate executor) {
		this.id = id;
		this.expression = expression;
		this.eventID = eventID;
		this.executor = executor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Serializable getExpression() {
		return expression;
	}

	public void setExpression(Serializable expression) {
		this.expression = expression;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public StringTemplate getExecutor() {
		return executor;
	}

	public void setExecutor(StringTemplate executor) {
		this.executor = executor;
	}

	@Override
	public int length() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public char charAt(int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
