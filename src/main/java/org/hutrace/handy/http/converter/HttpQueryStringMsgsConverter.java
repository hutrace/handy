package org.hutrace.handy.http.converter;

import java.lang.reflect.Parameter;

import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.validate.qs.ValidateQueryStringObject;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.utils.ServeUtil;
import org.hutrace.handy.utils.qs.QueryStringObject;

import com.alibaba.fastjson.util.TypeUtils;

public class HttpQueryStringMsgsConverter implements HttpMsgsConverter {
	
	public static final HttpQueryStringMsgsConverter instance = new HttpQueryStringMsgsConverter();

	@Override
	public Object read(HttpRequest request, String parameterName, Parameter parameter, Object msg, boolean verify) throws ConverterException {
		if(!(msg instanceof QueryStringObject)) {
			throw new ConverterException(ApplicationProperty.get("serve.req.error.converter"));
		}
		Class<?> clazs = parameter.getType();
		QueryStringObject qs = verify ? new ValidateQueryStringObject((QueryStringObject) msg) : (QueryStringObject) msg;
		qs.innerMap().putAll(request.parameters());
		if(ServeUtil.simpleType(clazs)) {
			Object obj = request.parameter(parameterName);
			if(obj == null) {
				obj = qs.get(parameterName);
			}
			return TypeUtils.castToJavaBean(obj, clazs);
		}else {
			return qs.toJavaBean(clazs);
		}
	}

}