package org.hutrace.handy.websocket.connect;

import org.hutrace.handy.exception.WebSocketConnectException;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.WebSocketResponse;

/**
 * <p>WebSocket自定义连接接口
 * <p>实现此接口即可加入连接管理
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public interface WebSocketConnect {
	
	void onConnect(WebSocketRequest request, WebSocketResponse response) throws WebSocketConnectException;
	
	void onClose(WebSocketRequest request);
	
}
