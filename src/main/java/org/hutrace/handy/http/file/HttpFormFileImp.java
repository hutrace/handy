package org.hutrace.handy.http.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import org.hutrace.handy.exception.HttpParseFileException;

import io.netty.buffer.ByteBuf;

public class HttpFormFileImp implements HttpFormFile {
	
	private HttpFormFileHeaders headers;
	private final ByteBuf buf;
	private byte[] cacheRubbish;
	private byte[] rubbish;
	private int rubbishIndex;
	
	public HttpFormFileImp(ByteBuf buf) throws HttpParseFileException {
		this.buf = buf;
		headers = new HttpFormFileHeaders(buf);
		clearRubbish();
	}
	
	private void clearRubbish() {
		initRubbish();
		int a = buf.writerIndex();
		cacheRubbish = new byte[rubbishIndex];
		while(checkRubbish()) {
			if(rubbishIndex == 0) {
				resetCacheRubbish();
			}
			cacheRubbish[--rubbishIndex] = buf.getByte(--a);
		}
		buf.writerIndex(a);
	}
	
	private void resetCacheRubbish() {
		rubbishIndex = 1;
		byte[] b = new byte[cacheRubbish.length];
		System.arraycopy(cacheRubbish, 0, b, 1, b.length - 1);
		cacheRubbish = b;
	}
	
	private void initRubbish() {
		byte[] b = headers.getBoundary().getBytes();
		rubbishIndex = b.length + 2;
		rubbish = new byte[rubbishIndex];
		System.arraycopy(b, 0, rubbish, 0, b.length);
		rubbish[rubbishIndex - 2] = 45;
		rubbish[rubbishIndex - 1] = 45;
	}
	
	private boolean checkRubbish() {
		for(int i = 0; i < cacheRubbish.length; i++) {
			if(cacheRubbish[i] != rubbish[i]) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getHeader(String name) {
		return headers.header(name);
	}
	
	@Override
	public HttpFormFileHeaders getHeaders() {
		return headers;
	}
	
	@Override
	public void bytes(byte[] dis) {
		buf.readBytes(dis);
	}
	
	@Override
	public void bytes(OutputStream out) throws IOException {
		buf.readBytes(out, length());
	}
	
	@Override
	public int length() {
		return buf.readableBytes();
	}
	
	@Override
	public void transferTo(File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		FileChannel channel = out.getChannel();
		channel.write(buf.nioBuffer());
		out.close();
		channel.close();
	}

	@Override
	public String fileName() {
		return headers.getFilename();
	}

}
