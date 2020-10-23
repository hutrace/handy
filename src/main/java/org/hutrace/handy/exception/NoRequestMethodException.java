package org.hutrace.handy.exception;


/**
 * <p>没有找到对应模块的异常, 继承自{@link Exception}
 * <p>此异常用于提示没有找到对应的模块, 如: 请求时是GET请求, 控制器中定义的为POST, 则会抛出此异常, 用于响应信息提示
 * @author hutrace
 * @see Exception
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NoRequestMethodException extends Exception {
	
	public NoRequestMethodException() {
		super();
	}

	public NoRequestMethodException(String message) {
		super(message);
	}

	public NoRequestMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRequestMethodException(Throwable cause) {
		super(cause);
	}
	
}
