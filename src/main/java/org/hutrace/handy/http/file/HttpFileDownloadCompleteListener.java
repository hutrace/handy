package org.hutrace.handy.http.file;

import io.netty.channel.ChannelProgressiveFuture;

/**
 * 文件下载（传输）完成的监听器
 * @author hu trace
 *
 */
public interface HttpFileDownloadCompleteListener {
	
	/**
	 * 完成回调方法
	 * @param future
	 */
	void complete(ChannelProgressiveFuture future);
	
}
