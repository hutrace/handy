package org.hutrace.handy.http;

import static io.netty.util.internal.ObjectUtil.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

import org.hutrace.handy.config.Configuration;
import org.hutrace.handy.exception.HttpResponseCloseException;
import org.hutrace.handy.exception.HttpResponseFlushException;
import org.hutrace.handy.http.file.HttpFileDownloadCompleteListener;
import org.hutrace.handy.http.file.HttpFileDownloadListener;
import org.hutrace.handy.http.listener.ChannelListener;
import org.hutrace.handy.http.result.ResultBean;
import org.hutrace.handy.language.SystemProperty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.handler.codec.http.CombinedHttpHeaders;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.IllegalReferenceCountException;
import io.netty.util.internal.StringUtil;

/**
 * <p>
 * HttpResponse响应类，通过此类可以向客户端响应数据
 * 
 * @author hutrace
 * @since 1.8
 * @version 1.0
 * @time 2019年4月15日
 */
public class HttpServerResponse extends DefaultHttpResponse implements HttpResponse {

	private ByteBuf content;
	private final HttpHeaders trailingHeaders;

	private int hash;
	
	private ChannelHandlerContext context;
	
	private boolean flush;
	
	private boolean close;
	
	private HttpRequest request;
	
	private Object result;
	
	private Object parameter;
	
