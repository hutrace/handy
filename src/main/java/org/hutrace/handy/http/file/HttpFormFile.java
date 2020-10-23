package org.hutrace.handy.http.file;

public interface HttpFormFile extends HttpFile {
	
	String getHeader(String name);
	
	HttpFormFileHeaders getHeaders();
	
}
