package org.hutrace.handy.http.file;

import java.io.File;

public class HttpDownload {
	
	private File file;
	
	private String contentType;
	
	private String disposition;
	
	public HttpDownload(File file) {
		this(file, "application/octet-stream", file.getName());
	}
	
	public HttpDownload(File file, String name) {
		this(file, "application/octet-stream", name);
	}
	
	public HttpDownload(File file, String contentType, String name) {
		this.file = file;
		this.contentType = contentType;
		this.disposition = "attachment; filename=\"" + name + "\"";
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}
	
}
