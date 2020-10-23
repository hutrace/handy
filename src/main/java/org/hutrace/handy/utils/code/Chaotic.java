package org.hutrace.handy.utils.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>混乱的加解密方式
 * @author hu trace
 * @since 1.8
 * @version 1.0
 * @time 2019年7月26日
 */
public class Chaotic {
	
	/** 字符转换因子 */
	private static final String CHAR_TRANSITION = "ZXCVBNMASDFGHJKLqwertyuiop012345";
	/** 16进制字符 */
	private static final String HEXADECIMAL = "0123456789abcdef";
	/** 分割字符, 每个字符之间随机取下面一个字符进行拼接 */
	private static final String SPLIT = "QWERTYUIOPasdfghjklzxcvbnm6789";
	/** 分割字符正则表达式 */
	private static final String REG_SPLIT = "Q|W|E|R|T|Y|U|I|O|P|a|s|d|f|g|h|j|k|l|z|x|c|v|b|n|m|6|7|8|9";
	/** 复杂度，值越大越复杂(整数) */
	private static final int DEFAULT_COMPLEXITY = 0;
	private static final String CHARSET = "UTF-8";
	
	private static String strToUnicode(String str) {
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			strb.append(transitionForChar(Integer.toHexString((int) str.charAt(i))));
			strb.append(randomSplit());
		}
		return strb.toString();
	}
	
	private static String unicodeToStr(String str) {
		str = str.replaceAll(REG_SPLIT, ",");
		String[] arr = str.split(",");
		String cha = "";
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < arr.length; i ++) {
			cha = restoreForChar(arr[i]);
			strb.append((char) Integer.parseInt(cha, 16));
		}
		return strb.toString();
	}
	
	private static String transitionForChar(String cha) {
		int index, random;
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < cha.length(); i++) {
			index = HEXADECIMAL.indexOf(cha.charAt(i));
			if(index > -1) {
				random = (int) (Math.floor(Math.random() * 10 + 1) % 2);
				strb.append(CHAR_TRANSITION.charAt(index + (random * 16)));
			}
		}
		return strb.toString();
	}
	
	private static String restoreSortStr(String str) {
		int splitIndex = str.length()/2;
		String str1 = str.substring(0, splitIndex);
		String str2 = str.substring(splitIndex);
		StringBuilder strb = new StringBuilder();
		for(int i = str1.length() - 1; i >= 0; i --) {
			strb.append(str1.charAt(i));
			strb.append(str2.charAt(i));
		}
		return strb.toString();
	}
	
	private static String restoreForChar(String cha) {
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < cha.length(); i ++) {
			strb.append(HEXADECIMAL.charAt(CHAR_TRANSITION.indexOf(cha.charAt(i)) % 16));
		}
		return strb.toString();
	}
	
	private static char randomSplit() {
		return SPLIT.charAt((int) Math.floor(Math.random() * 30));
	}
	
	private static String sortStr(String str) {
		StringBuilder odd = new StringBuilder();//基数位
		StringBuilder even = new StringBuilder();//偶数位
		for(int i = str.length() - 1; i >= 0; i --) {
			if(i%2 == 0) {
				odd.append(str.charAt(i));
			}else {
				even.append(str.charAt(i));
			}
		}
		return odd.toString() + even.toString();
	}
	
	/**
	 * <p>加密
	 * @param str
	 * @return
	 * @throws IOException 
	 */
	public static String encode(File file) throws IOException {
		InputStream in = new FileInputStream(file);
		byte[] bytes;
		try {
			bytes = new byte[in.available()];
			in.read(bytes);
		}catch (IOException e) {
			throw e;
		}finally {
			in.close();
		}
		return encode(new String(bytes, CHARSET));
	}
	
	/**
	 * <p>加密
	 * @param str
	 * @return
	 */
	public static String encode(String str) {
		String unicode = strToUnicode(str);
		for(int i = 0; i < DEFAULT_COMPLEXITY; i ++) {
			unicode = sortStr(unicode);
		}
		return unicode;
	}
	/**
	 * <p>解密
	 * @param str
	 * @return
	 */
	public static String decode(String str) {
		for(int i = 0; i < DEFAULT_COMPLEXITY; i ++) {
			str = restoreSortStr(str);
		}
		return unicodeToStr(str);
	}
	
}
