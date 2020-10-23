package org.hutrace.handy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class ServerChannelHandler extends SimpleChannelInboundHandler<Object> {
	
	private SocketChannel channel;
	private MsgHandler handler;
	
	ServerChannelHandler(SocketChannel channel) {
		this.channel = channel;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(msg instanceof FullHttpRequest) {
			handler = new HttpHandler();
		}else if(msg instanceof WebSocketFrame) {
			handler = new WebSocketHandler();
		}else {
			ctx.close();
			channel.close();
			return;
		}
		transmit(ctx, msg);
	}
	
	public void transmit(ChannelHandlerContext ctx, Object msg) throws Exception {
		handler.channelRead(ctx, msg);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(handler != null) {
			handler.handlerRemoved(ctx);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		if(handler != null) {
			handler.exceptionCaught(ctx, cause);
		}
	}
	
}
