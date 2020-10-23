package org.hutrace.handy.exception;

/**
 * <p>没有找到配置文件的异常
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ConfigurationNotFountException extends AppLoaderException {
	
	public ConfigurationNotFountException(Throwable e) {
		super(e);
	}
	
	public ConfigurationNotFountException(String msg) {
		super(msg);
	}
	
	public ConfigurationNotFountException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
