package org.hutrace.handy.http.converter;

import java.lang.reflect.Parameter;

import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.file.HttpFile;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * <p>HttpFile消息转换器
 * <p>此转换器将封装好的{@link HttpFile}实现类转发给映射器
 * @author hutrace
 * @see HttpMsgsConverter
 * @see HttpFile
 * @since 1.8
 * @version 1.0
 * @time 2019年6月11日
 */
public class HttpFileConverter implements HttpMsgsConverter {

	public static final HttpFileConverter instance = new HttpFileConverter();
	
	@Override
	public Object read(HttpRequest request, String parameterName,
			Parameter parameter, Object msg, boolean verify)
			throws ConverterException {
		if(!(msg instanceof HttpFile)) {
			throw new ConverterException("Parameter conversion failed. Converter error");
		}
		if(HttpFile.class.isAssignableFrom(parameter.getType())) {
			return msg;
		}
		Class<?> clazs = parameter.getType();
		return TypeUtils.castToJavaBean(request.parameter(parameterName), clazs);
	}
	
}
