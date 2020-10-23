package org.hutrace.handy.http.exception;

import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;

public interface ExceptionHandler {
	
	/**
	 * 异常处理器处理方法
	 * <pre>
	 *  实现者需要注意，此处理方法可以有两种响应方式
	 *    你可以将信息直接写成你需要的格式，然后返回
	 *    也可以使用response的write方法进行响应，返回null即可
	 * @param request
	 * @param response
	 * @param ex
	 * @return
	 */
	Object dispose(HttpRequest request, HttpResponse response, ExceptionBean ex);
	
}
