package org.hutrace.handy.exception;

/**
 * <p>参数转换异常
 * <p>统一继承{@link HandyserveException}
 * @author hutrace
 * @see HandyserveException
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConverterException extends HandyserveException {
	
	public ConverterException(String msg) {
		super(msg);
	}
	
	public ConverterException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public ConverterException(int code, String msg) {
		super(code, msg);
	}
	
}
