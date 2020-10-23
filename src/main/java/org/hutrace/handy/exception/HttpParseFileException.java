package org.hutrace.handy.exception;

public class HttpParseFileException extends ResolverException {

	private static final long serialVersionUID = -4006573996881880030L;

	public HttpParseFileException(String msg) {
		super(msg);
	}
	
	public HttpParseFileException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
