package org.hutrace.handy.exception;

/**
 * <p>应用程序加载时出现的异常类，继承{@link HandyserveException}
 * <p>支持之定义code: 响应状态码, msg: 错误信息
 * @author hutrace
 * @see HandyserveException
 * @since 1.8
 * @version 1.0
 */
public class AppLoaderException extends HandyserveException {
	
	private static final long serialVersionUID = 1L;

	public AppLoaderException(String msg) {
		super(msg);
	}
	
	public AppLoaderException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AppLoaderException(int code, String msg) {
		super(code, msg);
	}
	
	public AppLoaderException(Throwable e) {
		super(e);
	}
	
}
