package org.hutrace.handy.websocket.result;

import java.lang.reflect.InvocationTargetException;

import org.hutrace.handy.exception.CustomException;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.exception.ValidateRuntimeException;
import org.hutrace.handy.http.ResponseCode;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.WebSocketResponse;
import org.hutrace.handy.websocket.WebSocketResult;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class WebSocketDefaultResultHandler implements WebSocketResultHandler {

	@Override
	public void dispose(WebSocketRequest request, WebSocketResponse response, Object msg, ExceptionBean eb) {
		if(msg != null) {
			WebSocketResult result = new WebSocketResult();
			result.setEvent(request.url()).setData(msg).setMsg("ok");
			response.writeAndFlush(result);
		}else if(eb != null) {
			WebSocketResult result = new WebSocketResult();
			result.setEvent("error").setData(request.url());
			Throwable e = eb.getEx();
			if(e instanceof InvocationTargetException) {
				e = ((InvocationTargetException) e).getTargetException();
			}
			if(e instanceof ValidateRuntimeException) {
				result.setMsg(e.getMessage());
				result.setCode(ResponseCode.ERROR_PARAMETER);
			}else if(e instanceof HandyserveException) {
				result.setMsg(e.getMessage());
				result.setCode(eb.getCode());
			}else if(e instanceof CustomException) {
				result.setMsg(e.getMessage());
				result.setCode(((CustomException) e).getCode());
			}else {
				result.setMsg(eb.getMsg());
				result.setCode(eb.getCode());
			}
			response.writeAndFlush(result);
		}
	}

	@Override
	public void response(WebSocketResponse response, WebSocketResult result) {
		response.writeAndFlush(result);
	}

	@Override
	public void onInitError(ChannelHandlerContext ctx, String msg) {
		WebSocketResult result = new WebSocketResult();
		result.setCode(-1).setEvent("error").setMsg(msg);
		ctx.channel().writeAndFlush(new TextWebSocketFrame(
				Unpooled.wrappedBuffer(result.bytes()))).addListener(ChannelFutureListener.CLOSE);
	}

}
