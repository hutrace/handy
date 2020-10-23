package org.hutrace.handy.exception;

/**
 * <p>qs解析转换异常
 * <p>继承{@link RuntimeException}
 * @author hutrace
 */
public class QueryStringException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public QueryStringException() {
		super();
	}
	
	public QueryStringException(String msg) {
		super(msg);
	}

	public QueryStringException(Throwable e) {
		super(e);
	}

	public QueryStringException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
