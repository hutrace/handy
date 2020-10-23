package org.hutrace.handy.exception;

import org.hutrace.handy.websocket.WebSocketResultCodes;

/**
 * <p>连接异常
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class WebSocketConnectException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private int code = WebSocketResultCodes.ERROR_CUSTOM;
	
	public WebSocketConnectException(String msg) {
		super(msg);
	}
	
	public WebSocketConnectException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
}