	private String id;
	
	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, ChannelHandlerContext context) {
		this(version, status, Unpooled.buffer(0), context);
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, ChannelHandlerContext context) {
		this(version, status, content, true, context);
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status,
			boolean validateHeaders, ChannelHandlerContext context) {
		this(version, status, Unpooled.buffer(0), validateHeaders, false, context);
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, boolean validateHeaders,
			boolean singleFieldHeaders, ChannelHandlerContext context) {
		this(version, status, Unpooled.buffer(0), validateHeaders, singleFieldHeaders, context);
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content,
			boolean validateHeaders, ChannelHandlerContext context) {
		this(version, status, content, validateHeaders, false, context);
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, boolean validateHeaders,
			boolean singleFieldHeaders, ChannelHandlerContext context) {
		super(version, status, validateHeaders, singleFieldHeaders);
		this.content = checkNotNull(content, "content");
		this.trailingHeaders = singleFieldHeaders ? new CombinedHttpHeaders(validateHeaders)
				: new DefaultHttpHeaders(validateHeaders);
		this.context = context;
		flush = false;
		close = false;
	}

	public HttpServerResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content, HttpHeaders headers,
			HttpHeaders trailingHeaders, ChannelHandlerContext context) {
		super(version, status, headers);
		this.content = checkNotNull(content, "content");
		this.trailingHeaders = checkNotNull(trailingHeaders, "trailingHeaders");
		this.context = context;
		flush = false;
		close = false;
	}
	
	public void setChannel(ChannelHandlerContext context) {
		this.context = context;
	}
	
	public void setRequest(HttpRequest request) {
		this.request = request;
	}
	
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}

	@Override
	public HttpHeaders trailingHeaders() {
		return trailingHeaders;
	}

	@Override
	public ByteBuf content() {
		return content;
	}

	@Override
	public int refCnt() {
		return content.refCnt();
	}

	@Override
	public HttpResponse retain() {
		content.retain();
		return this;
	}

	@Override
	public HttpResponse retain(int increment) {
		content.retain(increment);
		return this;
	}

	@Override
	public HttpResponse touch() {
		content.touch();
		return this;
	}

	@Override
	public HttpResponse touch(Object hint) {
		content.touch(hint);
		return this;
	}

	@Override
	public boolean release() {
		return content.release();
	}

	@Override
	public boolean release(int decrement) {
		return content.release(decrement);
	}

	@Override
	public HttpResponse setProtocolVersion(HttpVersion version) {
		super.setProtocolVersion(version);
		return this;
	}

	@Override
	public HttpResponse setStatus(HttpResponseStatus status) {
		super.setStatus(status);
		return this;
	}

	@Override
	public HttpResponse copy() {
		return replace(content().copy());
	}

	@Override
	public HttpResponse duplicate() {
		return replace(content().duplicate());
	}

	@Override
	public HttpResponse retainedDuplicate() {
		return replace(content().retainedDuplicate());
	}

	@Override
	public HttpResponse replace(ByteBuf content) {
		HttpResponse response = new HttpServerResponse(protocolVersion(), status(), content, headers().copy(),
				trailingHeaders().copy(), context);
		response.setDecoderResult(decoderResult());
		return response;
	}

	@Override
	public int hashCode() {
		int hash = this.hash;
		if (hash == 0) {
			if (content().refCnt() != 0) {
				try {
					hash = 31 + content().hashCode();
				}catch (IllegalReferenceCountException ignored) {
					// Handle race condition between checking refCnt() == 0 and using the object.
					hash = 31;
				}
			} else {
				hash = 31;
			}
			hash = 31 * hash + trailingHeaders().hashCode();
			hash = 31 * hash + super.hashCode();
			this.hash = hash;
		}
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof DefaultFullHttpResponse)) {
			return false;
		}
		DefaultFullHttpResponse other = (DefaultFullHttpResponse) o;
		return super.equals(other) && content().equals(other.content())
				&& trailingHeaders().equals(other.trailingHeaders());
	}

	@Override
	public String toString() {
		return appendFullResponse(new StringBuilder(256), this).toString();
	}
	
	private StringBuilder appendFullResponse(StringBuilder buf, HttpServerResponse res) {
		appendFullCommon(buf, res);
		appendInitialLine(buf, res);
		appendHeaders(buf, res.headers());
		appendHeaders(buf, res.trailingHeaders());
		removeLastNewLine(buf);
		return buf;
	}

	private void appendFullCommon(StringBuilder buf, HttpServerResponse msg) {
		buf.append(StringUtil.simpleClassName(msg));
		buf.append("(decodeResult: ");
		buf.append(msg.decoderResult());
		buf.append(", version: ");
		buf.append(msg.protocolVersion());
		buf.append(", content: ");
		buf.append(msg.content());
		buf.append(')');
		buf.append(StringUtil.NEWLINE);
	}

	private void appendInitialLine(StringBuilder buf, HttpResponse res) {
		buf.append(res.protocolVersion());
		buf.append(' ');
		buf.append(res.status());
		buf.append(StringUtil.NEWLINE);
	}

	private void appendHeaders(StringBuilder buf, HttpHeaders headers) {
		for(Map.Entry<String, String> e : headers) {
			buf.append(e.getKey());
			buf.append(": ");
			buf.append(e.getValue());
			buf.append(StringUtil.NEWLINE);
		}
	}

	private void removeLastNewLine(StringBuilder buf) {
		buf.setLength(buf.length() - StringUtil.NEWLINE.length());
	}

	@Override
	public void write(byte[] bytes) {
		checkFlushOrClose();
		setHeader(HttpHeaderNames.CONTENT_LENGTH, bytes.length);
		content = Unpooled.wrappedBuffer(bytes);
		context.write(this);
	}

	@Override
	public void write(String data) {
		result = data;
		try {
			write(data.getBytes(Configuration.charset()));
		}catch (UnsupportedEncodingException e) {
			write(data.getBytes());
		}
	}
	
	public void write(ResultBean data) {
		result = data;
		try {
			write(data.getString().getBytes(Configuration.charset()));
		}catch (UnsupportedEncodingException e) {
			write(data.getString().getBytes());
		}
	}

	@Override
	public void writeAndFlush(File file) throws IOException {
		writeAndFlush(file, new HttpFileDownloadCompleteListener() {
			@Override
			public void complete(ChannelProgressiveFuture future) {
				close = false;
				close();
			}
		});
	}

	@Override
	public void writeAndFlush(File file, HttpFileDownloadCompleteListener completeListener) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		writeAndFlush(raf, new HttpFileDownloadListener(raf, completeListener));
	}

	@Override
	public void writeAndFlush(RandomAccessFile file, ChannelProgressiveFutureListener futureListener)
			throws IOException {
		checkFlushOrClose();
		flush = true;
		close = true;
		DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		long len = file.length();
		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, len);
		response.headers().set(this.headers());
		context.write(response);
		ChannelFuture future = context.write(new DefaultFileRegion(file.getChannel(), 0, len),
				context.newProgressivePromise());
		future.addListener(futureListener);
		context.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
	}
	
	@Override
	public void flush() {
		if(!flush) {
			flush = true;
			context.flush();
		}
	}

	@Override
	public void close() {
		flush();
		if(!close) {
			close = true;
			context.close().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					future.channel().close();
					if(Configuration.listeners() != null && Configuration.listeners().size() > 0) {
						for(ChannelListener listener : Configuration.listeners()) {
							listener.closed(request, HttpServerResponse.this, parameter, result);
						}
					}
				}
			});
		}
	}

	@Override
	public void setHeader(CharSequence name, Object value) {
		checkFlushOrClose();
		headers().set(name, value);
	}

	@Override
	public String header(CharSequence name) {
		checkFlushOrClose();
		return headers().get(name);
	}

	@Override
	public void removeHeader(CharSequence name) {
		headers().remove(name);
	}

	@Override
	public void setContentType(String contentType) {
		checkFlushOrClose();
		headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
	}
	
	public boolean isFlush() {
		return flush;
	}
	
	public boolean isClose() {
		return close;
	}
	
	private void checkFlushOrClose() {
		if(flush) {
			throw new HttpResponseFlushException(SystemProperty.get("serve.response.flushed"));
		}
		if(close) {
			throw new HttpResponseCloseException(SystemProperty.get("serve.response.closed"));
		}
	}
	
	public boolean canWrite() {
		return !isFlush() && !isClose();
	}
	
	public void errorResponse(HttpResponseStatus status) {
		flush = true;
		close = true;
		FullHttpResponse rsp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		context.writeAndFlush(rsp).addListener(ChannelFutureListener.CLOSE);
		context.close();
	}
	
	@Override
	public String id() {
		if(id == null) {
			id = UUID.randomUUID().toString().replace("-", "");
		}
		return id;
	}
	
}