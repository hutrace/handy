package org.hutrace.handy.http.resolver;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.converter.HttpJsonMsgsConverter;
import org.hutrace.handy.http.converter.HttpMsgsConverter;

import com.alibaba.fastjson.JSONObject;

public class HttpEmptyResolver implements HttpMsgsResolver {
	
	private String[] contentType = {""};
	
	@Override
	public Object parse(HttpRequest request) throws ResolverException {
		JSONObject json = new JSONObject();
		json.putAll(request.parameters());
		return json;
	}

	@Override
	public String[] getContentType() {
		return contentType;
	}

	@Override
	public HttpMsgsConverter converter() {
		return HttpJsonMsgsConverter.instance;
	}

	

}
