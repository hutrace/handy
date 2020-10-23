package org.hutrace.handy.websocket;

import org.hutrace.handy.server.ServerResponse;

import io.netty.buffer.ByteBuf;

/**
 * <p>WebSocket响应类
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public interface WebSocketResponse extends ServerResponse {
	
	void write(WebSocketResult result);
	
	void write(int code, String msg);
	
	void write(int code, String msg, Object data);
	
	@Override
	void write(byte[] bytes);
	
	void write(ByteBuf binaryData);
	
	void finish();
	
	void setHeader(String name, String value);
	
	void writeAndFlush(ByteBuf binaryData);
	
	void writeAndFlush();
	
	void writeAndFlush(WebSocketResult result);
	
	void writeAndFlush(int code, String msg);
	
	void writeAndFlush(int code, String msg, Object data);
	
}
