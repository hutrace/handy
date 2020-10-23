package org.hutrace.handy.http.resolver;

import java.util.HashMap;
import java.util.Map;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpServerRequest;
import org.hutrace.handy.http.converter.HttpJsonMsgsConverter;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.utils.xml.XMLObject;

import com.alibaba.fastjson.JSONObject;

import io.netty.handler.codec.http.HttpMethod;

/**
 * <p>XML请求参数解析器
 * @author hutrace
 * @see HttpMsgsResolver
 * @see XMLObject
 * @since 1.8
 * @version 1.0
 */
public class HttpXmlMsgsResolver implements HttpMsgsResolver {

	private static Logger log = LoggerFactory.getLogger(HttpXmlMsgsResolver.class);
	
	private static final String[] CONTENT_TYPE = {
		"text/xml"
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
	 * <p>因为此处添加了日志打印, 打印的是原始XML格式字符串, 所以解析时也应用的是字符串解析, 如果不打印日志, 调用{@link XMLObject#parse(io.netty.buffer.ByteBuf)}将会提高效率
	 * @param request {@link HttpServerRequest}
	 * @return 此处返回的是{@link JSONObject}
	 * @throws ResolverException 解析错误时将抛出此异常
	 */
	private JSONObject resolverBody(HttpRequest request) throws ResolverException {
		log.debug("serve.request.data", "'XML'", request.body());
		JSONObject json = XMLObject.parse(request.body());
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
