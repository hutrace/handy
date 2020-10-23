package org.hutrace.handy.websocket.resolver;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.converter.WebSocketMsgsConverter;

public interface WebSocketMsgsResolver {
	
	/**
	 * <p>此方法作用于解析参数，自行根据method判断取得消息
	 * @param request
	 * @throws ResolverException
	 */
	public Object parse(WebSocketRequest request) throws ResolverException;
	
	/**
	 * <p>设置当前解析器需要解析对应的ContentType
	 * @return ContentType
	 */
	public String[] getDataType();
	
	/**
	 * <p>根据contentType判断是否使用此解析器
	 * @param contentType
	 * @return
	 */
	public default boolean supports(String contentType) {
		if(contentType == null) {
			return true;
		}
		String[] contentTyeps = getDataType();
		for(int i = 0; i < contentTyeps.length; i++) {
			if(contentTyeps[i].equals(contentType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>设置当前解析器解析过后的转发器
	 * @return HttpMsgsConverter
	 */
	public WebSocketMsgsConverter converter();
	
}
