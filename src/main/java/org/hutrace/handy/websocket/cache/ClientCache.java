package org.hutrace.handy.websocket.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hutrace.handy.websocket.WebSocketResponse;

public class ClientCache {
	
	private static final Map<String, WebSocketResponse> cache;
	
	static {
		cache = new ConcurrentHashMap<>();
	}
	
	static void set(String id, WebSocketResponse value) {
		cache.put(id, value);
	}
	
	static WebSocketResponse get(String id) {
		return cache.get(id);
	}
	
	static void remove(String key) {
		cache.remove(key);
	}
	
}
