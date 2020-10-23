package org.hutrace.handy.mapping;

import java.util.Collection;
import java.util.Map;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.annotation.Service;
import org.hutrace.handy.annotation.WebSocket;
import org.hutrace.handy.exception.NoRequestMethodException;
import org.hutrace.handy.exception.NoRequestUrlException;
import org.hutrace.handy.exception.NoWebSocketMappingException;
import org.hutrace.handy.http.HttpServerRequest;
import org.hutrace.handy.http.HttpServerSystem;
import org.hutrace.handy.language.SystemProperty;

/**
 * <p>对所有映射的注入进行储存
 * <p>你可以使用它来获取被注入的{@link Service}类和{@link DAO}类
 * <pre>
 *  eg: 
 *  	<code>MappingTable.instance.getService(parameter)</code>
 *  	<code>MappingTable.instance.getDao(parameter)</code>
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class MappingTable extends HttpServerSystem {
	
	/**
	 * 此链表的实列，此类不支持自己构建，如要使用，直接使用此实列即可
	 */
	public static final MappingTable instance = new MappingTable();
	
	private MappingTable() {}
	
	/**
	 * {@link DAO}的缓存Map
	 */
	private Map<String, Object> daoMap;
	
	/**
	 * {@link Service}的缓存Map
	 */
	private Map<String, TableService> serviceMap;
	
	/**
	 * {@link Controller}的缓存数组
	 */
	private TableController[] controllers;
	
	/**
	 * {@link WebSocket}的缓存数组
	 */
	private TableWebSocket[] websockets;
	
	private int controlLength;
	
	private int wsLength;
	
	/**
	 * <p>根据Class<?>获取注入的{@link Service}类
	 * @param clazs Class
	 * @return 带有{@link Service}注解的类
	 * @throws Exception 如果你需要获取的类、以及它引用的类有多例时，实例化错误时会抛出此异常
	 */
	public static Object getService(Class<?> clazs) throws Exception {
		return getService(clazs.getName());
	}
	
	/**
	 * <p>根据Class<?>获取注入的{@link Service}类
	 * @param name Class类名
	 * @return 带有{@link Service}注解的类
	 * @throws Exception 如果你需要获取的类、以及它引用的类有多例时，实例化错误时会抛出此异常
	 */
	public static Object getService(String name) throws Exception {
		return MappingTable.instance.serviceMap.get(name).getInstance();
	}
	
	/**
	 * <p>根据Class<?>获取注入的{@link DAO}类
	 * @param clazs Class
	 * @return 带有{@link DAO}注解的类
	 */
	public static Object getDao(Class<?> clazs) {
		return getDao(clazs.getName());
	}
	
	/**
	 * <p>根据Class<?>获取注入的{@link DAO}类
	 * @param clazs Class
	 * @return 带有{@link DAO}注解的类
	 */
	public static Object getDao(String name) {
		return MappingTable.instance.daoMap.get(name);
	}
	
	/**
	 * <p>初始化，同包下使用方法，仅仅用于在{@link MappingExecutor}中调用
	 * @param daoMap
	 * @param serviceMap
	 * @param controllerMap
	 * @param webSocketMap
	 */
	void init(Map<String, Object> daoMap, Map<String, TableService> serviceMap,
			Map<String, TableController> controllerMap, Map<String, TableWebSocket> webSocketMap) {
		this.daoMap = daoMap;
		this.serviceMap = serviceMap;
		initControllerMap(controllerMap);
		initWebSocketMap(webSocketMap);
	}
	
	/**
	 * <p>初始化HTTP的控制器({@link Controller})
	 * @param controllerMap
	 */
	private void initControllerMap(Map<String, TableController> controllerMap) {
		Collection<TableController> colls = controllerMap.values();
		controlLength = colls.size();
		controllers = new TableController[controlLength];
		int i = 0;
		for(TableController tc : colls) {
			controllers[i] = tc;
			i ++;
		}
		controllerMap = null;
	}
	
	/**
	 * <p>初始化WebSocket的控制器({@link WebSocket})
	 * @param webSocketMap
	 */
	private void initWebSocketMap(Map<String, TableWebSocket> webSocketMap) {
		Collection<TableWebSocket> ws = webSocketMap.values();
		wsLength = ws.size();
		websockets = new TableWebSocket[wsLength];
		int i = 0;
		for(TableWebSocket tws : ws) {
			websockets[i] = tws;
			i ++;
		}
		webSocketMap = null;
	}
	
	/**
	 * <p>HTTP请求的控制器
	 * <p>通过请求的url和请求类型(如: GET、POST等)来取得它需要执行哪个类中的哪个方法
	 * @param request
	 * @return {@link TableControlMethod}
	 * @throws NoRequestUrlException 没有找到请求的url时抛出的异常，异常中包含详细信息
	 * @throws NoRequestMethodException 没有找到请求类型时抛出的异常，异常中包含详细信息
	 */
	public TableControlMethod control(HttpServerRequest request) throws NoRequestUrlException, NoRequestMethodException {
		String url = request.url();
		String requestMethod = request.method().name();
		if(url.substring(url.length() - 1).equals("/")) {
			url = url.substring(0, url.length() - 1);
		}
		String nurl = url;
		String mapUrl = "";
		int index;
		boolean foundControl = false;
		boolean flag = true;
		while(flag) {
			if(url.equals(mapUrl)) {
				flag = false;
			}
			TableControlMethod[] methods;
			for(int i = 0; i < controlLength; i++) {
				if(controllers[i].mapping.equals(nurl) || matcherUrl(request, controllers[i].mapping, nurl)) {
					methods = controllers[i].methods;
					for(int j = 0; j < methods.length; j++) {
						if(methods[j].hasMap(mapUrl) || matcherUrl(request, methods[j].mapping, mapUrl)) {
							foundControl = true;
							if(methods[j].equalsRequestMethod(requestMethod)) {
								return methods[j];
							}
						}
					}
				}
			}
			index = nurl.lastIndexOf("/");
			mapUrl = (index == -1 ? nurl : nurl.substring(index)) + mapUrl;
			nurl = index == -1 ? "" : nurl.substring(0, index);
		}
		if(foundControl) {
			throw new NoRequestMethodException("Not found '" + requestMethod + "' request method");
		}else {
			throw new NoRequestUrlException("Not found '" + url + mapUrl + "' request method");
		}
	}
	
	/**
	 * <p>匹配url中的"{parameter}"格式
	 * @param request
	 * @param local
	 * @param source
	 * @return 是否匹配
	 */
	private boolean matcherUrl(HttpServerRequest request, String[] local, String source) {
		for(int i = 0; i < local.length; i++) {
			if(matcherUrl(request, local[i], source)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * <p>匹配url中的"{parameter}"格式
	 * @param request
	 * @param local
	 * @param source
	 * @return 是否匹配
	 */
	private boolean matcherUrl(HttpServerRequest request, String local, String source) {
		if(source.indexOf("{") > -1 || source.indexOf("}") > -1) {
			return false;
		}
		int start = local.indexOf("{");
		int end = local.indexOf("}");
		if(start > -1 && end > start) {// 包含"{parameter}"规则
			String[] sourceArr = source.split("/");
			String[] localArr = local.split("/");
			for(int i = 0; i < sourceArr.length; i++) {
				if(i >= localArr.length) {
					return false;
				}
				if(localArr[i].equals("")) {
					continue;
				}
				if(localArr[i].charAt(0) == '{' && localArr[i].charAt(localArr[i].length() - 1) == '}') {
					super.setRequestParameter(request, localArr[i].substring(1, localArr[i].length() - 1), sourceArr[i]);
					continue;
				}
				if(!sourceArr[i].equals(localArr[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * <p>WebSocket请求的控制器
	 * <p>通过请求的url和请求类型(如: GET、POST等)来取得它需要执行哪个类中的哪个方法
	 * @param url
	 * @return {@link TableWebSocketMethod}
	 * @throws NoWebSocketMappingException 没有找到请求的url时抛出的异常，异常中包含详细信息
	 */
	public TableWebSocketMethod websocket(String url) throws NoWebSocketMappingException {
		if(url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}
		if(url.charAt(0) != '/') {
			url = '/' + url;
		}
		String nurl = url;
		String mapUrl = "";
		int index;
		boolean flag = true;
		while(flag) {
			if(url.equals(mapUrl)) {
				flag = false;
			}
			TableWebSocketMethod[] methods;
			for(int i = 0; i < wsLength; i++) {
				if(websockets[i].mapping.equals(nurl)) {
					methods = websockets[i].methods;
					for(int j = 0; j < methods.length; j++) {
						if(methods[j].hasMap(mapUrl)) {
							return methods[j];
						}
					}
				}
			}
			index = nurl.lastIndexOf("/");
			mapUrl = (index == -1 ? nurl : nurl.substring(index)) + mapUrl;
			nurl = index == -1 ? "" : nurl.substring(0, index);
		}
		throw new NoWebSocketMappingException("Not found '" + url + "' request method");
	}
	
	/**
	 * <p>生成Controller控制器的debug日志数据
	 * @return debug日志数据
	 */
	String debugControl() {
		StringBuilder strb = new StringBuilder(SystemProperty.get("serve.mapping.debug.control"));
		for(TableController tc : controllers) {
			for(TableControlMethod tcm : tc.methods) {
				for(String methodMap : tcm.mapping) {
					strb.append("\r\n    ");
					strb.append(tc.mapping);
					strb.append(methodMap);
					strb.append(" ");
					strb.append("[");
					strb.append(tcm.requestMethod.name());
					strb.append("]");
					strb.append(" ==> ");
					strb.append(tc.className);
					strb.append("#");
					strb.append(tcm.method.getName());
				}
			}
		}
		strb.append("\r\n");
		return strb.toString();
	}
	
	/**
	 * <p>生成WebSocket控制器的debug日志数据
	 * @return debug日志数据
	 */
	String debugWebSocket() {
		StringBuilder strb = new StringBuilder(SystemProperty.get("serve.mapping.debug.ws"));
		for(TableWebSocket tc : websockets) {
			for(TableWebSocketMethod tcm : tc.methods) {
				for(String methodMap : tcm.mapping) {
					strb.append("\r\n    ");
					strb.append(tc.mapping);
					strb.append(methodMap);
					strb.append(" ==> ");
					strb.append(tc.className);
					strb.append("#");
					strb.append(tcm.method.getName());
				}
			}
		}
		strb.append("\r\n");
		return strb.toString();
	}
	
}
