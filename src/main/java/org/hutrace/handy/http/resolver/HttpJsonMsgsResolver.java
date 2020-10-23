package org.hutrace.handy.http.resolver;

import java.util.HashMap;
import java.util.Map;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.converter.HttpJsonMsgsConverter;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpMethod;

/**
 * <p>JSON参数解析器，将消息解析为JSON数据
 * @author hutrace
 * @see {@link HttpMsgsResolver}
 * @since 1.8
 * @version 1.0
 */
public class HttpJsonMsgsResolver implements HttpMsgsResolver {
	
	private static Logger log = LoggerFactory.getLogger(HttpJsonMsgsResolver.class);
	
	private static final String[] CONTENT_TYPE = {
		"application/json"
	};
	
	@Override
	public Object parse(HttpRequest request) throws ResolverException {
		if(HttpMethod.GET.equals(request.method())) {
			return resolverGet(request);
		}else {
			return resolverBody(request);
		}
	}
	
	/**
	 * <p>根据uri取得参数
	 * @param request
	 * @return
	 */
	private JSONObject resolverGet(HttpRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(request.parameters());
		return new JSONObject(params);
	}
	
	/**
	 * <p>解析body
	 * @param request
	 * @return
	 * @throws ResolverException
	 */
	private JSONObject resolverBody(HttpRequest request) throws ResolverException {
		log.debug("serve.request.data", "'JSON'", request.body());
		JSONObject json = JSONObject.parseObject(request.body());
		if(json == null) {
			json = new JSONObject();
		}
		json.putAll(request.parameters());
		return json;
	}
	
	@Override
	public String[] getContentType() {
		return CONTENT_TYPE;
	}

	@Override
	public HttpMsgsConverter converter() {
		return HttpJsonMsgsConverter.instance;
	}

}
