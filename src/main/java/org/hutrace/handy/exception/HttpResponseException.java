package org.hutrace.handy.exception;

/**
 * 此异常是response响应时出现的异常，
 * <p>所以异常管理不应该对它做处理。
 * @author hu trace
 *
 */
public class HttpResponseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public HttpResponseException() {
        super();
    }

    public HttpResponseException(String message) {
        super(message);
    }

    public HttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseException(Throwable cause) {
        super(cause);
    }
	
}
