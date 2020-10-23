package org.hutrace.handy.http;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.language.ApplicationProperty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 * <p>HttpRequest请求体
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class HttpServerRequest implements HttpRequest {
	
	/**
	 * 客户端真实ip
	 */
	private String ip;
	
	/**
	 * 连接服务的客户端ip
	 */
	private String connectIp;
	
	/**
	 * 请求url
	 */
	private String url;
	
	/**
	 * 请求消息
	 */
	private StringBuilder body;
	
	/**
	 * 请求
	 */
	private Channel channel;
	private Map<String, String> parameters;
	private Map<String, Object> attr;
	private final FullHttpRequest request;
	private Method mapping;
	private String[] mappingParameterNames;
	
	private String id;
	
	private Class<?> control;
	
	public HttpServerRequest(FullHttpRequest request, Channel channel) {
		this.request = request;
		this.channel = channel;
		initParameters();
		ip = ip1();
		connectIp = addr().getHostAddress();
	}
	
	private void initParameters() {
		parameters = new HashMap<>();
		QueryStringDecoder decoder = new QueryStringDecoder(uri());
		Map<String, List<String>> paramList = decoder.parameters();
		for(Map.Entry<String, List<String>> entry : paramList.entrySet()) {
			parameters.put(entry.getKey(), entry.getValue().get(0));
		}
	}
	
	@Deprecated
	@Override
	public HttpMethod getMethod() {
		return request.getMethod();
	}
	
	@Override
	public HttpMethod method() {
		return request.method();
	}
	
	@Deprecated
	@Override
	public String getUri() {
		return request.getUri();
	}
	
	@Override
	public String uri() {
		return request.uri();
	}
	
	@Deprecated
	@Override
	public HttpVersion getProtocolVersion() {
		return request.getProtocolVersion();
	}
	
	@Override
	public HttpVersion protocolVersion() {
		return request.protocolVersion();
	}
	
	@Override
	public HttpHeaders headers() {
		return request.headers();
	}
	
	@Deprecated
	@Override
	public DecoderResult getDecoderResult() {
		return request.getDecoderResult();
	}
	
	@Override
	public DecoderResult decoderResult() {
		return request.decoderResult();
	}
	
	@Override
	public void setDecoderResult(DecoderResult result) {
		request.setDecoderResult(result);
	}
	
	@Override
	public HttpHeaders trailingHeaders() {
		return request.trailingHeaders();
	}
	
	@Override
	public ByteBuf content() {
		return request.content();
	}
	
	@Override
	public int refCnt() {
		return request.refCnt();
	}
	
	@Override
	public boolean release() {
		return request.release();
	}
	
	@Override
	public boolean release(int decrement) {
		return request.release(decrement);
	}
	
	@Override
	public HttpRequest copy() {
		return replace(content().copy());
	}
	
	@Override
	public HttpRequest duplicate() {
		return replace(content().duplicate());
	}
	
	@Override
	public HttpRequest retainedDuplicate() {
		return replace(content().retainedDuplicate());
	}
	
	@Override
	public HttpRequest replace(ByteBuf content) {
		FullHttpRequest ftr = request.replace(content);
		return new HttpServerRequest(ftr, channel);
	}
	
	@Override
	public HttpRequest retain(int increment) {
		request.retain(increment);
		return this;
	}
	
	@Override
	public HttpRequest retain() {
		request.retain();
		return this;
	}
	
	@Override
	public HttpRequest touch() {
		request.touch();
		return this;
	}
	
	@Override
	public HttpRequest touch(Object hint) {
		request.touch(hint);
		return this;
	}
	
	@Override
	public HttpRequest setProtocolVersion(HttpVersion version) {
		request.setProtocolVersion(version);
		return this;
	}
	
	@Override
	public HttpRequest setMethod(HttpMethod method) {
		request.setMethod(method);
		return this;
	}
	
	@Override
	public HttpRequest setUri(String uri) {
		request.setUri(uri);
		return this;
	}
	
	@Override
	public String url() {
		if(url == null) {
			StringBuilder strb = new StringBuilder();
			String uri = uri();
			for(int i = 0; i < uri.length(); i++) {
				char c = uri.charAt(i);
				if(c == '?' || c == '#') {
					break;
				}
				strb.append(c);
			}
			url = strb.toString();
		}
		return url;
	}
	
	@Override
	public String header(CharSequence name) {
		return request.headers().get(name);
	}
	
	@Override
	public String contentType() {
		return request.headers().get(HttpHeaderNames.CONTENT_TYPE);
	}
	
	@Override
	public String parameter(String name) {
		return parameters().get(name);
	}

	@Override
	public HttpRequest setParameter(String name, String value) {
		parameters.put(name, value);
		return this;
	}
	
	@Override
	public Map<String, String> parameters() {
		return parameters;
	}
	
	@Override
	public String body() {
		if(body == null) {
			byte[] byteContent = new byte[content().readableBytes()];
			content().readBytes(byteContent);
			try {
				body = new StringBuilder(new String(byteContent, Configuration.charset()));
			}catch (UnsupportedEncodingException e) {
				throw new RuntimeException(ApplicationProperty.get("serve.req.error.body"), e);
			}
		}
		return body.toString();
	}
	
	@Override
	public Channel channel() {
		return channel;
	}
	
	@Override
	public InetSocketAddress socketAddr() {
		return (InetSocketAddress) channel.remoteAddress();
	}
	
	@Override
	public InetAddress addr() {
		return socketAddr().getAddress();
	}
	
	@Override
	public String ip() {
		return ip(true);
	}
	
	@Override
	public String ip(boolean agency) {
		if(agency) {
			return ip;
		}else {
			return connectIp;
		}
	}
	
	private String ip1() {
		// Squid代理
		String ip = header("X-Forwarded-For");
		String unknown = "unknown";
		if(ip == null || ip.isEmpty() || unknown.equals(ip)) {
			// Apache代理
			ip = header("Proxy-Client-IP");
		}
		if(ip == null || ip.isEmpty() || unknown.equals(ip)) {
			// Weblogic代理
			ip = header("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.isEmpty() || unknown.equals(ip)) {
			// Nginx代理
			ip = header("X-Real-IP");
		}
		if(ip == null || ip.isEmpty() || unknown.equals(ip)) {
			// 其它代理
			ip = header("HTTP_CLIENT_IP");
		}
		// 有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
		if(ip != null && !ip.isEmpty()) {
			ip = ip.split(",")[0];
		}
		if(ip == null || ip.isEmpty() || unknown.equals(ip)) {
			ip = addr().getHostAddress();
		}
		if(ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	
	@Override
	public void setAttr(String name, Object value) {
		if(attr == null) {
			attr = new HashMap<>();
		}
		attr.put(name, value);
	}
	
	@Override
	public Object getAttr(String name) {
		if(attr == null) {
			return null;
		}
		return attr.get(name);
	}
	
	public HttpServerRequest setMapping(Method mapping) {
		this.mapping = mapping;
		return this;
	}
	
	public HttpServerRequest setMappingParameterNames(String[] mappingParameterNames) {
		this.mappingParameterNames = mappingParameterNames;
		return this;
	}
	
	public void setControl(Class<?> control) {
		this.control = control;
	}

	@Override
	public Class<?> control() {
		return control;
	}
	
	@Override
	public Method mapping() {
		return mapping;
	}
	
	@Override
	public String[] mappingParameterNames() {
		return mappingParameterNames;
	}

	@Override
	public String id() {
		if(id == null) {
			id = UUID.randomUUID().toString().replace("-", "");
		}
		return id;
	}
	
}
