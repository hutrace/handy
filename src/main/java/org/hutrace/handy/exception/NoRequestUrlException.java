package org.hutrace.handy.exception;


/**
 * <p>没有找到对应请求地址的异常, 继承自{@link Exception}
 * <p>此异常用于提示没有找到对应的请求地址, 抛出此异常, 用于响应信息提示
 * @author hutrace
 * @see Exception
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NoRequestUrlException extends Exception {
	
	public NoRequestUrlException() {
		super();
	}

	public NoRequestUrlException(String message) {
		super(message);
	}

	public NoRequestUrlException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRequestUrlException(Throwable cause) {
		super(cause);
	}
	
}
