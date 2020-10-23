package org.hutrace.handy.config;

import java.util.List;

import org.hutrace.handy.exception.ConfigurationNotFountException;
import org.hutrace.handy.exception.ConfigurationReadException;
import org.hutrace.handy.http.exception.ExceptionHandler;
import org.hutrace.handy.http.explorer.Explorer;
import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.listener.ChannelListener;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.http.result.ResultHandler;
import org.hutrace.handy.http.validate.HttpMsgsValidate;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.websocket.connect.WebSocketConnect;
import org.hutrace.handy.websocket.resolver.WebSocketMsgsResolver;

public interface ConfigSetter {
	
	/**
	 * 设置项目名称（http请求前缀）
	 * @param name
	 * @throws ConfigurationReadException
	 */
	void setName(String name) throws ConfigurationReadException;
	
	/**
	 * 设置程序版本号（http请求会以消息头的形式响应）
	 * @param appVersion
	 * @throws ConfigurationReadException
	 */
	void setAppVersion(String appVersion) throws ConfigurationReadException;
	
	/**
	 * 设置程序启动占用的端口号
	 * @param port
	 * @throws ConfigurationReadException
	 */
	void setPort(int port) throws ConfigurationReadException;
	
	/**
	 * 设置FastServer扫描的路径
	 * @param scan
	 * @throws ConfigurationReadException
	 */
	void setScan(String[] scan) throws ConfigurationReadException;
	
	/**
	 * 设置http消息解析器
	 * @param resolver
	 * @throws ConfigurationReadException
	 */
	void setResolvers(List<HttpMsgsResolver> resolvers) throws ConfigurationReadException;
	
	/**
	 * 设置http拦截器
	 * @param interceptor
	 * @throws ConfigurationReadException
	 */
	void setInterceptors(List<HttpMsgsInterceptor> interceptors) throws ConfigurationReadException;
	
	/**
	 * 设置http参数验证器
	 * @param validate
	 * @throws ConfigurationReadException
	 */
	void setValidate(HttpMsgsValidate validate) throws ConfigurationReadException;
	
	/**
	 * 设置程序默认模式（单例/多例），默认为单例
	 * @param pattern
	 * @throws ConfigurationReadException
	 */
	void setPattern(Pattern pattern) throws ConfigurationReadException;
	
	/**
	 * 设置程序启动加载器
	 * @param loader
	 * @throws ConfigurationReadException
	 */
	void setLoaders(List<Loader> loaders) throws ConfigurationReadException;
	
	/**
	 * 设置异常处理器
	 * @param exceptionHandler
	 * @throws ConfigurationReadException
	 */
	void setExceptionHandler(ExceptionHandler exceptionHandler) throws ConfigurationReadException;
	
	/**
	 * 设置监听器
	 * @param listener
	 * @throws ConfigurationReadException
	 */
	void setListeners(List<ChannelListener> listeners) throws ConfigurationReadException;
	
	/**
	 * 设置资源管理器
	 * @param explorers
	 * @throws ConfigurationReadException
	 */
	void setExplorers(List<Explorer> explorers) throws ConfigurationReadException;
	
	/**
	 * 设置响应数据处理器
	 * @param resultHandler
	 * @throws ConfigurationReadException
	 */
	void setResultHandler(ResultHandler resultHandler) throws ConfigurationReadException;
	
	/**
	 * 设置WebSocket消息解析器
	 * @param wsResolver
	 * @throws ConfigurationReadException
	 */
	void setWsResolvers(List<WebSocketMsgsResolver> wsResolvers) throws ConfigurationReadException;
	
	/**
	 * 设置WebSocket参数验证器
	 * @param wsValidate
	 * @throws ConfigurationReadException
	 */
	void setWsValidate(HttpMsgsValidate wsValidate) throws ConfigurationReadException;
	
	/**
	 * 设置WebSocket连接器
	 * @param wsConnect
	 * @throws ConfigurationReadException
	 */
	void setWsConnect(WebSocketConnect wsConnect) throws ConfigurationReadException;
	
	/**
	 * 设置程序支持最大消息长度
	 * @param maxContentLength
	 * @throws ConfigurationReadException
	 */
	void setMaxContentLength(int maxContentLength) throws ConfigurationReadException;
	
	/**
	 * 设置程序默认编码格式
	 * @param charset
	 * @throws ConfigurationReadException
	 */
	void setCharset(String charset) throws ConfigurationReadException;
	
	/**
	 * 设置程序业务线程数量
	 * @param threadPools
	 * @throws ConfigurationReadException
	 */
	void setThreadPools(int threadPools) throws ConfigurationReadException;
	
	/**
	 * 设置程序业务线程数量
	 * @param threadPools
	 * @throws ConfigurationReadException
	 */
	void setThreadName(String threadName) throws ConfigurationReadException;
	
	/**
	 * 设置公共DAO类
	 * @param commonDao
	 * @throws ConfigurationReadException
	 */
	void setCommonDao(String commonDao) throws ConfigurationReadException;
	
	/**
	 * 开始解析配置
	 * @throws ConfigurationReadException
	 */
	void parse() throws ConfigurationReadException, ConfigurationNotFountException;
	
}
