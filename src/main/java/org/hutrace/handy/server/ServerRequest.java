package org.hutrace.handy.server;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

public interface ServerRequest {
	
	String id();
	
	/**
	 * 获取请求url
	 * 
	 * @return
	 */
	String url();

	/**
	 * 根据名称获取消息头
	 * 
	 * @param name
	 * @return
	 */
	String header(CharSequence name);

	/**
	 * 获取请求文本内容
	 * 
	 * @return
	 */
	String body();

	/**
	 * 获取请求连接信息
	 * 
	 * @return
	 */
	InetSocketAddress socketAddr();

	/**
	 * 获取请求方地址信息
	 * 
	 * @return
	 */
	InetAddress addr();

	/**
	 * 传入true调用{@link #ip(boolean)}
	 * 
	 * @see #ip(boolean)
	 * @return
	 */
	String ip();

	/**
	 * 获取请求方的ip
	 * <p>
	 * 根据agency判断，true则优先获取消息头，false则直接取连接ip。
	 * <p>
	 * 此方法预留在此，如有特殊情况还是自己手动获取较好。
	 * 
	 * @param agency true则优先获取消息头，false则直接取连接ip。
	 * @return
	 */
	String ip(boolean agency);

	/**
	 * 向request中写入自定义数据
	 * 
	 * @param name
	 * @param value
	 */
	void setAttr(String name, Object value);

	/**
	 * 获取自定义数据
	 * 
	 * @param name
	 * @return
	 */
	Object getAttr(String name);
	
	/**
	 * 获取请求对应的映射类
	 * 
	 * @return
	 */
	Class<?> control();
	
	/**
	 * 获取请求对应的映射方法
	 * 
	 * @return
	 */
	Method mapping();

	/**
	 * 获取请求对应的映射方法上的所有参数名
	 * 
	 * @return
	 */
	String[] mappingParameterNames();
	
	/**
	 * 写入请求参数
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	ServerRequest setParameter(String name, String value);
	
	/**
	 * 根据名称获取qs参数
	 * 
	 * @param name
	 * @return
	 */
	String parameter(String name);

	/**
	 * 获取所有qs参数
	 * 
	 * @return
	 */
	Map<String, String> parameters();
	
}
