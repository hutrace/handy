package org.hutrace.handy.mapping;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.WebSocket;
import org.hutrace.handy.annotation.WebSocketMapping;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.config.Pattern;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;

/**
 * <p>{@link WebSocket}表（包装类）
 * <p>它的作用与{@link Controller}一致，此类中不再做过多的解释
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class TableWebSocket {
	
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
	
	Field[] childFields;
	
	/**
	 * 映射地址
	 */
	String mapping;
	
	TableWebSocketMethod[] methods;
	
	TableWebSocket(Class<?> clazs) throws Exception {
		this.clazs = clazs;
		this.className = clazs.getName();
		initRequestMapping();
		initSingleton();
		initInstance();
		bindMethod();
	}
	
	private void initRequestMapping() {
		WebSocketMapping requestMapping = clazs.getAnnotation(WebSocketMapping.class);
		if(requestMapping == null) {
			requestMapping = DefaultWebSocketMapping.class.getAnnotation(WebSocketMapping.class);
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
	
	private void initSingleton() {
		WebSocket ctro = clazs.getAnnotation(WebSocket.class);
		Pattern pattern = ctro.pattern();
		if(pattern == Pattern.DEFAULT) {
			pattern = Configuration.pattern();
		}
		singleton = (pattern == Pattern.SINGLETON);
	}
	
	private void initInstance() throws Exception {
		if(singleton) {
			instance = clazs.newInstance();
		}
	}
	
	private void bindMethod() throws IOException {
		Method[] methods = clazs.getDeclaredMethods();
		List<TableWebSocketMethod> tableMethods = new ArrayList<>();
		for(Method method : methods) {
			WebSocketMapping mapping = method.getAnnotation(WebSocketMapping.class);
			if(mapping != null) {
				tableMethods.add(new TableWebSocketMethod(method, mapping, this));
			}
		}
		this.methods = new TableWebSocketMethod[tableMethods.size()];
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
	
	private void clearCache() {
		if(childServices != null) {
			Thread thread = Thread.currentThread();
			for(int i = 0; i < childServices.length; i++) {
				childServices[i].isInstantiateMap.remove(thread);
			}
		}
	}
	
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
	
	private void setMultitonService(Object instance) throws Exception {
		if(childServices != null) {
			for(int i = 0; i < childServices.length; i++) {
				childServices[i].setField(instance, childFields[i]);
			}
		}
		clearCache();
	}
	
}
