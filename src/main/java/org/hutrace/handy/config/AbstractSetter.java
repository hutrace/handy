package org.hutrace.handy.config;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

public abstract class AbstractSetter implements ConfigSetter {

	@Override
	public void setName(String name) throws ConfigurationReadException {
		if(name != null) {
			Configuration.name = name;
		}
	}

	@Override
	public void setAppVersion(String appVersion) throws ConfigurationReadException {
		if(appVersion != null) {
			Configuration.appVersion = appVersion;
		}
	}

	@Override
	public void setPort(int port) throws ConfigurationReadException {
		if(port != 0) {
			Configuration.port = port;
		}
	}

	@Override
	public void setScan(String[] scan) throws ConfigurationReadException {
		if(scan != null && scan.length > 0) {
			Configuration.scan = scan;
		}
	}

	@Override
	public void setResolvers(List<HttpMsgsResolver> resolvers) throws ConfigurationReadException {
		if(resolvers != null && resolvers.size() > 0) {
			Configuration.resolvers = resolvers;
		}
	}

	@Override
	public void setInterceptors(List<HttpMsgsInterceptor> interceptors) throws ConfigurationReadException {
		if(interceptors != null && interceptors.size() > 0) {
			Configuration.interceptors = interceptors;
		}
	}

	@Override
	public void setValidate(HttpMsgsValidate validate) throws ConfigurationReadException {
		if(validate != null) {
			Configuration.validate = validate;
		}
	}

	@Override
	public void setPattern(Pattern pattern) throws ConfigurationReadException {
		if(pattern != null) {
			Configuration.pattern = pattern;
		}
	}

	@Override
	public void setLoaders(List<Loader> loaders) throws ConfigurationReadException {
		if(loaders != null && loaders.size() > 0) {
			Configuration.loaders = loaders;
		}
	}
	
	@Override
	public void setExceptionHandler(ExceptionHandler exceptionHandler) throws ConfigurationReadException {
		if(exceptionHandler != null) {
			Configuration.exceptionHandler = exceptionHandler;
		}
	}
	
	@Override
	public void setListeners(List<ChannelListener> listeners) throws ConfigurationReadException {
		if(listeners != null) {
			Configuration.listeners = listeners;
		}
	}
	
	@Override
	public void setExplorers(List<Explorer> explorers) throws ConfigurationReadException {
		if(explorers != null) {
			Configuration.explorers = explorers;
		}
	}
	
	@Override
	public void setResultHandler(ResultHandler resultHandler) throws ConfigurationReadException {
		if(resultHandler != null) {
			Configuration.resultHandler = resultHandler;
		}
	}

	@Override
	public void setWsResolvers(List<WebSocketMsgsResolver> wsResolvers) throws ConfigurationReadException {
		if(wsResolvers != null && wsResolvers.size() > 0) {
			Configuration.wsResolvers = wsResolvers;
		}
	}

	@Override
	public void setWsValidate(HttpMsgsValidate wsValidate) throws ConfigurationReadException {
		if(wsValidate != null) {
			Configuration.wsValidate = wsValidate;
		}
	}

	@Override
	public void setWsConnect(WebSocketConnect wsConnect) throws ConfigurationReadException {
		if(wsConnect != null) {
			Configuration.wsConnect = wsConnect;
		}
	}

	@Override
	public void setMaxContentLength(int maxContentLength) throws ConfigurationReadException {
		if(maxContentLength != 0) {
			Configuration.maxContentLength = maxContentLength;
		}
	}

	@Override
	public void setCharset(String charset) throws ConfigurationReadException {
		try {
			new String(new byte[] {1}, charset);
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new ConfigurationReadException(e);
		}
		if(charset != null) {
			Configuration.charset = charset;
		}
	}

	@Override
	public void setThreadPools(int threadPools) throws ConfigurationReadException {
		if(threadPools != 0) {
			Configuration.threadPools = threadPools;
		}
	}

	@Override
	public void setThreadName(String threadName) throws ConfigurationReadException {
		if(threadName != null && !threadName.isEmpty()) {
			Configuration.threadName = threadName;
		}
	}

	@Override
	public void setCommonDao(String commonDao) throws ConfigurationReadException {
		if(commonDao != null && !commonDao.isEmpty()) {
			Configuration.commonDao = commonDao;
		}
	}
	
	/**
	 * 校验resource，获取正确的resource路径
	 * @param name
	 * @return
	 */
	protected String resource(String resource) {
		return resource.charAt(0) == '/' ? resource : "/" + resource;
	}

}
