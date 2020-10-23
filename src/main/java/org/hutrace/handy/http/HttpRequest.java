package org.hutrace.handy.http;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;

import org.hutrace.handy.server.ServerRequest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Http请求对象接口
 * 
 * @author hu trace
 *
 */
public interface HttpRequest extends FullHttpRequest, ServerRequest {
	
	/**
	 * 当前单次会话请求id
	 * <p>建议实现者使用uuid实现
	 * <p>此id需要保持唯一性
	 * @return
	 */
	@Override
	String id();
	
	@Override
	HttpRequest copy();

	@Override
	HttpRequest duplicate();

	@Override
	HttpRequest retainedDuplicate();

	@Override
	HttpRequest replace(ByteBuf content);

	@Override
	HttpRequest retain(int increment);

	@Override
	HttpRequest retain();

	@Override
	HttpRequest touch();

	@Override
	HttpRequest touch(Object hint);

	@Override
	HttpRequest setProtocolVersion(HttpVersion version);

	@Override
	HttpRequest setMethod(HttpMethod method);

	@Override
	HttpRequest setUri(String uri);

	@Override
	String url();

	/**
	 * 根据名称获取消息头
	 * <p>
	 * 你可以直接使用{@link HttpHeaderNames}中的常量
	 * <p>
	 * 也可以直接使用字符串
	 * 
	 * @param name
	 * @return
	 */
	@Override
	String header(CharSequence name);

	/**
	 * 获取消息类型
	 * 
	 * @return
	 */
	String contentType();

	@Override
	ServerRequest setParameter(String name, String value);

	@Override
	String parameter(String name);

	@Override
	Map<String, String> parameters();

	@Override
	String body();

	/**
	 * 获取请求连接通道
	 * 
	 * @return
	 */
	Channel channel();

	@Override
	InetSocketAddress socketAddr();

	@Override
	InetAddress addr();

	@Override
	String ip();

	@Override
	String ip(boolean agency);

	@Override
	void setAttr(String name, Object value);

	@Override
	Object getAttr(String name);
	
	@Override
	Class<?> control();
	
	@Override
	Method mapping();

	@Override
	String[] mappingParameterNames();

}
