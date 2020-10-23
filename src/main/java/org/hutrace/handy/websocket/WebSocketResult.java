package org.hutrace.handy.websocket;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class WebSocketResult {
	
	private static final String KEY_MSG = "msg";
	private static final String KEY_CODE = "code";
	private static final String KEY_DATA = "data";
	private static final String KEY_EVENT = "event";
	private static final String KEY_TIMESTAMP = "timestamp";
	
	protected Map<String, Object> map;
	
	public WebSocketResult() {
		map = new HashMap<>(8);
		map.put(KEY_TIMESTAMP, System.currentTimeMillis());
		map.put(KEY_CODE, WebSocketResultCodes.SUCCESS);
	}
	
	public WebSocketResult setMsg(String msg) {
		map.put(KEY_MSG, msg);
		return this;
	}
	
	public WebSocketResult setCode(int code) {
		map.put(KEY_CODE, code);
		return this;
	}
	
	public WebSocketResult setData(Object data) {
		map.put(KEY_DATA, data);
		return this;
	}
	
	public WebSocketResult setEvent(String event) {
		map.put(KEY_EVENT, event);
		return this;
	}
	
	@Override
	public String toString() {
		return JSONObject.toJSONString(map, SerializerFeature.WriteDateUseDateFormat);
	}
	
	public byte[] bytes() {
		return JSONObject.toJSONBytes(map, SerializerFeature.WriteDateUseDateFormat);
	}
	
}
