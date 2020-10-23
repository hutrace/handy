package org.hutrace.handy.exception;

import org.hutrace.handy.http.ResponseCode;

/**
 * <p>继承自{@link Exception}
 * <p>自定义异常, 简称业务异常, 在业务层可使用此异常来实现异常错误响应提示
 * <p>此异常中包含
 * <pre>
 *  code: 响应状态码, 默认是{@link ResponseCode#ERROR_CUSTOM}, 非必要情况不比修改
 *  msg: 响应提示信息
 * @author hutrace
 * @see Exception
 * @see ResponseCode
 * @since 1.8
 * @version 1.0
 */
public class CustomException extends Exception {

	private static final long serialVersionUID = -2478462184871123622L;
	
	private int code = ResponseCode.ERROR_CUSTOM;
	private String msg;
	
	public CustomException() {
		super();
	}
	
	public CustomException(int code) {
		super();
		this.code = code;
	}
	public CustomException(Throwable e) {
		super(e);
		this.msg = e.getMessage();
	}
	
	public CustomException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = e.getMessage();
	}
	
	public CustomException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public CustomException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
	public CustomException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public CustomException(int code, String msg, Throwable e) {
		super(msg, e);
		this.code = code;
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}

	public int getCode() {
		return code;
	}

}
