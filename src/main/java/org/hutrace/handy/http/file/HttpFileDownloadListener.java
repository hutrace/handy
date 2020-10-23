package org.hutrace.handy.http.file;

import java.io.RandomAccessFile;

import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;

/**
 * 默认的文件下载监听器
 * @author hu trace
 *
 */
public class HttpFileDownloadListener implements ChannelProgressiveFutureListener {
	
	private RandomAccessFile raf;
	
	private HttpFileDownloadCompleteListener complateListener;
	
	public HttpFileDownloadListener(RandomAccessFile raf, HttpFileDownloadCompleteListener complateListener) {
		this.raf = raf;
		this.complateListener = complateListener;
	}
	
	@Override
	public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
		
	}

	@Override
	public void operationComplete(ChannelProgressiveFuture future) throws Exception {
		raf.close();
		complateListener.complete(future);
	}
	
}