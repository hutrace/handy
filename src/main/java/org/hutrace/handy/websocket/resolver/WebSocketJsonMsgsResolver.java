package org.hutrace.handy.websocket.resolver;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.exception.WebSocketReturnException;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.WebSocketResultCodes;
import org.hutrace.handy.websocket.converter.WebSocketJsonMsgsConverter;
import org.hutrace.handy.websocket.converter.WebSocketMsgsConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class WebSocketJsonMsgsResolver implements WebSocketMsgsResolver {
	
	private static Logger log = LoggerFactory.getLogger(WebSocketJsonMsgsResolver.class);

	private static final String[] DATA_TYPE = {
		"json"
	};
	
	@Override
	public Object parse(WebSocketRequest request) throws ResolverException {
		JSONObject json;
		try{
			json = JSONObject.parseObject(request.body());
			if(json == null) {
				json = new JSONObject();
			}
			log.debug("serve.ws.request.data", "json", json);
			request.putParameters(json);
			return json;
		}catch (Exception e) {
			throw new WebSocketReturnException(WebSocketResultCodes.DATA_FORMAT_ERROR,
					ApplicationProperty.get("serve.ws.req.error.resolver"));
		}
	}

	@Override
	public String[] getDataType() {
		return DATA_TYPE;
	}

	@Override
	public WebSocketMsgsConverter converter() {
		return WebSocketJsonMsgsConverter.instance;
	}
	
}
