package org.hutrace.handy.websocket.cache;

import org.hutrace.handy.websocket.WebSocketResponse;

public class InputClient {
	
	protected void add(String id, WebSocketResponse response) {
		ClientCache.set(id, response);
	}
	
	protected void remove(String id) {
		ClientCache.remove(id);
	}
	
	protected WebSocketResponse get(String id) {
		return ClientCache.get(id);
	}
	
}
