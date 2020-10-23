package org.hutrace.handy.websocket.cache;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.websocket.WebSocketResult;

public class OutputClient {
	
	protected void writeAndFlush(String id, String event, Object data) {
		writeAndFlush(id, event, 0, "ok", data);
	}
	
	protected void writeAndFlush(String id, String event, int code, String msg, Object data) {
		WebSocketResult result = new WebSocketResult();
		result.setCode(code).setData(data).setEvent(event).setMsg(msg);
		writeAndFlush(id, result);
	}
	
	protected void writeAndFlush(String id, WebSocketResult result) {
		Configuration.wsResultHandler().response(ClientCache.get(id), result);
	}
	
}
