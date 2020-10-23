package org.hutrace.handy.config;

import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.http.validate.HttpMsgsValidate;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.websocket.connect.WebSocketConnect;
import org.hutrace.handy.websocket.resolver.WebSocketMsgsResolver;

/**
 * <p>自定义配置, 通过此类可以配置框架的参数
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class ConfigurationCustom {
	
	protected String name;
	protected String appVersion;
	protected int port;
	protected String[] scan;
	protected String[] mapper;
	protected List<HttpMsgsResolver> resolver;
	protected List<HttpMsgsInterceptor> interceptor;
	protected HttpMsgsValidate validate;
	protected Pattern pattern;
	protected List<Loader> loader;

	protected List<WebSocketMsgsResolver> wsResolver;
	protected HttpMsgsValidate wsValidate;
	protected WebSocketConnect wsConnect;
	protected int maxContentLength;
	protected String charset;
	protected int threadPools;
	
	/**
	 * <p>设置当前项目访问时需要添加的项目名
	 * <p>默认为""
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * <p>设置应用程序的版本
	 * @param appVersion
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	/**
	 * <p>设置程序使用的端口号
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * <p>设置程序需要扫描的包路径
	 * @param scan
	 */
	public void setScan(String[] scan) {
		this.scan = scan;
	}
	
	/**
	 * <p>设置MyBatis中mapper根目录的路径
	 * @param mapper
	 */
	public void setMapper(String[] mapper) {
		this.mapper = mapper;
	}
	
	/**
	 * <p>设置需要扩展的参数解析器
	 * @param resolvers
	 */
	public void setResolver(List<HttpMsgsResolver> resolvers) {
		this.resolver = resolvers;
	}
	
	/**
	 * <p>设置需要扩展的参数解析器
	 * @param resolver
	 */
	public void setResolver(String[] resolver) {
		this.resolver = new ArrayList<>();
		try {
			for(String r : resolver) {
				this.resolver.add((HttpMsgsResolver) Class.forName(r).newInstance());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>设置需要扩展的拦截器
	 * @param interceptors
	 */
	public void setInterceptor(List<HttpMsgsInterceptor> interceptors) {
		this.interceptor = interceptors;
	}
	
	/**
	 * <p>设置需要扩展的拦截器
	 * @param interceptor
	 */
	public void setInterceptor(String[] interceptor) {
		this.interceptor = new ArrayList<>();
		try {
			for(String r : interceptor) {
				this.interceptor.add((HttpMsgsInterceptor) Class.forName(r).newInstance());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>设置参数验证器
	 * @param validate
	 */
	public void setValidate(HttpMsgsValidate validate) {
		this.validate = validate;
	}
	
	/**
	 * <p>设置整个项目默认实列配置
	 * @param pattern
	 */
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	/**
	 * <p>扩展程序加载器
	 * @param loaders
	 */
	public void setLoader(List<Loader> loaders) {
		this.loader = loaders;
	}
	
	/**
	 * <p>扩展程序加载器
	 * @param loader
	 */
	public void setLoader(String[] loader) {
		this.loader = new ArrayList<>();
		try {
			for(String r : loader) {
				this.loader.add((Loader) Class.forName(r).newInstance());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>扩展WebSocket参数解析器
	 * @param wsResolver
	 */
	public void setWsResolver(List<WebSocketMsgsResolver> wsResolver) {
		this.wsResolver = wsResolver;
	}
	
	/**
	 * <p>扩展WebSocket参数解析器
	 * @param resolver
	 */
	public void setWsResolver(String[] wsResolver) {
		this.wsResolver = new ArrayList<>();
		try {
			for(String r : wsResolver) {
				this.wsResolver.add((WebSocketMsgsResolver) Class.forName(r).newInstance());
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * <p>设置WebSocket参数验证器
	 * @param wsValidate
	 */
	public void setWsValidate(HttpMsgsValidate wsValidate) {
		this.wsValidate = wsValidate;
	}
	
	/**
	 * <p>设置WebSocket连接实现类
	 * @param wsConnect
	 */
	public void setWsConnect(WebSocketConnect wsConnect) {
		this.wsConnect = wsConnect;
	}
	
	/**
	 * <p>设置http内容的最大长度, 默认为1024*1024(1m)
	 * @param maxContentLength
	 */
	public void setMaxContentLength(int maxContentLength) {
		this.maxContentLength = maxContentLength;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	/**
	 * 设置程序业务线程总数
	 * @param threadPools
	 */
	public void setThreadPools(int threadPools) {
		this.threadPools = threadPools;
	}
	
}
