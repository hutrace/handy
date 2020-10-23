package org.hutrace.handy.websocket;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.WebSocketHeaderParseException;
import org.hutrace.handy.server.ServerRequest;

import java.util.Set;

import io.netty.buffer.ByteBuf;

public class WebSocketRequest implements ServerRequest {
	
	private WebSocketHeaders headers;
	
	private ByteBuf buf;
	
	private InetSocketAddress remoteAddress;
	
	private String id;
	
	private String requestType;
	private String dataType;
	private String event;
	private Map<String, Object> attr;
	/**
	 * 连接服务的客户端ip
	 */
	private String connectIp;

	private Method mapping;
	private String[] mappingParameterNames;
	private Class<?> control;
	private Map<String, String> parameters;
	
	public WebSocketRequest(ByteBuf buf, SocketAddress remoteAddress, String id) throws WebSocketHeaderParseException {
		parameters = new HashMap<>();
		this.remoteAddress = (InetSocketAddress) remoteAddress;
		headers = new WebSocketHeaders(buf);
		this.buf = buf;
		this.id = id;
		checkHeaders();
		connectIp = addr().getHostAddress();
	}
	
	private void checkHeaders() {
		checkRequestType();
		if(!isConnect()) {
			checkDataType();
			checkUrl();
		}
	}
	
	private void checkRequestType() {
		requestType = headers.header(WebSocketHeaders.Names.REQUEST_TYPE);
		if(requestType == null) {
			throw new WebSocketHeaderParseException("Header message format error, The 'request-type' cannot be null");
		}
		headers.remove(WebSocketHeaders.Names.REQUEST_TYPE);
	}
	
	private void checkDataType() {
		dataType = headers.header(WebSocketHeaders.Names.DATA_TYPE);
		if(dataType == null) {
			throw new WebSocketHeaderParseException("Header message format error, The 'data-type' cannot be null");
		}
		headers.remove(WebSocketHeaders.Names.DATA_TYPE);
	}
	
	private void checkUrl() {
		String uri = headers.header(WebSocketHeaders.Names.URL);
		if(uri == null || uri.isEmpty()) {
			throw new WebSocketHeaderParseException("Header message format error, The 'event' cannot be null");
		}
		String[] arr = uri.split("\\?");
		event = arr[0];
		if(!event.isEmpty()) {
			throw new WebSocketHeaderParseException("Header message format error, The 'event' cannot be null");
		}
		if(arr.length > 1) {
			for(int i = 1; i < arr.length; i++) {
				parseQueryString(arr[i]);
			}
		}
		headers.remove(WebSocketHeaders.Names.URL);
	}
	
	private void parseQueryString(String str) {
		String[] arr = str.split("&");
		String[] itemArr;
		for(String item : arr) {
			if(!item.isEmpty()) {
				itemArr = item.split("=");
				if(itemArr.length == 2) {
					parameters.put(itemArr[0], itemArr[1]);
				}
			}
		}
	}
	
	public WebSocketHeaders headers() {
		return headers;
	}
	
	public String id() {
		return id;
	}
	
	public String dataType() {
		return dataType;
	}
	
	public String requestType() {
		return requestType;
	}
	
	public boolean isConnect() {
		return WebSocketHeaders.Values.REQUEST_TYPE_CONNECT.equals(requestType);
	}
	
	public byte[] bytes() {
		byte[] byt = new byte[buf.readableBytes()];
		buf.readBytes(byt);
		return byt;
	}
	
	public ByteBuf context() {
		return buf;
	}

	@Override
	public String url() {
		return event;
	}

	@Override
	public String header(CharSequence name) {
		return headers.header(name.toString());
	}

	@Override
	public String body() {
		return new String(bytes(), Charset.forName(Configuration.charset()));
	}

	@Override
	public InetSocketAddress socketAddr() {
		return remoteAddress;
	}

	@Override
	public InetAddress addr() {
		return remoteAddress.getAddress();
	}
	
	@Override
	public String ip() {
		return ip(true);
	}
	
	@Override
	public String ip(boolean agency) {
		return connectIp;
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
	
	public WebSocketRequest setControl(Class<?> control) {
		this.control = control;
		return this;
	}
	
	public WebSocketRequest setMapping(Method mapping) {
		this.mapping = mapping;
		return this;
	}
	
	public WebSocketRequest setMappingParameterNames(String[] mappingParameterNames) {
		this.mappingParameterNames = mappingParameterNames;
		return this;
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
	public ServerRequest setParameter(String name, String value) {
		parameters.put(name, value);
		return this;
	}

	@Override
	public String parameter(String name) {
		return parameters.get(name);
	}

	@Override
	public Map<String, String> parameters() {
		return parameters;
	}
	
	public void putParameters(Map<String, Object> parameters) {
		Set<Entry<String, Object>> set = parameters.entrySet();
		for(Entry<String, Object> entry : set) {
			this.parameters.put(entry.getKey(), entry.getValue() == null ? null : entry.getValue().toString());
		}
	}
	
	public void addParameters(Map<String, String> parameters) {
		this.parameters.putAll(parameters);
	}

}
