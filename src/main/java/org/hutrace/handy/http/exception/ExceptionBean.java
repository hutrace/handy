package org.hutrace.handy.http.exception;

public class ExceptionBean {
	
	public static final ExceptionBean EMPTY = new ExceptionBean();
	
	private Throwable ex;
	private int code;
	private String msg;
	
	private ExceptionBean() {}
	
	public ExceptionBean(Throwable ex, int code, String msg) {
		this.ex = ex;
		this.code = code;
		this.msg = msg;
	}
	public Throwable getEx() {
		return ex;
	}
	public void setEx(Throwable ex) {
		this.ex = ex;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
