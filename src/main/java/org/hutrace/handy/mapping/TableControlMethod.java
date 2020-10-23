package org.hutrace.handy.mapping;

import java.io.IOException;
import java.lang.reflect.Method;

import org.hutrace.handy.annotation.RequestMapping;
import org.hutrace.handy.annotation.verify.Validated;
import org.hutrace.handy.http.RequestMethod;
import org.hutrace.handy.utils.ServeUtil;

/**
 * <p>控制器中的方法表（包装类）
 * <p>用于储存控制器中的接口方法（包含{@link RequestMapping}的方法）
 * <p>以及绑定与控制器的关系
 * @author hutrace
 * @see TableController
 * @see Method
 * @since 1.8
 * @version 1.0
 */
public class TableControlMethod {
	
	/**
	 * 方法详情
	 */
	Method method;
	
	/**
	 * 当前方法映射的请求类型
	 */
	RequestMethod requestMethod;
	
	/**
	 * 当前方法的映射地址数组
	 */
	String[] mapping;
	
	/**
	 * 方法中包含的参数名称
	 */
	private String[] parameters;
	
	/**
	 * 方法是否需要参数验证
	 */
	private boolean verify;
	
	/**
	 * 当前方法所属的控制器包装类{@link TableController}
	 */
	TableController parent;
	
	/**
	 * <p>构造{@link TableControlMethod}
	 * <p>通过方法对象{@link Method}、映射对象{@link RequestMapping} 进行构造
	 * @param method 方法对象
	 * @param map 映射对象
	 * @throws IOException
	 */
	public TableControlMethod(Method method, RequestMapping map, TableController parent) throws IOException {
		this(method, map, method.getAnnotation(Validated.class) != null, parent);
	}
	
	/**
	 * <p>构造{@link TableControlMethod}
	 * <p>通过方法对象{@link Method}、映射对象{@link RequestMapping}进行构造、是否需要验证参数 进行构造
	 * @param method 方法对象
	 * @param map 映射对象
	 * @param verify 是否需要验证参数
	 * @throws IOException 异常情况: 
	 * <br>构造方法时会初始化方法上的参数名称, 当参数名称初始化错误时, 将会抛出异常{@link IOException}
	 */
	public TableControlMethod(Method method, RequestMapping map, boolean verify, TableController parent) throws IOException {
		this.method = method;
		this.verify = verify;
		this.parent = parent;
		requestMethod = map.method();
		mapping = initMapping(map.value());
		parameters = ServeUtil.getMethodParametersNames(method);
	}
	
	/**
	 * <p>初始化方法上映射对象{@link RequestMapping}的映射地址
	 * @param map 原始映射地址
	 * @return 处理后的映射地址
	 */
	private String[] initMapping(String[] map) {
		String[] res = new String[map.length];
		for(int i = 0; i < res.length; i++) {
			if(map[i].equals("")) {
				res[i] = "";
			}else {
				res[i] = ("/" + map[i]).replace("//", "/");
			}
		}
		return res;
	}
	
	/**
	 * <p>根据映射地址判断是否含有此映射地址
	 * @param mapUrl 映射地址
	 * @return 如果有则返回true, 没有则返回false
	 */
	public boolean hasMap(String mapUrl) {
		for(int i = 0; i < mapping.length; i++) {
			if(mapping[i].equals(mapUrl)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>根据传入的请求类型(requestMethod)判断当前的请求类型是否相等
	 * @param requestMethod 请求类型
	 * @return 如果相等返回true, 不相等则返回false
	 */
	public boolean equalsRequestMethod(String requestMethod) {
		return this.requestMethod.name().equals(requestMethod);
	}
	
	/**
	 * <p>根据传入的请求类型(requestMethod)判断当前的请求类型是否相等
	 * @param requestMethod 请求类型
	 * @return 如果相等返回true, 不相等则返回false
	 */
	public boolean equalsRequestMethod(RequestMethod requestMethod) {
		return this.requestMethod == requestMethod;
	}
	
	/**
	 * <p>调用当前{@link #method}
	 * <p>通过parent记录它所属的控制器, 使用此方法{@link #invoke(Object...)}调用当前{@link #method}
	 * @param args 传入方法的参数
	 * @return 方法的返回参数
	 * @throws Exception 调用失败会抛出异常{@link Exception}
	 */
	public Object invoke(Object... args) throws Exception {
		return parent.invoke(method, args);
	}
	
	/**
	 * <p>获取当前{@link #method}
	 * @return 当前{@link #method}
	 */
	public Method method() {
		return method;
	}
	
	/**
	 * <p>获取当前{@link #method}的参数名称
	 * @return 当前{@link #method}的参数名称
	 */
	public String[] parameters() {
		return parameters;
	}
	
	/**
	 * <p>获取当前{@link #method}是否需要参数验证
	 * @return 当前{@link #method}是否需要参数验证
	 */
	public boolean verify() {
		return verify;
	}
	
	/**
	 * 获取当前映射的类
	 * @return
	 */
	public Class<?> control() {
		return parent.clazs;
	}
	
}