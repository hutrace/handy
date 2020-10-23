package org.hutrace.handy.exception;

/**
 * <p>读取配置文件出错后抛出此异常
 * <p>此异常用于读取配置文件失败时所抛出
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConfigurationReadException extends AppLoaderException {
	
	public ConfigurationReadException(Throwable e) {
		super(e);
	}
	
	public ConfigurationReadException(String msg) {
		super(msg);
	}
	
	public ConfigurationReadException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
