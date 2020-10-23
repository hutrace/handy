package org.hutrace.handy.utils.code;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.hutrace.handy.utils.Charset;

/**
 * <p>MD5工具类
 * @author HuTrace
 * @since 1.8
 * @version 1.0
 * @time 2020年2月25日
 */
public class MD5 {
	
	private static final String[] LOWER = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
	private static final String[] UPPER = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	
	/**
	 * 将字符串进行md5摘要，返回值问小写md5
	 * <p>将字符串通过{@link Charset#DEFAULT}编码为字节，进行摘要
	 * @param str 需要摘要的字符串
	 * @return md5字符串
	 */
	public static String lowerCase(String str) {
		return lowerCase(str, Charset.DEFAULT);
	}

	/**
	 * 将字符串进行md5摘要，返回值问小写md5
	 * <p>将字符串通过charset编码为字节，进行摘要
	 * @param str 需要摘要的字符串
	 * @param charset 字符串编码格式
	 * @return md5字符串
	 */
	public static String lowerCase(String str, String charset) {
		try {
			return lowerCase(str.getBytes(charset));
		}catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将字节进行md5摘要，返回值问小写md5
	 * @param bytes 字节
	 * @return md5字符串
	 */
	public static String lowerCase(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(bytes);
			StringBuilder sb = new StringBuilder(32);
			// 将结果转为16进制字符 0~9 A~F
			for (int i = 0; i < result.length; i++) {
				// 一个字节对应两个字符
				byte x = result[i];
				// 取得高位
				int h = 0x0f & (x >>> 4);
				// 取得低位
				int l = 0x0f & x;
				sb.append(LOWER[h]).append(LOWER[l]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将字符串进行md5摘要，返回值问大写md5
	 * <p>将字符串通过{@link Charset#DEFAULT}编码为字节，进行摘要
	 * @param str 需要摘要的字符串
	 * @return md5字符串
	 */
	public static String upperCase(String str) {
		return upperCase(str, Charset.DEFAULT);
	}

	/**
	 * 将字符串进行md5摘要，返回值问大写md5
	 * <p>将字符串通过charset编码为字节，进行摘要
	 * @param str 需要摘要的字符串
	 * @param charset 字符串编码格式
	 * @return md5字符串
	 */
	public static String upperCase(String str, String charset) {
		try {
			return upperCase(str.getBytes(charset));
		}catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * 将字节进行md5摘要，返回值问大写md5
	 * @param bytes 字节
	 * @return md5字符串
	 */
	public static String upperCase(byte[] bytes) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(bytes);
			StringBuilder sb = new StringBuilder(32);
			// 将结果转为16进制字符 0~9 A~F
			for (int i = 0; i < result.length; i++) {
				// 一个字节对应两个字符
				byte x = result[i];
				// 取得高位
				int h = 0x0f & (x >>> 4);
				// 取得低位
				int l = 0x0f & x;
				sb.append(UPPER[h]).append(UPPER[l]);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
