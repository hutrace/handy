package org.hutrace.handy.mapping;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.config.Pattern;

/**
 * <p>{@link Service}表（包装类）
 * <p>用于储存{@link Service}的实列、它调用的{@link Service}、以及它调用的{@link DAO}
 * <p>并且还储存了它调用{@link Service}的字段属性。
 * <p>当然，这些所有的储存信息仅仅在多例模式下才会有的。
 * @author hutrace
 * @see TableDao
 * @see Field
 * @since 1.8
 * @version 1.0
 */
public class TableService {
	
	/**
	 * 当前{@link Service}的实列
	 */
	Object instance;
	
	/**
	 * 当前{@link Service}的类名称（全路径）
	 */
	String className;
	
	/**
	 * 当前{@link Service}的Class属性
	 */
	Class<?> clazs;
	
	/**
	 * 当前{@link Service}是否为单例
	 */
	boolean singleton;
	
	/**
	 * 当前{@link Service}调用的子{@link Service}包装类{@link TableService}
	 */
	TableService[] childServices;
	
	/**
	 * 当前{@link Service}调用的子{@link Service}字段属性
	 */
	Field[] childServiceFields;
	
	/**
	 * 当前{@link Service}调用的{@link DAO}包装类{@link TableDao}
	 */
	TableDao[] childDaos;
	
	/**
	 * <p>通过Class构建当前实列
	 * @param clazs
	 * @throws Exception
	 */
	TableService(Class<?> clazs) throws Exception {
		this.clazs = clazs;
		initSingleton();
		initInstance();
	}
	
	/**
	 * <p>初始化当前类是否为单例
	 */
	private void initSingleton() {
		Service ser = clazs.getAnnotation(Service.class);
		Pattern pattern = ser.pattern();
		if(pattern == Pattern.DEFAULT) {
			pattern = Configuration.pattern();
		}
		singleton = (pattern == Pattern.SINGLETON);
	}
	
	/**
	 * <p>初始化当前{@link Service}的实列
	 * @throws Exception
	 */
	private void initInstance() throws Exception {
		if(singleton) {
			instance = clazs.newInstance();
		}
	}
	
	/**
	 * <p>添加单例模式下的子{@link Service}包装类{@link TableService}
	 * @throws Exception
	 */
	private void addSingletonChildServices() throws Exception {
		if(childServices != null) {
			for(int i = 0; i < childServices.length; i++) {
				if(!childServices[i].singleton) {
					childServices[i].setField(instance, childServiceFields[i]);
				}
			}
		}
	}
	
	/**
	 * <p>添加多例模式下的子{@link Service}包装类{@link TableService}
	 * @throws Exception
	 */
	private void addMultitonChildServices() throws Exception {
		if(childServices != null) {
			for(int i = 0; i < childServices.length; i++) {
				childServices[i].setField(instance, childServiceFields[i]);
			}
		}
	}
	
	/**
	 * <p>添加当前{@link Service}调用的{@link DAO}封装类{@link TableDao}
	 * @throws Exception
	 */
	private void addChildDaos() throws Exception {
		if(childDaos != null) {
			for(int i = 0; i < childDaos.length; i++) {
				childDaos[i].setServiceField(instance);
			}
		}
	}
	
	/**
	 * <p>是否初始化当前类的缓存，用于处理在多例模式下，多层{@link Service}互相调用，会无限创建{@link Service}的问题
	 * <p>每次使用完成记得清空
	 */
	Map<Thread, Boolean> isInstantiateMap = new HashMap<>();
	
	/**
	 * <p>设置当前{@link Service}调用的{@link Service}字段属性
	 * @param service
	 * @param field
	 * @throws Exception
	 */
	void setField(Object service, Field field) throws Exception {
		Thread thread = Thread.currentThread();
		if(isInstantiateMap.get(thread) == null) {
			isInstantiateMap.put(thread, true);
			if(singleton) {
				addSingletonChildServices();
			}else {
				instance = clazs.newInstance();
				addMultitonChildServices();
				addChildDaos();
			}
		}
		field.set(service, instance);
	}
	
	/**
	 * <p>获取当前{@link Service}的实列
	 * <p>如果当前{@link Service}为多例，则会创建实列并返回。
	 * @return 当前{@link Service}的实列
	 * @throws Exception
	 */
	Object getInstance() throws Exception {
		Thread thread = Thread.currentThread();
		if(isInstantiateMap.get(thread) == null) {
			if(singleton) {
				addSingletonChildServices();
			}else {
				instance = clazs.newInstance();
				addMultitonChildServices();
				addChildDaos();
			}
		}
		return instance;
	}
	
}
