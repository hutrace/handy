package org.hutrace.handy.server;

public interface ServerResponse {
	
	/**
	 * 当前单次会话请求id
	 * <p>建议实现者使用uuid实现
	 * <p>此id需要保持唯一性
	 * @return
	 */
	String id();
	
	/**
	 * 向响应通道写入数据
	 * @param bytes
	 */
	void write(byte[] bytes);
	
	/**
	 * 刷新所有挂起的消息
	 */
	void flush();
	
	/**
	 * 关闭通道
	 */
	void close();
	
	/**
	 * 设置响应消息头
	 * @param name
	 * @param value
	 */
	void setHeader(CharSequence name, Object value);
	
	/**
	 * 获取响应消息头
	 * @param name
	 * @return
	 */
	String header(CharSequence name);
	
	/**
	 * 删除响应消息头
	 * @param name
	 */
	void removeHeader(CharSequence name);
	
}
