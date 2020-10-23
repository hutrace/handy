package org.hutrace.handy.websocket.result;

import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.WebSocketResponse;
import org.hutrace.handy.websocket.WebSocketResult;

import io.netty.channel.ChannelHandlerContext;

/**
 * WebSocket 响应类标准接口
 * @author hu trace
 *
 */
public interface WebSocketResultHandler {
	
	/**
	 * 处理你想要响应的数据进行响应
	 * <p>你可以直接通过response.write进行响应
	 * <p>也可以通过标准接口{@link ResultBean}进行响应
	 * <p>建议使用标准接口{@link ResultBean}进行响应
	 * @param request
	 * @param response
	 * @param msg
	 */
	void dispose(WebSocketRequest request, WebSocketResponse response, Object msg, ExceptionBean eb);
	
	/**
	 * 向客户端推送数据
	 * @param response
	 * @param code
	 * @param msg
	 * @param data
	 */
	void response(WebSocketResponse response, WebSocketResult result);
	
	/**
	 * 初始化失败时的处理
	 * @param ctx
	 * @param msg
	 */
	void onInitError(ChannelHandlerContext ctx, String msg);
	
}
