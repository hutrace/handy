package org.hutrace.handy.server;

import org.hutrace.handy.websocket.WebSocketResponse;
import org.hutrace.handy.websocket.WebSocketResult;
import org.hutrace.handy.websocket.cache.InputClient;
import org.hutrace.handy.websocket.cache.OutputClient;

public class WebSocketClients extends InputClient {
	
	private static final WebSocketClients clients = new WebSocketClients();
	private static final Output output = new Output();
	
	/**
	 * <p>新增客户端,当客户端连接时储存连接的客户端
	 * @param request
	 * @param response
	 */
	static void addClient(String id, WebSocketResponse response) {
		clients.add(id, response);
	}
	
	/**
	 * <p>删除客户端,当客户端出错或者客户端关闭时需要将客户端清除
	 * @param id
	 */
	static void removeClient(String id) {
		clients.remove(id);
	}
	
	/**
	 * <p>获取客户端
	 * <p>当客户端不是第一次连接时,需要获取到储存的对应客户端进行操作（如向客户端推送数据）
	 * <p>或者在需要手动操作客户端时进行获取并手动操作
	 * @param id
	 * @return WebSocketResponse
	 */
	public static WebSocketResponse getClient(String id) {
		return clients.get(id);
	}
	
	/**
	 * <p>向客户端发送数据
	 * @param id 客户端ID
	 * @param event 向客户端发送的事件
	 * @param data 向客户端发送的数据
	 */
	public static void writeAndFlush(String id, String event, Object data) {
		output.writeAndFlush1(id, event, data);
	}
	
	/**
	 * <p>向客户端发送数据
	 * @param id 客户端ID
	 * @param event 向客户端发送的事件
	 * @param code 向客户端发送的状态码
	 * @param msg 向客户端发送的提示信息
	 * @param data 向客户端发送的数据
	 */
	public static void writeAndFlush(String id, String event, int code, String msg, Object data) {
		output.writeAndFlush1(id, event, code, msg, data);
	}
	
	/**
	 * <p>向客户端发送数据
	 * <p>需要传入{@link WebSocketResult}类型数据
	 * @param id 客户端ID
	 * @param result 需要发送的数据
	 */
	public static void writeAndFlush(String id, WebSocketResult result) {
		output.writeAndFlush1(id, result);
	}
	
	static class Output extends OutputClient {
		
		void writeAndFlush1(String id, String event, Object data) {
			super.writeAndFlush(id, event, data);
		}
		
		void writeAndFlush1(String id, String event, int code, String msg, Object data) {
			super.writeAndFlush(id, event, code, msg, data);
		}
		
		void writeAndFlush1(String id, WebSocketResult result) {
			super.writeAndFlush(id, result);
		}
		
	}
	
}
