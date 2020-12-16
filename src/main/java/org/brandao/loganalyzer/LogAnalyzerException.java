package org.brandao.loganalyzer;

public class LogAnalyzerException extends Exception{

	private static final long serialVersionUID = 5430020751497427312L;

	public LogAnalyzerException() {
		super();
	}

	public LogAnalyzerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogAnalyzerException(String message) {
		super(message);
	}

	public LogAnalyzerException(Throwable cause) {
		super(cause);
	}

}
