package org.hutrace.handy.http.listener;

import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;

public interface ChannelListener {
	
	void closed(HttpRequest request, HttpResponse response, Object parameter, Object result);
	
}
