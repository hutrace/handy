package org.hutrace.handy.http;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.hutrace.handy.http.file.HttpFileDownloadCompleteListener;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.server.ServerResponse;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public interface HttpResponse extends FullHttpResponse, ServerResponse {
	
	/**
	 * 当前单次会话请求id
	 * <p>建议实现者使用uuid实现
	 * <p>此id需要保持唯一性
	 * @return
	 */
	@Override
	String id();
	
	@Override
	HttpResponse copy();

	@Override
	HttpResponse duplicate();

	@Override
	HttpResponse retainedDuplicate();

	@Override
	HttpResponse replace(ByteBuf content);

	@Override
	HttpResponse retain(int increment);

	@Override
	HttpResponse retain();

	@Override
	HttpResponse touch();

	@Override
	HttpResponse touch(Object hint);

	@Override
	HttpResponse setProtocolVersion(HttpVersion version);

	@Override
	HttpResponse setStatus(HttpResponseStatus status);
	
	/**
	 * 向响应通道写入数据
	 * @param bytes
	 */
	@Override
	void write(byte[] bytes);
	
	/**
	 * 向响应通道写入数据
	 * @param data
	 */
	void write(String data);
	
	/**
	 * 向响应通道写入数据
	 * @param data
	 */
	void write(ResultBean data);
	
	/**
	 * 向响应通道写入数据，并刷新所有挂起的消息。
	 * <p>此方法用于下载文件
	 * <p>应创建默认下载完成监听器调用{@link #writeAndFlush(File, HttpFileDownloadCompleteListener)}
	 * <p>使用该方法需要注意，此方法不需要手动调用{@link #close()}方法，已经默认处理，手动调用{@link #close()}可能会导致文件下载失败。
	 * @param file
	 * @throws IOException
	 */
	void writeAndFlush(File file) throws IOException;

	/**
	 * 向响应通道写入数据，并刷新所有挂起的消息。
	 * <p>此方法用于下载文件
	 * <p>应创建默认下载完成监听器调用{@link #writeAndFlush(File, ChannelProgressiveFutureListener)}
	 * <p>使用该方法需要注意，一定需要在监听器里面手动调用{@link #close()}关闭通道
	 * @param file
	 * @param completeListener 文件下载完成监听器，使用该方法需要注意，一定需要在监听器里面手动调用{@link #close()}关闭通道
	 * @throws IOException
	 */
	void writeAndFlush(File file, HttpFileDownloadCompleteListener completeListener) throws IOException;
	
	/**
	 * 向响应通道写入数据，并刷新所有挂起的消息。
	 * <p>此方法用于下载文件
	 * <p>使用该方法需要注意
	 * <p>一定需要在监听器里面手动调用{@link #close()}关闭通道
	 * <p>一定需要在监听器里面手动{@link RandomAccessFile#close()}关闭文件通道
	 * @param file
	 * @param futureListener
	 * @throws IOException
	 */
	void writeAndFlush(RandomAccessFile file, ChannelProgressiveFutureListener futureListener) throws IOException;
	
	/**
	 * 刷新所有挂起的消息
	 */
	@Override
	void flush();
	
	/**
	 * 关闭通道
	 */
	@Override
	void close();
	
	/**
	 * 设置响应消息头
	 * @param name
	 * @param value
	 */
	@Override
	void setHeader(CharSequence name, Object value);
	
	/**
	 * 获取响应消息头
	 * @param name
	 * @return
	 */
	@Override
	String header(CharSequence name);
	
	/**
	 * 删除响应消息头
	 * @param name
	 */
	@Override
	void removeHeader(CharSequence name);
	
	/**
	 * 设置响应消息头的content-type
	 * @param contentType
	 */
	void setContentType(String contentType);
	
}
