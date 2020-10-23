package org.hutrace.handy.websocket.converter;

import java.lang.reflect.Parameter;

import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.websocket.WebSocketRequest;

/**
 * <p>参数转换器接口规范
 * @author hutrace
 * @since 1.8
 * @version 1.0
 * @param <T>
 */
public interface WebSocketMsgsConverter {
	
	/**
	 * <p>读取参数，进行转换成控制器的参数类型
	 * <p>有几个参数将会进入多少次，request和response除外
	 * @param clazs
	 * @param parameter
	 * @param msg
	 * @return 转换过后的参数
	 * @throws ConverterException
	 */
	public Object read(WebSocketRequest request, String parameterName, Parameter parameter, Object msg, boolean verify) throws ConverterException;
	
}
