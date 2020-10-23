package org.hutrace.handy.exception;

import org.hutrace.handy.websocket.WebSocketResultCodes;

/**
 * <p>WebSocket消息头解析异常, 继承自{@link WebSocketReturnException}
 * <p>此异常在解析WebSocket消息头错误或不匹配时将会抛出, 并将错误信息响应给客户端
 * @author hutrace
 * @see WebSocketReturnException
 * @since 1.8
 * @version 1.0
 */
public class WebSocketHeaderParseException extends WebSocketReturnException {

	private static final long serialVersionUID = 1L;
	
	public WebSocketHeaderParseException(String msg) {
		super(WebSocketResultCodes.MSG_HEADER_ERROR, msg);
	}
	
}
