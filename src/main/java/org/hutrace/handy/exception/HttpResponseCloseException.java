package org.hutrace.handy.exception;

/**
 * 该异常表示response已经close
 * @author hu trace
 *
 */
public class HttpResponseCloseException extends HttpResponseException {
	
	private static final long serialVersionUID = 1L;
	
	public HttpResponseCloseException() {
        super();
    }

    public HttpResponseCloseException(String message) {
        super(message);
    }

    public HttpResponseCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpResponseCloseException(Throwable cause) {
        super(cause);
    }
	
}
