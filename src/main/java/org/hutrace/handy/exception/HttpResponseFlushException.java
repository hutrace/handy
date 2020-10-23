package org.hutrace.handy.exception;

/**
 * 该异常表示response已经flush
 * @author hu trace
 *
 */
public class HttpResponseFlushException extends HttpResponseException {
	
	private static final long serialVersionUID = 1L;
	
	public HttpResponseFlushException() {
        super();
    }

    public HttpResponseFlushException(String message) {
        super(message);
    }

    public HttpResponseFlushException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseFlushException(Throwable cause) {
        super(cause);
    }
	
}
