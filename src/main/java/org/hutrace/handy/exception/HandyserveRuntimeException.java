package org.hutrace.handy.exception;

import org.hutrace.handy.http.ResponseCode;

/**
 * <p>服务器运行异常, 继承自{@link RuntimeException}
 * <p>此异常用于在服务器执行过程中捕获到{@link ResponseCode}中对应的错误信息后进行终止并相应错误信息
 * @author hutrace
 * @see RuntimeException
 * @see ResponseCode
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class HandyserveRuntimeException extends RuntimeException {
	
	private int code;
	private String msg;
	public HandyserveRuntimeException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
	public HandyserveRuntimeException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = e.getMessage();
	}
	
	public HandyserveRuntimeException(String msg) {
		super(msg);
		this.code = -1;
		this.msg = msg;
	}

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}
	
}
