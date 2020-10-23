package org.hutrace.handy.server;

import org.hutrace.handy.websocket.DefaultWebSocketResponse;
import org.hutrace.handy.websocket.WebSocketResult;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class WebSocketAstrictResponse extends DefaultWebSocketResponse {

	public WebSocketAstrictResponse(ChannelHandlerContext context) {
		super(context);
	}
	
	public void writeAndFlush(byte[] bytes) {
		writeAndFlush(Unpooled.wrappedBuffer(bytes));
	}
	
	public void writeAndFlush(WebSocketResult result) {
		writeAndFlush(result.bytes());
	}
	
	public void writeAndFlush(int code, String msg) {
		writeAndFlush(code, msg, null);
	}
	
	public void writeAndFlush(int code, String msg, Object data) {
		writeAndFlush(new WebSocketResult().setCode(code).setMsg(msg).setData(data));
	}

}
