package org.hutrace.handy.http.file;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface HttpFile {
	
	/**
	 * <p>获取当前内容的长度
	 * @return 当前内容的长度
	 */
	int length();
	
	/**
	 * <p>传入字节数组
	 * <p>该方法需要将内容填充值此数组
	 * @param dis 空内容数组
	 */
	void bytes(byte[] dis);
	
	/**
	 * <p>获取file输出流
	 * @return
	 */
	void bytes(OutputStream out) throws IOException;
	
	/**
	 * <p>将内容转换成文件
	 * @param file 目标文件
	 * @throws IOException
	 */
	void transferTo(File file) throws IOException;
	
	/**
	 * <p>获取文件名称
	 * @return String 文件名称
	 */
	String fileName();
	
}
