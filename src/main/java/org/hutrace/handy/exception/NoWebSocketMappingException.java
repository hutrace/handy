package org.hutrace.handy.exception;

/**
 * <p>没有找到WebSocket对应的映射异常, 继承自{@link Exception}
 * <p>此异常在使用WebSocket时, 分发调用控制器时, 没有找到对应的控制器会抛出此异常, 进行响应提示
 * @author hutrace
 * @see Exception
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class NoWebSocketMappingException extends Exception {
	
	public NoWebSocketMappingException() {
		super();
	}

	public NoWebSocketMappingException(String message) {
		super(message);
	}

	public NoWebSocketMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoWebSocketMappingException(Throwable cause) {
		super(cause);
	}
	
}
