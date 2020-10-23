package org.hutrace.handy.mapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.config.Pattern;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;

/**
 * <p>{@link Controller}控制器表（包装类）
 * <p>用于储存以及初始化{@link Controller}控制器、以及调用的{@link Service}、以及绑定控制器中的接口方法（包含{@link RequestMapping}的方法）
 * @author hutrace
 * @see TableService
 * @see TableControlMethod
 * @see Field
 * @since 1.8
 * @version 1.0
 */
public class TableController {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 实列
	 */
	Object instance;
	
	/**
	 * 类名称
	 */
	String className;
	
	/**
	 * 类
	 */
	Class<?> clazs;
	
	/**
	 * 是否是单例
	 */
	boolean singleton;
	
	/**
	 * 调用的Service数组
	 */
	TableService[] childServices;
	
	/**
	 * Controller中Service的属性
	 */
	Field[] childFields;
	
	/**
	 * 映射地址
	 */
	String mapping;
	
	/**
	 * Controller中所有包含RequestMapping注解的方法
	 */
	TableControlMethod[] methods;
	
	/**
	 * <p>构造方法
	 * <p>传入Class进行新建此对象
	 * @param clazs
	 * @throws Exception
	 */
	TableController(Class<?> clazs) throws Exception {
		this.clazs = clazs;
		this.className = clazs.getName();
		initRequestMapping();
		initSingleton();
		initInstance();
		bindMethod();
	}
	
	/**
	 * <p>初始化Controller类上的RequestMapping
	 */
	private void initRequestMapping() {
		RequestMapping requestMapping = clazs.getAnnotation(RequestMapping.class);
		if(requestMapping == null) {
			requestMapping = DefaultControlMapping.class.getAnnotation(RequestMapping.class);
		}
		StringBuilder m = new StringBuilder();
		m.append("/");
		m.append(Configuration.name());
		m.append("/");
		m.append(requestMapping.value()[0]);
		m.append("/");
		String map = m.toString().replace("//", "/");
		this.mapping  = map.substring(0, map.length() - 1);
		if(this.mapping.equals("/")) {
			this.mapping = "";
		}
	}
	
	/**
	 * <p>初始化当前类是否是单例
	 */
	private void initSingleton() {
		Controller ctro = clazs.getAnnotation(Controller.class);
		Pattern pattern = ctro.pattern();
		if(pattern == Pattern.DEFAULT) {
			pattern = Configuration.pattern();
		}
		singleton = (pattern == Pattern.SINGLETON);
	}
	
	/**
	 * <p>初始化当前类实列
	 * @throws Exception
	 */
	private void initInstance() throws Exception {
		if(singleton) {
			instance = clazs.newInstance();
		}
	}
	
	/**
	 * <p>绑定类中的方法
	 * @throws IOException
	 */
	private void bindMethod() throws IOException {
		Method[] methods = clazs.getDeclaredMethods();
		List<TableControlMethod> tableMethods = new ArrayList<>();
		for(Method method : methods) {
			RequestMapping mapping = method.getAnnotation(RequestMapping.class);
			if(mapping != null) {
				tableMethods.add(new TableControlMethod(method, mapping, this));
			}
		}
		this.methods = new TableControlMethod[tableMethods.size()];
		for(int i = 0; i < this.methods.length; i++) {
			this.methods[i] = tableMethods.get(i);
		}
	}
	
	/**
	 * <p>调用控制器中的方法
	 * <p>需要传入调用的方法以及参数
	 * @param method 调用的方法
	 * @param args 传给方法的参数
	 * @return 方法返回的参数
	 * @throws Exception 调用失败会抛出异常{@link Exception}
	 */
	public Object invoke(Method method, Object... args) throws Exception {
		if(singleton) {
			log.debug("serve.mapping.invoke.singleton", className + "#" + method.getName());
			setSingletonService(instance);
			return method.invoke(instance, args);
		}else {
			log.debug("serve.mapping.invoke.multiton", className + "#" + method.getName());
			Object controlInstance = clazs.newInstance();
			setMultitonService(controlInstance);
			return method.invoke(controlInstance, args);
		}
	}
	
	/**
	 * <p>清除缓存
	 */
	private void clearCache() {
		if(childServices != null) {
			Thread thread = Thread.currentThread();
			for(int i = 0; i < childServices.length; i++) {
				/*
				 *  修改此处代码，如果直接清空map，会出现多线程方面的问题。
				 *  例如：两个线程同时进入，当第一个线程执行完的时候第二个线程还在执行，如果清空的话，第二个线程会创建无用的实列
				 */
//				childServices[i].isInstantiateMap = new HashMap<>();
				childServices[i].isInstantiateMap.remove(thread);
			}
		}
	}
	
	/**
	 * <p>设置单例Service
	 * @param instance
	 * @throws Exception
	 */
	private void setSingletonService(Object instance) throws Exception {
		if(childServices != null) {
			for(int i = 0; i < childServices.length; i++) {
				if(!childServices[i].singleton) {
					childServices[i].setField(instance, childFields[i]);
				}
			}
		}
		clearCache();
	}
	
	/**
	 * <p>设置多例Service
	 * @param instance
	 * @throws Exception
	 */
	private void setMultitonService(Object instance) throws Exception {
		if(childServices != null) {
			for(int i = 0; i < childServices.length; i++) {
				childServices[i].setField(instance, childFields[i]);
			}
		}
		clearCache();
	}
	
}
