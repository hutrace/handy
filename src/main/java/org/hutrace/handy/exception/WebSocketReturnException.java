package org.hutrace.handy.exception;

/**
 * <p>WebSocket返回异常, 继承自{@link RuntimeException}
 * <p>此异常设计时想用于所有需要响应的父类, 然而写着的时候忘记了
 * <p>此异常目前用于WebSocket参数解析错误时调用以及在业务中使用
 * @author hutrace
 * @see RuntimeException
 * @since 1.8
 * @version 1.0
 */
public class WebSocketReturnException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int code;
	
	public WebSocketReturnException(int code, String msg) {
		super(msg);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

}
