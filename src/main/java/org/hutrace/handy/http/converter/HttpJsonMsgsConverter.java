package org.hutrace.handy.http.converter;

import java.lang.reflect.Parameter;

import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.validate.ValidateJSONObject;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.utils.ServeUtil;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * <p>JSON参数转换器, 实现{@link HttpMsgsConverter}接口, 达到转换器的作用
 * <p>此转换器用于对JSON数据进行转换
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @see HttpMsgsConverter
 * @since 1.8
 * @version 1.0
 * @time 2019年5月15日
 */
public class HttpJsonMsgsConverter implements HttpMsgsConverter {
	
	public static final HttpJsonMsgsConverter instance = new HttpJsonMsgsConverter();

	@Override
	public Object read(HttpRequest request, String parameterName, Parameter parameter, Object msg, boolean verify) throws ConverterException {
		if(!(msg instanceof JSONObject)) {
			throw new ConverterException(ApplicationProperty.get("serve.req.error.converter"));
		}
		Class<?> clazs = parameter.getType();
		JSONObject json = verify ? new ValidateJSONObject((JSONObject) msg) : (JSONObject) msg;
		json.putAll(request.parameters());
		if(ServeUtil.simpleType(clazs)) {
			Object obj = request.parameter(parameterName);
			if(obj == null) {
				return json.getObject(parameterName, clazs);
			}else {
				return TypeUtils.castToJavaBean(obj, clazs);
			}
		}else {
			return json.toJavaObject(clazs);
		}
	}

}
