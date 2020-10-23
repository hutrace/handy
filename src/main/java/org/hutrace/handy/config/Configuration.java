package org.hutrace.handy.config;

import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.http.exception.ExceptionHandler;
import org.hutrace.handy.http.explorer.Explorer;
import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.listener.ChannelListener;
import org.hutrace.handy.http.resolver.HttpEmptyResolver;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.http.result.ResultHandler;
import org.hutrace.handy.http.validate.DefaultHttpMsgsValidate;
import org.hutrace.handy.http.validate.HttpMsgsValidate;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.utils.Charset;
import org.hutrace.handy.websocket.connect.DefaultWebSocketConnect;
import org.hutrace.handy.websocket.connect.WebSocketConnect;
import org.hutrace.handy.websocket.resolver.WebSocketJsonMsgsResolver;
import org.hutrace.handy.websocket.resolver.WebSocketMsgsResolver;
import org.hutrace.handy.websocket.result.WebSocketDefaultResultHandler;
import org.hutrace.handy.websocket.result.WebSocketResultHandler;

/**
 * <p>系统配置
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class Configuration {
	
	static String name;
	static String appVersion;
	static int port;
	static String[] scan;
	static List<HttpMsgsResolver> resolvers;
	static List<HttpMsgsInterceptor> interceptors;
	static HttpMsgsValidate validate;
	static Pattern pattern;
	static List<Loader> loaders;
	static ExceptionHandler exceptionHandler;
	static List<ChannelListener> listeners;
	static List<Explorer> explorers;
	static ResultHandler resultHandler;
	
	static List<WebSocketMsgsResolver> wsResolvers;
	static HttpMsgsValidate wsValidate;
	static WebSocketConnect wsConnect;
	static WebSocketResultHandler wsResultHandler;
	
	static int maxContentLength;
	static String charset;
	static int threadPools;
	static String threadName;
	static String commonDao;
	
	static {
		name = "";
		appVersion = "v0.0.1";
		port = 10101;
		pattern = Pattern.SINGLETON;
		resolvers = new ArrayList<>();
		resolvers.add(new HttpEmptyResolver());
		
		interceptors = new ArrayList<>();
		validate = new DefaultHttpMsgsValidate();
		explorers = new ArrayList<>();
		
		wsResolvers = new ArrayList<>();
		wsResolvers.add(new WebSocketJsonMsgsResolver());
		wsValidate = new DefaultHttpMsgsValidate();
		wsConnect = new DefaultWebSocketConnect();
		wsResultHandler = new WebSocketDefaultResultHandler();
		maxContentLength = 1024 * 1024;// 1m
		
		charset = Charset.UTF_8;
		threadPools = 100;
		threadName = "fast-server-threads";
	}
	
	/**
	 * 获取程序名称，HTTP请求前缀
	 * @return
	 */
	public static String name() {
		return name;
	}
	
	/**
	 * 获取程序版本号
	 * @return
	 */
	public static String appVersion() {
		return appVersion;
	}
	
	/**
	 * 获取启动占用端口
	 * @return
	 */
	public static int port() {
		return port;
	}
	
	/**
	 * 获取需要扫描的程序包路径
	 * @return
	 */
	public static String[] scan() {
		return scan;
	}
	
	/**
	 * 获取自定义Http的消息解析器
	 * @return
	 */
	public static List<HttpMsgsResolver> resolvers() {
		return resolvers;
	}
	
	/**
	 * 获取自定义Http的拦截器
	 * @return
	 */
	public static List<HttpMsgsInterceptor> interceptors() {
		return interceptors;
	}
	
	/**
	 * 获取Http参数验证器
	 * @return
	 */
	public static HttpMsgsValidate validate() {
		return validate;
	}
	
	/**
	 * 获取程序采用的模式（全局）
	 * @return
	 */
	public static Pattern pattern() {
		return pattern;
	}
	
	/**
	 * 获取自定义Handyserve的加载器
	 * @return
	 */
	public static List<Loader> loaders() {
		return loaders;
	}
	
	/**
	 * 获取自定义WebSocket的消息解析器
	 * @return
	 */
	public static List<WebSocketMsgsResolver> wsResolvers() {
		return wsResolvers;
	}
	
	/**
	 * 获取WebSocket参数验证器
	 * @return
	 */
	public static HttpMsgsValidate wsValidate() {
		return wsValidate;
	}
	
	/**
	 * 获取自定义WebSocket连接实现类
	 * @return
	 */
	public static WebSocketConnect wsConnect() {
		return wsConnect;
	}
	
	/**
	 * 获取自定义WebSocketResultHandler实现类
	 * @return
	 */
	public static WebSocketResultHandler wsResultHandler() {
		return wsResultHandler;
	}
	
	/**
	 * 获取请求支持的最大内容长度,单位是byte
	 * @return
	 */
	public static int maxContentLength() {
		return maxContentLength;
	}
	
	/**
	 * 获取设置的系统编码格式
	 * @return
	 */
	public static String charset() {
		return charset;
	}
	
	/**
	 * 程序的业务线程总数
	 * @return
	 */
	public static int threadPools() {
		return threadPools;
	}
	
	/**
	 * 程序的业务线程名字
	 * @return
	 */
	public static String threadName() {
		return threadName;
	}
	
	/**
	 * 获取公共dao的class
	 * @return
	 */
	public static String commonDao() {
		return commonDao;
	}
	
	/**
	 * 获取异常处理器
	 * @return
	 */
	public static ExceptionHandler exceptionHandler() {
		return exceptionHandler;
	}
	
	/**
	 * 获取HTTP监听器
	 * @return
	 */
	public static List<ChannelListener> listeners() {
		return listeners;
	}
	
	/**
	 * 获取HTTP资源管理器
	 * @return
	 */
	public static List<Explorer> explorers() {
		return explorers;
	}
	
	/**
	 * 获取响应数据处理器
	 * @return
	 */
	public static ResultHandler resultHandler() {
		return resultHandler;
	}
	
}
