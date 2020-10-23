package org.hutrace.handy.websocket;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.exception.WebSocketHeaderParseException;

import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class WebSocketHeaders {
	
	public static class Names {
		static final String DATA_TYPE = "data-type";
		static final String URL = "url";
		static final String REQUEST_TYPE = "request-type";
		static final String CLIENT_ID = "client-id";
	}
	
	static class Values {
		static final String REQUEST_TYPE_CONNECT = "connect";
		static final String REQUEST_TYPE_INTERACTIVE = "interactive";
	}
	
	private Map<String, String> map;
	private static final byte[] LINE_BREAK = {13, 10};
	private static final byte HEADER_APLIT = 58;
	
	private static final String MSG_FORMAT_HINT = "Header message format error, The format example: \r\nname: value\r\nname: value\r\n\r\ndata......";
	
	private byte[] cacheNames;
	
	public WebSocketHeaders() {
		map = new HashMap<String, String>();
	}
	
	public WebSocketHeaders(ByteBuf buf) throws WebSocketHeaderParseException {
		map = new HashMap<String, String>();
		readHeaders(buf);
	}
	
	private void readHeaders(ByteBuf buf) throws WebSocketHeaderParseException {
		byte[] values;
		while(true) {
			try {
				values = readLine(buf);
				if(values == null) {
					break;
				}
			}catch (IndexOutOfBoundsException e) {
				throw new WebSocketHeaderParseException(MSG_FORMAT_HINT);
			}
			if(cacheNames == null) {
				throw new WebSocketHeaderParseException(MSG_FORMAT_HINT);
			}
			map.put(new String(cacheNames, CharsetUtil.UTF_8).trim().toLowerCase(),
					new String(values, 0, values.length - 2, CharsetUtil.UTF_8).trim());
		}
		cacheNames = null;
		values = null;
	}

	private byte[] readLine(ByteBuf buf) {
		cacheNames = null;
		byte[] byt = new byte[0];
		while(!isLineBreak(byt)) {
			byt = append(byt, buf.readByte());
		}
		return byt.length == 2 ? null : byt;
	}
	
	private byte[] append(byte[] source, byte byt) {
		if(source.length == 0) {
			source = new byte[1];
			source[0] = byt;
			return source;
		}
		if(byt == HEADER_APLIT) {
			cacheNames = source;
			return new byte[0];
		}
		byte[] dst = new byte[source.length + 1];
		System.arraycopy(source, 0, dst, 0, source.length);
		dst[source.length] = byt;
		return dst;
	}
	
	private boolean isLineBreak(byte[] byt) {
		if(byt.length < 2) {
			return false;
		}
		return byt[byt.length - 1] == LINE_BREAK[1] && byt[byt.length - 2] == LINE_BREAK[0];
	}
	
	public WebSocketHeaders add(String name, String value) {
		map.put(name, value);
		return this;
	}
	
	public String header(String name) {
		return map.get(name);
	}
	
	void remove(String name) {
		map.remove(name);
	}
	
	public Set<String> names() {
		return map.keySet();
	}
	
	public Collection<String> values() {
		return map.values();
	}
	
	public Set<Entry<String, String>> entry() {
		return map.entrySet();
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
}
