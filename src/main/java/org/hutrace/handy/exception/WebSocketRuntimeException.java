package org.hutrace.handy.exception;

import org.hutrace.handy.websocket.WebSocketResultCodes;

/**
 * <p>WebSocket允许异常, 继承自{@link RuntimeException}
 * <p>此异常用于捕获{@link WebSocketResultCodes}中对应的错误信息进行响应给客户端
 * @author hutrace
 * @see RuntimeException
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class WebSocketRuntimeException extends RuntimeException {
	
	private int code;
	private String msg;
	public WebSocketRuntimeException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}
	
	public WebSocketRuntimeException(int code, Throwable e) {
		super(e);
		this.code = code;
		this.msg = e.getMessage();
	}

	public int code() {
		return code;
	}

	public String msg() {
		return msg;
	}
	
	
	
}
