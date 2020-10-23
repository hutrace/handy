package org.hutrace.handy.server;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.ConverterException;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.exception.ValidateRuntimeException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.HttpServerRequest;
import org.hutrace.handy.http.HttpServerResponse;
import org.hutrace.handy.http.ResponseCode;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.http.explorer.Explorer;
import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.mapping.MappingTable;
import org.hutrace.handy.mapping.TableControlMethod;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * HTTP处理器
 * @author hu trace
 *
 */
public class HttpHandler implements MsgHandler {
	
	private Logger log = LoggerFactory.getLogger(HttpHandler.class);

	private HttpServerRequest request;
	private HttpServerResponse response;
	private HttpMsgsConverter converter;
	
	private TableControlMethod table;
	
	/**
	 * 解析器解得的数据
	 */
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
	
	/**
	 * 默认响应的类型，值：text/plain
	 */
	public static final String DEFAULT_RSP_CONTENT_TYPE = "text/plain";
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		init(ctx, msg);
		if(response != null) {
			openExplorer();
			execute();
			closeExplorer();
			write();
		}
	}
	
	/**
	 * 初始化请求/响应对象
	 * @param ctx
	 * @param msg
	 */
	private void init(ChannelHandlerContext ctx, Object msg) {
		request = new HttpServerRequest((FullHttpRequest) msg, ctx.channel());
		log.debug("serve.request.url", request.url(), request.method().toString());
		try {
			table = MappingTable.instance.control(request);
		}catch (Exception e) {
			DefaultHttpResponse rsp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
			ctx.writeAndFlush(rsp).addListener(ChannelFutureListener.CLOSE);
			ctx.close();
			return;
		}
		request.setMapping(table.method());
		request.setMappingParameterNames(table.parameters());
		request.setControl(table.control());
		response = new HttpServerResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, ctx);
		response.setRequest(request);
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
//	private class MappingInitExecuter implements FlowExecuter {
//		
//		@Override
//		public ExceptionBean execute() throws Throwable {
//			log.debug("serve.request.url", request.url(), request.method().toString());
//			try {
//				table = MappingTable.instance.control(request);
//				request.setMapping(table.method());
//				request.setMappingParameterNames(table.parameters());
//				request.setControl(table.control());
//			}catch (NoRequestUrlException e) {
//				return new ExceptionBean(e, ResponseCode.NOTFOUND_URL,
//						ApplicationProperty.get("serve.req.notfound.url"));
//			}catch (NoRequestMethodException e) {
//				return new ExceptionBean(e, ResponseCode.NOTFOUND_METHOD, ApplicationProperty.get(
//						"serve.req.notfound.method", request.method().toString()));
//			}
//			return null;
//		}
//		
//	}
	
	/**
	 * HTTP请求拦截执行器
	 * <p>依次调用拦截器
	 * @author hu trace
	 *
	 */
	private class InterceptorRequestExecuter implements FlowExecuter {

		@Override
		public ExceptionBean execute() throws Throwable {
			if(Configuration.interceptors() != null && Configuration.interceptors().size() > 0) {
				for(HttpMsgsInterceptor interceptor : Configuration.interceptors()) {
					if(!interceptor.request(request, response)) {
						if(response.canWrite()) {
							response.write("The interceptor intercepted it");
						}
						response.close();
						return ExceptionBean.EMPTY;
					}
				}
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
			String ct = request.headers().get("Content-Type");
			HttpMsgsResolver resolver = null;
			for(HttpMsgsResolver relv : Configuration.resolvers()) {
				if(relv.supports(ct)) {
					if(relv.getMaxContentLength() < request.content().readableBytes()) {
						String msg = ApplicationProperty.get("serve.req.length.exceeded");
						return new ExceptionBean(new HandyserveException(msg),
								ResponseCode.NOTFOUND_RESOLVER, msg);
					}
					resolver = relv;
					parameter = relv.parse(request);
					converter = relv.converter();
					break;
				}
			}
			if(resolver == null) {
				String msg = ApplicationProperty.get("serve.req.notfound.resolver", ct);
				return new ExceptionBean(new HandyserveException(msg),
						ResponseCode.NOTFOUND_RESOLVER, msg);
			}
			if(converter == null) {
				String msg = ApplicationProperty.get("serve.req.notfound.converter", ct);
				return new ExceptionBean(new HandyserveException(msg),
						ResponseCode.NOTFOUND_CONVERTER, msg);
			}
			response.setParameter(parameter);
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
				return new ExceptionBean(e, ResponseCode.ERROR_PARAMETER, e.getMessage());
			}catch (ConverterException e) {
				return new ExceptionBean(e, ResponseCode.ERROR_SYSTEM_CONVERTER, e.getMessage());
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
	 * 响应数据解析器
	 * <p>可通过自定义<code>Content-Type</code>来处理响应数据
	 * @author hu trace
	 *
	 */
	private class ResolverResponseExecuter implements FlowExecuter {

		@Override
		public ExceptionBean execute() {
			String ct = response.header(HttpHeaderNames.CONTENT_TYPE);
			if(ct == null || ct.isEmpty()) {
				ct = request.mapping().getAnnotation(RequestMapping.class).produces();
			}
			if(!ct.isEmpty()) {
				HttpMsgsResolver resolver = null;
				for(HttpMsgsResolver relv : Configuration.resolvers()) {
					if(relv.supports(ct)) {
						resolver = relv;
						break;
					}
				}
				if(resolver == null) {
					String msg = ApplicationProperty.get("serve.req.notfound.resolver", ct);
					return new ExceptionBean(new HandyserveException(msg),
							ResponseCode.NOTFOUND_RESOLVER, msg);
				}
				try {
					result = resolver.write(request, response, result);
				}catch (ResolverException e) {
					return new ExceptionBean(e, ResponseCode.ERROR_RESOLVER, e.getMessage());
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 开始执行所有执行器
	 * <pre>
	 *  执行顺序
	 *    {@link MappingInitExecuter}
	 *    {@link InterceptorRequestExecuter}
	 *    {@link ResolverExecuter}
	 *    {@link ConverterExecuter}
	 *    {@link MappingInvokingExecuter}
	 *    {@link ResolverResponseExecuter}
	 */
	public void execute() {
		List<FlowExecuter> executers = new ArrayList<>();
//		executers.add(new MappingInitExecuter());
		executers.add(new ResolverExecuter());
		executers.add(new InterceptorRequestExecuter());
		executers.add(new ConverterExecuter());
		executers.add(new MappingInvokingExecuter());
		executers.add(new ResolverResponseExecuter());
		execute(executers);
	}
	
	/**
	 * 开始执行执行器
	 * @param executers
	 */
	public void execute(List<FlowExecuter> executers) {
		ExceptionBean eb;
		for(FlowExecuter executer : executers) {
			try {
				eb = executer.execute();
			}catch (Throwable e) {
				eb = new ExceptionBean(e, ResponseCode.ERROR_SYSTEM_UNKNOWN,
						ApplicationProperty.get("serve.system.error"));
			}
			if(eb != null) {
				if(response.canWrite()) {
					exception(eb);
				}
				break;
			}
		}
	}
	
	/**
	 * 异常处理
	 * <p>此处异常处理可以对所有异常进行分析处理
	 */
	public void exception(ExceptionBean eb) {
		exceptionBean = eb;
		// 此处处理需要处理标准HTTP状态码，目前仅处理404
		if(eb.getCode() == ResponseCode.NOTFOUND_URL) {
			response.errorResponse(HttpResponseStatus.NOT_FOUND);
		}
		if(Configuration.exceptionHandler() != null) {
			Object res = Configuration.exceptionHandler().dispose(request, response, eb);
			if(response.canWrite()) {
				result = res;
			}
		}else {
			result = eb.getMsg();
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
	
	/**
	 * 向客户端响应数据
	 */
	private void write() {
		if(response.canWrite()) {
			ResultBean rb;
			if(result instanceof ResultBean) {
				rb = (ResultBean) result;
			}else {
				if(Configuration.resultHandler() == null) {
					rb = null;
				}else {
					rb = Configuration.resultHandler().dispose(request, response, result);
				}
			}
			if(response.canWrite()) {
				String ct = response.header(HttpHeaderNames.CONTENT_TYPE);
				if(ct == null || ct.isEmpty()) {
					response.setHeader(HttpHeaderNames.CONTENT_TYPE, rb == null ? DEFAULT_RSP_CONTENT_TYPE : rb.contentType());
				}
				if(rb != null) {
					response.write(rb);
				}else {
					response.write(result.toString());
				}
				response.flush();
				response.close();
			}
		}
	}
	
}
