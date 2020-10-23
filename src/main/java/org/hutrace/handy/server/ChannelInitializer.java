package org.hutrace.handy.server;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.EventExecutorGroup;

public class ChannelInitializer extends io.netty.channel.ChannelInitializer<SocketChannel> {
	
	private EventExecutorGroup businessGroup;
	
	public ChannelInitializer(EventExecutorGroup businessGroup) {
		this.businessGroup = businessGroup;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 请求解码器
		ch.pipeline().addLast("http-decoder", new HttpServerCodec());
		// 将HTTP消息的多个部分合成一条完整的HTTP消息
		ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE)); //65535
		// 解决大码流的问题，ChunkedWriteHandler：向客户端发送HTML5文件
		ch.pipeline().addLast("chunked-handler", new ChunkedWriteHandler());
		// WebSocket的请求地址
		ch.pipeline().addLast("websocket-protocol-handler", new WebSocketServerProtocolHandler("/ws"));
		// 自定义处理handler
		ch.pipeline().addLast(businessGroup, "channel-handler", new ServerChannelHandler(ch));
	}
	
}
