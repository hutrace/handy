package org.hutrace.handy.http.resolver;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.converter.HttpMsgsConverter;

/**
 * <p>参数解析器规范
 * <p>继承可后可加入参数解析
 * <p>解析规则，根据{@link #getContentType}获取到的值对应消息头的Content-Type值，匹配则使用此解析器
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public interface HttpMsgsResolver {
	
	/**
	 * <p>此方法作用于解析参数，自行根据method判断取得消息
	 * @param request
	 * @throws ResolverException
	 */
	Object parse(HttpRequest request) throws ResolverException;
	
	/**
	 * <p>设置当前解析器需要解析对应的ContentType
	 * @return ContentType
	 */
	String[] getContentType();
	
	/**
	 * <p>根据contentType判断是否使用此解析器
	 * @param contentType
	 * @return
	 */
	default boolean supports(String contentType) {
		if(contentType == null) {
			return true;
		}
		String[] contentTyeps = getContentType();
		for(int i = 0; i < contentTyeps.length; i++) {
			if(contentTyeps[i].equalsIgnoreCase(contentType.split(";")[0])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>设置当前解析器解析过后的转发器
	 * @return HttpMsgsConverter
	 */
	HttpMsgsConverter converter();
	
	/**
	 * <p>获取当前解析器支持的最大内容长度
	 * <p>默认为系统设置的最大内容长度
	 * @return 当前解析器支持的最大内容长度
	 */
	default int getMaxContentLength() {
		return Configuration.maxContentLength();
	}
	
	default Object write(HttpRequest request, HttpResponse response, Object msg) throws ResolverException {
		return msg;
	}
	
}
