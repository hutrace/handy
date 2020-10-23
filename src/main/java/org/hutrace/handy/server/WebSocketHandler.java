package org.hutrace.handy.server;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.exception.NoWebSocketMappingException;
import org.hutrace.handy.exception.ValidateRuntimeException;
import org.hutrace.handy.exception.WebSocketConnectException;
import org.hutrace.handy.exception.WebSocketHeaderParseException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.ResponseCode;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.http.explorer.Explorer;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.mapping.MappingTable;
import org.hutrace.handy.mapping.TableWebSocketMethod;
import org.hutrace.handy.websocket.WebSocketRequest;
import org.hutrace.handy.websocket.WebSocketResponse;
import org.hutrace.handy.websocket.WebSocketResultCodes;
import org.hutrace.handy.websocket.converter.WebSocketMsgsConverter;
import org.hutrace.handy.websocket.resolver.WebSocketMsgsResolver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class WebSocketHandler implements MsgHandler {
	
	private static Logger log = LoggerFactory.getLogger(WebSocketHandler.class);
	
	private WebSocketResponse response;
	private WebSocketRequest request;
	private WebSocketMsgsConverter converter;
	private TableWebSocketMethod table;
	
	private Object parameter;
	
	/**
	 * 解析器解得的数据
	 */
	private Object[] mappingInvokingParameters;
	
	/**
	 * 向客户端响应的数据
	 */
	private Object result;
	
	/**
	 * 异常
	 */
	private ExceptionBean exceptionBean;
	
	private static final byte P = 80;
	private static final byte I = 73;
	private static final byte N = 78;
	private static final byte G = 71;
	private static final byte[] PONG = {80, 79, 78, 71};
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object arg0) throws Exception {
		try {
			if(init(ctx, arg0)) {
				return;
			}
		}catch (Exception e) {
			Configuration.wsResultHandler().onInitError(ctx, e.getMessage());
			return;
		}
		openExplorer();
		execute();
		closeExplorer();
		write();
	}

	/**
	 * 打开资源
	 */
	private void openExplorer() {
		if(Configuration.explorers() != null && Configuration.explorers().size() > 0) {
			for(Explorer explorer : Configuration.explorers()) {
				try {
					explorer.open(request, response);
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean init(ChannelHandlerContext ctx, Object arg0) throws WebSocketHeaderParseException, WebSocketConnectException {
		ByteBuf buf = ((WebSocketFrame) arg0).content();
		if(buf.readableBytes() == 4 && buf.getByte(0) == P
				&& buf.getByte(1) == I && buf.getByte(2) == N && buf.getByte(3) == G) {
			ByteBuf binaryData = Unpooled.buffer(4);
			binaryData.writeBytes(PONG);
			ctx.channel().writeAndFlush(new TextWebSocketFrame(binaryData));
			return true;
		}
		String id = ctx.channel().id().toString();
		request = new WebSocketRequest(buf, ctx.channel().remoteAddress(), id);
		boolean isConnect = request.isConnect();
		/*
		 * 初始化response,此处暂不考虑一个用户多端在线的情况,如果需要考虑,可以添加client-type的header来区分不同的客户端
		 */
		if(request.isConnect()) {
			response = new WebSocketAstrictResponse(ctx);
			Configuration.wsConnect().onConnect(request, response);
			WebSocketClients.addClient(id, response);
		}else {
			response = WebSocketClients.getClient(id);
			if(response == null) {
				response = new WebSocketAstrictResponse(ctx);
				WebSocketClients.addClient(id, response);
			}
		}
		return isConnect;
	}
	

	/**
	 * 所有包含异常的流程执行器
	 * @author hu trace
	 */
	private interface FlowExecuter {
		
		ExceptionBean execute() throws Throwable;
		
	}
	
	/**
	 * 初始化映射执行器
	 * <p>初始化映射相关内容
	 * <p>包含获取{@link MappingTable}，设置mapping以及mapping方法对应的参数名称
	 * @author hu trace
	 *
	 */
	private class MappingInitExecuter implements FlowExecuter {
		
		@Override
		public ExceptionBean execute() throws Throwable {
			log.debug("serve.ws.request.url", request.url());
			try {
				table = MappingTable.instance.websocket(request.url());
				request.setMapping(table.method());
				request.setMappingParameterNames(table.parameters());
				request.setControl(table.control());
			}catch (NoWebSocketMappingException e) {
				return new ExceptionBean(e, WebSocketResultCodes.NOTFOUND_URL,
						ApplicationProperty.get("serve.ws.req.notfound.url"));
			}
			return null;
		}
		
	}
	
	/**
	 * HTTP消息解析执行器
	 * <p>根据请求的<code>Content-Type</code>获取解析器解析数据
	 * <p>验证请求消息的内容长度是否超出了限制最大长度
	 * @author hu trace
	 *
	 */
	private class ResolverExecuter implements FlowExecuter {

		@Override
		public ExceptionBean execute() throws Throwable {
			String contentType = request.dataType();
			WebSocketMsgsResolver resolver = null;
			for(WebSocketMsgsResolver wsResolver : Configuration.wsResolvers()) {
				if(wsResolver.supports(contentType)) {
					resolver = wsResolver;
					parameter = wsResolver.parse(request);
					converter = wsResolver.converter();
					break;
				}
			}
			if(resolver == null) {
				String msg = ApplicationProperty.get("serve.ws.req.notfound.resolver", contentType);
				return new ExceptionBean(new HandyserveException(msg),
						WebSocketResultCodes.NOTFOUND_RESOLVER, msg);
			}
			if(converter == null) {
				String msg = ApplicationProperty.get("serve.ws.req.notfound.converter", contentType);
				return new ExceptionBean(new HandyserveException(msg),
						WebSocketResultCodes.NOTFOUND_CONVERTER, msg);
			}
			return null;
		}
		
	}
	
	/**
	 * HTTP参数转换执行器
	 * <p>调用参数解析器绑定的参数转换器
	 * <p>最终得到mapping方法上需要的参数数据
	 * @author hu trace
	 *
	 */
	private class ConverterExecuter implements FlowExecuter {

		@Override
		public ExceptionBean execute() {
			Method method = table.method();
			Parameter[] parameters = method.getParameters();
			mappingInvokingParameters = new Object[parameters.length];
			try {
				for(int i = 0; i < parameters.length; i++) {
					Class<?> clazs = parameters[i].getType();
					if(HttpRequest.class.isAssignableFrom(clazs)) {
						mappingInvokingParameters[i] = request;
					}else if(HttpResponse.class.isAssignableFrom(clazs)) {
						mappingInvokingParameters[i] = response;
					}else {
						mappingInvokingParameters[i] = converter.read(request,
								table.parameters()[i], parameters[i], parameter, table.verify());
					}
				}
			}catch(ValidateRuntimeException e) {
				return new ExceptionBean(e, WebSocketResultCodes.ERROR_PARAMETER, e.getMessage());
			}catch (ConverterException e) {
				return new ExceptionBean(e, WebSocketResultCodes.ERROR_SYSTEM_CONVERTER, e.getMessage());
			}
			return null;
		}
		
	}
	
	/**
	 * 调用mapping方法执行器
	 * <p>直接使用转换器得到的参数调用mapping对应的方法
	 * @author hu trace
	 *
	 */
	private class MappingInvokingExecuter implements FlowExecuter {

		@Override
		public ExceptionBean execute() throws Throwable {
			result = table.invoke(mappingInvokingParameters);
			return null;
		}
		
	}
	
	/**
	 * 开始执行所有执行器
	 * <pre>
	 *  执行顺序
	 *    {@link MappingInitExecuter}
	 *    {@link ResolverExecuter}
	 *    {@link ConverterExecuter}
	 *    {@link MappingInvokingExecuter}
	 */
	public void execute() {
		List<FlowExecuter> executers = new ArrayList<>();
		executers.add(new MappingInitExecuter());
		executers.add(new ResolverExecuter());
		executers.add(new ConverterExecuter());
		executers.add(new MappingInvokingExecuter());
		execute(executers);
	}
	
	/**
	 * 开始执行执行器
	 * @param executers
	 */
	public void execute(List<FlowExecuter> executers) {
		for(FlowExecuter executer : executers) {
			try {
				exceptionBean = executer.execute();
			}catch (Throwable e) {
				exceptionBean = new ExceptionBean(e, ResponseCode.ERROR_SYSTEM_UNKNOWN,
						ApplicationProperty.get("serve.system.error"));
			}
			if(exceptionBean != null) {
				break;
			}
		}
	}
	
	private void write() {
		if(result != null) {
			Configuration.wsResultHandler().dispose(request, response, result, exceptionBean);
		}
	}
	
	/**
	 * 关闭单会话资源
	 */
	private void closeExplorer() {
		if(Configuration.explorers() != null && Configuration.explorers().size() > 0) {
			for(Explorer explorer : Configuration.explorers()) {
				try {
					explorer.close(request, response, exceptionBean);
				}catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}
	

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(request != null) {
			log.debug("serve.ws.close", request.id());
			WebSocketClients.removeClient(request.id());
			Configuration.wsConnect().onClose(request);
		}else {
			log.debug("serve.ws.close", ctx.channel().id().toString());
			WebSocketClients.removeClient(ctx.channel().id().toString());
		}
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		if(request != null) {
			log.debug("serve.ws.close", request.id());
			WebSocketClients.removeClient(request.id());
			Configuration.wsConnect().onClose(request);
		}else {
			log.debug("serve.ws.close", ctx.channel().id().toString());
			WebSocketClients.removeClient(ctx.channel().id().toString());
		}
		ctx.close();
	}

}
