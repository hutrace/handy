package org.hutrace.handy.utils.code;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.hutrace.handy.utils.Charset;

public class SHA {
	
	public static String sha1(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return sha1(input, Charset.DEFAULT);
	}
	
	public static String sha1(String input, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(input.getBytes(charset));
		StringBuilder strb = new StringBuilder();
		for(int i = 0; i < result.length; i++) {
			strb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return strb.toString();
	}

	public static String sha256(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		return sha256(input, Charset.DEFAULT);
	}
	
	public static String sha256(String str, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(str.getBytes(charset));
		StringBuffer stringBuffer = new StringBuffer();
		String temp = null;
		byte[] bytes = messageDigest.digest();
		for (int i = 0; i < bytes.length; ++i) {
			temp = Integer.toHexString(bytes[i] & 255);
			if (temp.length() == 1) {
				stringBuffer.append("0");
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString();
	}

}
