package org.hutrace.handy.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.exception.ConfigurationNotFountException;
import org.hutrace.handy.exception.ConfigurationReadException;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.loader.Loader;

/**
 * <p>配置文件加载器
 * <p>此加载器用于加载默认配置以及自定义配置
 * @author hutrace
 * @see Loader
 * @since 1.8
 * @version 1.0
 */
public class ConfigSetterLoader implements Loader {
	
	/**
	 * 默认读取的配置文件
	 */
	private String[] resources = {
			"application.json",
			"application.xml"
	};
	
	/**
	 * 自定义设置的配置文件路径
	 */
	private static String resource;
	
	/**
	 * 自定义设置的配置器
	 */
	private static ConfigSetter setter;
	
	/**
	 * 设置配置文件相对路径
	 * @param resource
	 */
	public static void setResource(String resource) {
		ConfigSetterLoader.resource = resource;
	}
	
	/**
	 * 设置配置信息设置器
	 * @param setter
	 */
	public static void setDefaultSetter(ConfigSetter setter) {
		ConfigSetterLoader.setter = setter;
	}
	
	/**
	 * 开始配置
	 */
	@Override
	public void execute() throws AppLoaderException {
		if(setter == null) {
			if(resource == null) {
				// 自动获取配置文件
				autoLoader();
			}else {
				// 非自动获取
				loader(resource);
			}
		}else {
			setter.parse();
		}
	}
	
	/**
	 * 自动加载默认配置列表
	 * @throws AppLoaderException
	 */
	private void autoLoader() throws AppLoaderException {
		boolean flag = false;
		for(int i = 0; i < resources.length; i++) {
			if(autoLoader(resources[i])) {
				flag = true;
				break;
			}
		}
		if(!flag) {
			throw new ConfigurationNotFountException(SystemProperty.get("serve.config.notfound",
					" application.json/application.xml "));
		}
	}
	
	/**
	 * 自动加载默认配置
	 * @param resource
	 * @return
	 * @throws ConfigurationReadException
	 */
	private boolean autoLoader(String resource) throws ConfigurationReadException {
		try {
			loader(resource);
			return true;
		}catch (ConfigurationNotFountException e) {
			return false;
		}
	}
	
	/**
	 * 开始加载配置
	 * @param resource
	 * @throws ConfigurationNotFountException
	 * @throws ConfigurationReadException
	 */
	private void loader(String resource) throws ConfigurationNotFountException, ConfigurationReadException {
		InputStream in = null;
		try {
			in = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + resource));
		}catch (FileNotFoundException e) {}
		resource = resource(resource);
		if(in == null) {
			in = this.getClass().getResourceAsStream(resource);
		}
		if(in == null) {
			throw new ConfigurationNotFountException(SystemProperty.get("serve.config.custom.notfound", resource));
		}
		Suffix suffix = getSuffix(resource);
		ConfigSetter setter = null;
		switch (suffix) {
			case XML:
				setter = new XMLSetter(resource, in);
				break;
			case JSON:
				setter = new JSONSetter(resource, in);
				break;
			default:
				break;
		}
		if(setter == null) {
			throw new ConfigurationNotFountException(SystemProperty.get("serve.config.type.notfount", resource, " xml/json "));
		}
		setter.parse();
	}
	
	/**
	 * 获取配置文件后缀并转换为{@link Suffix}
	 * @param resource
	 * @return
	 * @throws ConfigurationNotFountException
	 */
	private Suffix getSuffix(String resource) throws ConfigurationNotFountException {
		try {
			return Suffix.valueOf(resource.substring(resource.lastIndexOf(".") + 1).toUpperCase());
		}catch (Exception e) {
			throw new ConfigurationNotFountException(SystemProperty.get("serve.config.type.notfount", resource, " xml/json "));
		}
	}
	
	/**
	 * 校验resource，获取正确的resource路径
	 * @param name
	 * @return
	 */
	private String resource(String resource) {
		return resource.charAt(0) == '/' ? resource : "/" + resource;
	}
	
	/**
	 * 支持的配置文件格式后缀枚举
	 * @author hu trace
	 *
	 */
	private enum Suffix {
		
		/**
		 * XML格式
		 */
		XML,
		
		/**
		 * JSON格式
		 */
		JSON
	}
	
}
