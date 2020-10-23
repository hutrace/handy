package org.hutrace.handy.http.resolver;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.http.converter.HttpQueryStringMsgsConverter;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.utils.qs.QueryString;
import org.hutrace.handy.utils.qs.QueryStringObject;

import io.netty.handler.codec.http.HttpMethod;

/**
 * <p>QueryString参数解析器
 * @author hutrace
 * @see HttpMsgsResolver
 * @since 1.8
 * @version 1.0
 */
public class HttpQueryStringMsgsResolver implements HttpMsgsResolver {
	
	private static Logger log = LoggerFactory.getLogger(HttpQueryStringMsgsResolver.class);
	
	private static final String[] CONTENT_TYPE = {
		"application/x-www-form-urlencoded"
	};
	
	@Override
	public Object parse(HttpRequest request) throws ResolverException {
		if(HttpMethod.GET.equals(request.method())) {
			return resolverGet(request);
		}else {
			return resolverBody(request);
		}
	}
	
	private String charset(String ct) throws ResolverException {
		String[] arr = ct.split(";");
		if(arr.length == 2) {
			try {
				return arr[1].trim().split("=")[1];
			}catch (Exception e) {
				throw new ResolverException(SystemProperty.get("serve.req.error.qs.resolver", ct));
			}
		}
		return Configuration.charset();
	}

	/**
	 * <p>根据uri取得参数
	 * @param request
	 * @return
	 */
	private QueryStringObject resolverGet(HttpRequest request) {
		return new QueryStringObject(request.parameters());
	}
	
	/**
	 * <p>解析body
	 * @param request
	 * @return
	 * @throws ResolverException
	 */
	private QueryStringObject resolverBody(HttpRequest request) throws ResolverException {
		log.debug("serve.request.data", "'QueryString'", request.body());
		QueryStringObject qso = QueryString.parse(request.body(), charset(request.contentType()));
		if(qso == null) {
			qso = new QueryStringObject();
		}
		qso.innerMap().putAll(request.parameters());
		return qso;
	}
	
	@Override
	public String[] getContentType() {
		return CONTENT_TYPE;
	}

	@Override
	public HttpMsgsConverter converter() {
		return HttpQueryStringMsgsConverter.instance;
	}

}
