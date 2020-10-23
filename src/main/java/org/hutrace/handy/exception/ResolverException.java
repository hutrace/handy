package org.hutrace.handy.exception;

/**
 * <p>参数解析异常
 * <p>统一继承{@link ServerException}
 * @author hutrace
 * @see ServerException
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ResolverException extends HandyserveException {
	
	public ResolverException(String msg) {
		super(msg);
	}
	
	public ResolverException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
