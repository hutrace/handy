package org.hutrace.handy.server;

import io.netty.channel.ChannelHandlerContext;

/**
 * <p>公共MsgHandler处理接口
 * @author hu trace
 * @since 1.8
 * @version 1.0
 */
public interface MsgHandler {
	
	/**
	 * <p>读取通道数据
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception;
	
	/**
	 * <p>有客户端退出
	 * @param ctx
	 * @throws Exception
	 */
	default void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		ctx.close();
	}
	
	/**
	 * <p>有客户端异常
	 * @param ctx
	 * @param cause
	 */
	default void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}
	
}
