package org.hutrace.handy.http.file;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.exception.HttpParseFileException;

import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class HttpFormFileHeaders {
	
	private Map<String, String> map;
	private static final byte[] LINE_BREAK = {13, 10};
	private static final byte HEADER_APLIT = 58;
	
	private static final String MSG_FORMAT_HINT = "Header message format error, The format example: \r\nname: value\r\nname: value\r\n\r\ndata......";
	private String contentDisposition;
	private String contentType;
	private String name;
	private String filename;
	private String boundary;
	
	private byte[] cacheNames;
	
	public HttpFormFileHeaders() {
		map = new HashMap<String, String>();
	}
	
	public HttpFormFileHeaders(ByteBuf buf) throws HttpParseFileException {
		map = new HashMap<String, String>();
		readHeaders(buf);
		parseFileInfo();
		headerClear();
	}
	
	public String getContentDisposition() {
		return contentDisposition;
	}
	public String getContentType() {
		return contentType;
	}
	public String getName() {
		return name;
	}
	public String getFilename() {
		return filename;
	}
	public String getBoundary() {
		return boundary;
	}
	
	private void parseFileInfo() {
		contentType = map.get("content-type");
		contentDisposition = map.get("content-disposition");
		String[] arr = contentDisposition.split(";");
		contentDisposition = arr[0];
		name = arr[1].substring(arr[1].indexOf("=") + 2, arr[1].length() - 1);
		filename = arr[2].substring(arr[2].indexOf("=") + 2, arr[2].length() - 1);
	}
	
	private void headerClear() {
		map.remove("content-type");
		map.remove("content-disposition");
	}
	
	private void readHeaders(ByteBuf buf) throws HttpParseFileException {
		byte[] values;
		while(true) {
			try {
				values = readLine(buf);
				if(values == null) {
					break;
				}
			}catch (IndexOutOfBoundsException e) {
				throw new HttpParseFileException(MSG_FORMAT_HINT);
			}
			if(cacheNames == null) {
				if(boundary == null) {
					boundary = new String(values, 0, values.length - 2, CharsetUtil.UTF_8).trim();
				}
				continue;
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
	
	public HttpFormFileHeaders add(String name, String value) {
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

	public String toFileString() {
		return "HttpFileHeaders [contentDisposition=" + contentDisposition
				+ ", contentType=" + contentType + ", name=" + name
				+ ", filename=" + filename + ", boundary=" + boundary + "]";
	}
	
}
