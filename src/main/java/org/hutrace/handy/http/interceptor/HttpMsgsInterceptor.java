package org.hutrace.handy.http.interceptor;

import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;

/**
 * <p>拦截器接口规范
 * <p>实现此接口达到拦截器编程实现
 * <p>可以分别在请求时、响应时、响应完成后进行相应的处理
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public interface HttpMsgsInterceptor {
	
	/**
	 * <p>请求时拦截，在参数解析后执行拦截
	 * @param request
	 * @param response
	 * @return 返回true时代表通过拦截验证，返回false表示未通过拦截验证。
	 */
	public boolean request(HttpRequest request, HttpResponse response) throws Exception;
	
}
