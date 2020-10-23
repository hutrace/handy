package org.hutrace.handy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.hutrace.handy.utils.code.TextCoding;

public class IOUtils {
	
	public static String readString(InputStream in) throws IOException {
		return new String(readBytes(in), Charset.DEFAULT);
	}
	
	public static String readString(InputStream in, String charset) throws IOException {
		return new String(readBytes(in), charset);
	}
	
	public static byte[] readBytes(InputStream in) throws IOException {
		byte[] bytes = new byte[1024 * 64];
		int count;
		int offset = 0;
		try {
			for( ; ; ) {
				count = in.read(bytes, offset, bytes.length - offset);
				if(count == -1) {
					break;
				}
				offset += count;
				if(offset == bytes.length) {
					byte[] newBytes = new byte[bytes.length * 3 / 2];
					System.arraycopy(bytes, 0, newBytes, 0, bytes.length);
					bytes = newBytes;
				}
			}
			if(offset != bytes.length) {
				byte[] newBytes = new byte[offset];
				System.arraycopy(bytes, 0, newBytes, 0, offset);
				bytes = newBytes;
			}
			return bytes;
		}catch (IOException e) {
			throw e;
		}finally {
			try {
				in.close();
			}catch (IOException e) {}
		}
	}
	
	/**
	 * 按行读取文件数据，默认使用{@link System#lineSeparator()}作为行分割符
	 * @param in 输入流
	 * @return 字符串
	 * @throws IOException
	 */
	public static String readLine(InputStream in) throws IOException {
		return readLine(in, System.lineSeparator());
	}
	
	/**
	 * 按行读取文件数据，可传入行分割符
	 * @param in 输入流
	 * @param lineSeparator 行分割和
	 * @return 字符串
	 * @throws IOException
	 */
	public static String readLine(InputStream in, String lineSeparator) throws IOException {
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		StringBuilder strb = new StringBuilder();
		String line;
		try {
			if(lineSeparator.length() > 0) {
				while((line = br.readLine()) != null) {
					strb.append(line);
					strb.append(lineSeparator);
				}
			}else {
				while((line = br.readLine()) != null) {
					strb.append(line);
				}
			}
			return strb.toString();
		}catch (IOException e) {
			throw e;
		}finally {
			br.close();
		}
	}
	
	/**
	 * <p>将文件的内容读取成List
	 * <p>使用{@link BufferedReader#readLine()}进行读取，每一行为List的一条数据
	 * <p>当行数据是""(空字符串)时,不会将此行数据添加进入List
	 * <p>当行数据去掉两边空格后是""(空字符串)时,不会将此行数据添加进入List
	 * @param in 输入流
	 * @return List
	 * @throws IOException
	 */
	public static List<String> readLineToList(InputStream in) throws IOException {
		return readLineToList(in, false, false);
	}
	
	/**
	 * <p>将文件的内容读取成List
	 * <p>使用{@link BufferedReader#readLine()}进行读取，每一行为List的一条数据
	 * <p>当行数据是""(空字符串)时,不会将此行数据添加进入List
	 * <p>根据trimEmptyStringAdd的值判断一行的内容经过{@link String#trim()}后是否需要添加到List
	 * <p>当trimEmptyStringAdd为false时，emptyStringAdd必定为false
	 * @param in 输入流
	 * @param trimEmptyStringAdd 当行数据去掉两边空格后是""(空字符串)时是否需要添加进入List，当此值为false时，emptyStringAdd比定是false。
	 * @return List
	 * @throws IOException
	 */
	public static List<String> readLineToList(InputStream in, boolean trimEmptyStringAdd) throws IOException {
		return readLineToList(in, false, trimEmptyStringAdd);
	}
	
	/**
	 * <p>将文件的内容读取成List
	 * <p>使用{@link BufferedReader#readLine()}进行读取，每一行为List的一条数据
	 * <p>根据emptyStringAdd的值判断一行的空内容是否需要添加到List
	 * <p>根据trimEmptyStringAdd的值判断一行的内容经过{@link String#trim()}后是否需要添加到List
	 * <p>当trimEmptyStringAdd为false时，emptyStringAdd必定为false
	 * <p>默认trimEmptyStringAdd和emptyStringAdd都为false
	 * @param in 输入流
	 * @param emptyStringAdd 当行数据是""(空字符串)时是否需要添加进入List
	 * @param trimEmptyStringAdd 当行数据去掉两边空格后是""(空字符串)时是否需要添加进入List，当此值为false时，emptyStringAdd比定是false。
	 * @return List
	 * @throws IOException
	 */
	public static List<String> readLineToList(InputStream in, boolean emptyStringAdd, boolean trimEmptyStringAdd) throws IOException {
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		List<String> list = new ArrayList<>();
		String line;
		try {
			if(trimEmptyStringAdd && emptyStringAdd) {
				while((line = br.readLine()) != null) {
					list.add(line);
				}
			}else {
				if(!trimEmptyStringAdd) {
					while((line = br.readLine()) != null) {
						if(!TextCoding.EMPTY_STR.equals(line.trim())) {
							list.add(line);
						}
					}
				}else {
					while((line = br.readLine()) != null) {
						if(!TextCoding.EMPTY_STR.equals(line)) {
							list.add(line);
						}
					}
				}
			}
			return list;
		}catch (IOException e) {
			throw e;
		}finally {
			br.close();
		}
	}
	
	/**
	 * 按行读取数据，自定义监听器
	 * @param in 输入流
	 * @param listener 监听器
	 * @throws IOException
	 */
	public static void readLine(InputStream in, ReadLineListener listener) throws IOException {
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		String line;
		try {
			while((line = br.readLine()) != null) {
				listener.read(line);
			}
		}catch (IOException e) {
			throw e;
		}finally {
			br.close();
		}
	}
	
	/**
	 * <p>按行读取数据的监听器
	 * @author HuTrace
	 * @since 1.8
	 * @version 1.0
	 */
	public static interface ReadLineListener {
		void read(String line) throws IOException;
	}
	
}
