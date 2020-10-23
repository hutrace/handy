package org.hutrace.handy.websocket.converter;

import java.lang.reflect.Parameter;

import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.http.validate.ValidateJSONObject;
import org.hutrace.handy.utils.ServeUtil;
import org.hutrace.handy.websocket.WebSocketRequest;

import com.alibaba.fastjson.JSONObject;

public class WebSocketJsonMsgsConverter implements WebSocketMsgsConverter {

	public static final WebSocketJsonMsgsConverter instance = new WebSocketJsonMsgsConverter();
	
	@Override
	public Object read(WebSocketRequest request, String parameterName, Parameter parameter, Object msg, boolean verify) throws ConverterException {
		if(!(msg instanceof JSONObject)) {
			throw new ConverterException("Parameter conversion failed. Converter error");
		}
		Class<?> clazs = parameter.getType();
		JSONObject json = verify ? new ValidateJSONObject((JSONObject) msg) : (JSONObject) msg;
		if(ServeUtil.simpleType(clazs)) {
			return json.getObject(parameterName, clazs);
		}else {
			return json.toJavaObject(clazs);
		}
	}

}
