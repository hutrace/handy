package org.hutrace.handy.exception;

/**
 * <p>参数验证异常, 继承自{@link RuntimeException}
 * <p>此异常用于参数验证时出现不匹配时抛出, 抛出此异常将会终止流程, 将异常信息响应至客户端
 * <pre>可设置值
 *  {@link ValidateRuntimeException#key}
 *  {@link ValidateRuntimeException#msg}
 * @author hutrace
 * @see 
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ValidateRuntimeException extends RuntimeException {

	public ValidateRuntimeException(Throwable e) {
		super(e);
	}
	
	public ValidateRuntimeException(String msg) {
		super(msg);
	}
	
	public ValidateRuntimeException(String msg, Throwable e) {
		super(msg, e);
	}

}
