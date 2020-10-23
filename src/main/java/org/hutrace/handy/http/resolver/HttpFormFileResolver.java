package org.hutrace.handy.http.resolver;

import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.converter.HttpFileConverter;
import org.hutrace.handy.http.converter.HttpMsgsConverter;
import org.hutrace.handy.http.file.HttpFormFile;
import org.hutrace.handy.http.file.HttpFormFileImp;

import io.netty.buffer.ByteBuf;

public class HttpFormFileResolver implements HttpMsgsResolver {

	private String[] contentType = {"multipart/form-data"};
	private int maxContentLength = 1048576000;// 100m
	
	@Override
	public Object parse(HttpRequest request) throws ResolverException {
		ByteBuf buf = request.content();
		HttpFormFile httpFormFile = new HttpFormFileImp(buf);
		return httpFormFile;
	}
	
	public boolean supports(String contentType) {
		for(int i = 0; i < this.contentType.length; i++) {
			if(contentType.indexOf(this.contentType[i]) > -1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getContentType() {
		return contentType;
	}

	@Override
	public HttpMsgsConverter converter() {
		return HttpFileConverter.instance;
	}
	
	@Override
	public int getMaxContentLength() {
		return maxContentLength;
	}
	
	/**
	 * <p>设置文件最大长度
	 * @param maxContentLength
	 */
	public void setMaxContentLength(int maxContentLength) {
		this.maxContentLength = maxContentLength;
	}
	
}
