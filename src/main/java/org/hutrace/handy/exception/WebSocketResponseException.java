package org.hutrace.handy.exception;

import org.hutrace.handy.websocket.WebSocketResponse;

/**
 * <p>WebSocket响应异常, 继承自{@link RuntimeException}
 * <p>此异常用于中断业务流程使用, 在{@link WebSocketResponse#finish()}时进行了调用, 实现终止流程的目的
 * @author hutrace
 * @see RuntimeException
 * @since 1.8
 * @version 1.0
 */
public class WebSocketResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WebSocketResponseException() {
		super();
	}
	
}
