package org.hutrace.handy.utils.code;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.hutrace.handy.utils.Charset;

/**
 * <p>AES加解密工具类
 * @author HuTrace
 * @since 1.8
 * @version 1.0
 */
public class AES {
	
	/**
	 * 将字符串加密，使用{@link Charset#DEFALUT}编码
	 * @param secret 密钥
	 * @param content 字符串
	 * @return 加密后的字符串
	 * @throws Exception 
	 */
	public static String encode(String secret, String content) throws Exception {
		return encode(secret, content, Charset.DEFAULT);
	}

	/**
	 * 将字符串解密，使用{@link Charset#DEFALUT}编码
	 * @param secret 密钥
	 * @param content 字符串
	 * @return 解密后的字符串
	 * @throws Exception 
	 */
	public static String dncode(String secret, String content) throws Exception {
		return dncode(secret, content, Charset.DEFAULT);
	}
	
	/**
	 * 将字符串加密，使用charset编码
	 * @param secret 密钥
	 * @param content 字符串
	 * @param charset 字符串编码
	 * @return 加密后的字符串
	 * @throws Exception 
	 */
	public static String encode(String secret, String content, String charset) throws Exception {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			keygen.init(128);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(1, new SecretKeySpec(secret.getBytes(), "AES"));
			byte[] byteAes = cipher.doFinal(content.getBytes(charset));
			return new String(Base64.getEncoder().encode(byteAes), charset);
		}catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 将字符串解密，使用charset编码
	 * @param secret 密钥
	 * @param content 字符串
	 * @param charset 字符串编码
	 * @return 解密后的字符串
	 * @throws Exception 
	 */
	public static String dncode(String secret, String content, String charset) throws Exception {
		try {
			KeyGenerator keygen = KeyGenerator.getInstance("AES");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secret.getBytes());
			keygen.init(128, secureRandom);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(2, new SecretKeySpec(secret.getBytes(), "AES"));
			byte[] byteDecode = cipher.doFinal(Base64.getDecoder().decode(content));
			return new String(byteDecode, charset);
		}catch (Exception e) {
			throw e;
		}
	}
}