package org.hutrace.handy.http.result;

import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;

/**
 * 响应类标准接口
 * @author hu trace
 *
 */
public interface ResultHandler {
	
	/**
	 * 处理你想要响应的数据进行响应
	 * <p>你可以直接通过response.write进行响应
	 * <p>也可以通过标准接口{@link ResultBean}进行响应
	 * <p>建议使用标准接口{@link ResultBean}进行响应
	 * @param request
	 * @param response
	 * @param msg
	 */
	ResultBean dispose(HttpRequest request, HttpResponse response, Object msg);
	
}
