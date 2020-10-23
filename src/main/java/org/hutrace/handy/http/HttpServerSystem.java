package org.hutrace.handy.http;

/**
 * 通过继承此类可以实现对request中的私有方法进行调用
 * @author hutrace
 */
public class HttpServerSystem {
	
	protected void setRequestParameter(HttpServerRequest request, String name, String value) {
		request.setParameter(name, value);
	}
	
}
