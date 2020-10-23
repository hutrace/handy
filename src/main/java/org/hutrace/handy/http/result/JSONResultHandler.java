package org.hutrace.handy.http.result;

import java.io.File;
import java.io.IOException;

import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.http.file.HttpDownload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * JSON响应数据处理器
 * <p>实现{@link ResultHandler}接口
 * <p>该类会判断是否异常/文件下载，如果都不是，则会构造{@link JSONResultBean}返回
 * @author hu trace
 *
 */
public class JSONResultHandler implements ResultHandler {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 判断处理可能出现的类型
	 * <p>File类型，文件下载
	 * <p>ExceptionBean类型，异常中转
	 */
	@Override
	public ResultBean dispose(HttpRequest request, HttpResponse response, Object msg) {
		if(msg instanceof ResultBean) {
			return (ResultBean) msg;
		}
		ResultBean rb;
		if(msg instanceof File) {
			rb = disposeFile(response, (File) msg);
		}else if(msg instanceof HttpDownload) {
			rb = disposeFile(response, (HttpDownload) msg);
		}else if(msg instanceof ExceptionBean) {
			rb = disposeException(response, (ExceptionBean) msg);
		}else {
			rb = new JSONResultBean();
			rb.setCode(0);
			rb.setMsg("ok");
			rb.setData(msg);
		}
		if(rb != null) {
			rb.setTimestamp(System.currentTimeMillis());
			rb.setReqId(request.id());
			rb.setRspId(response.id());
		}
		return rb;
	}
	
	/**
	 * 处理文件下载
	 * @param response
	 * @param file
	 * @return
	 */
	private ResultBean disposeFile(HttpResponse response, HttpDownload file) {
		try {
			response.setContentType(file.getContentType());
			response.setHeader(HttpHeaderNames.CONTENT_DISPOSITION, file.getDisposition());
			response.writeAndFlush(file.getFile());
			return null;
		}catch (IOException e) {
			log.error(e.getMessage(), e);
			return disposeException(response, new ExceptionBean(e, -1, e.getMessage()));
		}
	}
	
	/**
	 * 处理文件下载
	 * @param response
	 * @param file
	 * @return
	 */
	private ResultBean disposeFile(HttpResponse response, File file) {
		try {
			response.writeAndFlush(file);
			return null;
		}catch (IOException e) {
			log.error(e.getMessage(), e);
			return disposeException(response, new ExceptionBean(e, -1, e.getMessage()));
		}
	}
	
	/**
	 * 处理异常类型
	 * @param response
	 * @param eb
	 * @return
	 */
	private ResultBean disposeException(HttpResponse response, ExceptionBean eb) {
		ResultBean rb = new JSONResultBean();
		rb.setMsg(eb.getMsg());
		rb.setCode(eb.getCode());
		rb.setData(null);
		return rb;
	}
	
}
