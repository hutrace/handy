package org.hutrace.handy.config;

import java.io.InputStream;

import org.hutrace.handy.exception.ConfigurationNotFountException;
import org.hutrace.handy.exception.ConfigurationReadException;
import org.hutrace.handy.language.SystemProperty;

/**
 * 未实现的JSON配置器
 * <p>后面如果有需要可以实现
 * @author hu trace
 */
@SuppressWarnings("unused")
public class JSONSetter extends AbstractSetter {
	
	private String resource;
	private InputStream in;
	
	public JSONSetter(String resource, InputStream in) {
		this.resource = resource;
		this.in = in;
	}
	
	@Override
	public void parse() throws ConfigurationReadException, ConfigurationNotFountException {
		throw new ConfigurationReadException(SystemProperty.get("serve.config.type.notfount", "json", "xml"));
	}
	
}
