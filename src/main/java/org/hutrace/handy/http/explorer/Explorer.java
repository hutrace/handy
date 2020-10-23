package org.hutrace.handy.http.explorer;

import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.server.ServerRequest;
import org.hutrace.handy.server.ServerResponse;

public interface Explorer {
	
	void open(ServerRequest request, ServerResponse response);
	
	void close(ServerRequest request, ServerResponse response, ExceptionBean eb);
	
}
