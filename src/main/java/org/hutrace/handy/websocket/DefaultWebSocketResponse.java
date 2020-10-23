package org.hutrace.handy.websocket;

import org.hutrace.handy.exception.WebSocketResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * <p>WebSocket响应类
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public abstract class DefaultWebSocketResponse implements WebSocketResponse {
	
	protected Logger log = LoggerFactory.getLogger(WebSocketResponse.class);
	
	protected ChannelHandlerContext context;
	
	protected ByteBuf data;
	
	protected static final WebSocketResponseException exception = new WebSocketResponseException();
	
	public DefaultWebSocketResponse(ChannelHandlerContext context) {
		this.context = context;
	}
	
	protected void init() {
		data = null;
	}
	
	@Override
	public void write(WebSocketResult result) {
		write(result.bytes());
	}

	@Override
	public void write(int code, String msg) {
		write(code, msg, null);
	}

	@Override
	public void write(int code, String msg, Object data) {
		if(this.data == null) {
			WebSocketResult result = new WebSocketResult(); 
			write(result.setCode(code).setMsg(msg).setData(data));
		}
	}

	@Override
	public void write(byte[] bytes) {
		write(Unpooled.wrappedBuffer(bytes));
	}

	@Override
	public void write(ByteBuf binaryData) {
		if(data == null) {
			data = binaryData;
		}
	}

	@Override
	public void finish() {
		if(data == null) {
			throw new NullPointerException("The data is empty, Please call the write method");
		}
		throw exception;
	}
	
	public void writeAndFlush() {
		writeAndFlush(data);
	}
	
	public void writeAndFlush(ByteBuf binaryData) {
		context.channel().writeAndFlush(new TextWebSocketFrame(binaryData));
		log.debug("Sending data to the client is complete");
		init();
	}

	@Override
	public void writeAndFlush(WebSocketResult result) {
		writeAndFlush(Unpooled.wrappedBuffer(result.bytes()));
	}

	@Override
	public void writeAndFlush(int code, String msg) {
		writeAndFlush(code, msg, null);
	}

	@Override
	public void writeAndFlush(int code, String msg, Object data) {
		WebSocketResult result = new WebSocketResult(); 
		writeAndFlush(result.setCode(code).setMsg(msg).setData(data));
	}

	@Override
	public String id() {
		return context.channel().id().toString();
	}

	@Override
	public void flush() {
		context.channel().flush();
	}

	@Override
	public void close() {
		context.channel().close();
	}

	@Override
	@Deprecated
	public void setHeader(String name, String value) {
		
	}
	
	@Override
	@Deprecated
	public void setHeader(CharSequence name, Object value) {
		
	}

	@Override
	@Deprecated
	public String header(CharSequence name) {
		return null;
	}

	@Override
	@Deprecated
	public void removeHeader(CharSequence name) {
		
	}
	
}
