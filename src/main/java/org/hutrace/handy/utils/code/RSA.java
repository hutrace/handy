package org.hutrace.handy.utils.code;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.hutrace.handy.utils.Charset;

public class RSA {
	public static final String KEY_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 1024;
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	public static String encryptByPrivateKey(String content, String key) throws Exception {
		return encryptByPrivateKey(content, key, Charset.DEFAULT);
	}

	public static String encryptByPrivateKey(String content, String key, String charset) throws Exception {
		byte[] byt = encryptByPrivateKey(content.getBytes(charset), Base64.getDecoder().decode(key.getBytes(charset)));
		return new String(Base64.getEncoder().encode(byt), charset);
	}

	public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, privateKey);
		return cipher.doFinal(data);
	}

	public static String encryptByPublicKey(String content, String key) throws Exception {
		return encryptByPublicKey(content, key, Charset.DEFAULT);
	}

	public static String encryptByPublicKey(String content, String key, String charset) throws Exception {
		byte[] byt = encryptByPublicKey(content.getBytes(charset), Base64.getDecoder().decode(key.getBytes(charset)));
		return new String(Base64.getEncoder().encode(byt), charset);
	}

	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(1, pubKey);
		return cipher.doFinal(data);
	}

	public static String decryptByPrivateKey(String content, String key) throws Exception {
		return decryptByPrivateKey(content, key, Charset.DEFAULT);
	}

	public static String decryptByPrivateKey(String content, String key, String charset) throws Exception {
		byte[] byt = decryptByPrivateKey(Base64.getDecoder().decode(content.getBytes(charset)),
				Base64.getDecoder().decode(key.getBytes(charset)));
		return new String(byt, charset);
	}

	public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, privateKey);
		return cipher.doFinal(data);
	}

	public static String decryptByPublicKey(String content, String key) throws Exception {
		return decryptByPublicKey(content, key, Charset.DEFAULT);
	}

	public static String decryptByPublicKey(String content, String key, String charset) throws Exception {
		byte[] byt = decryptByPublicKey(Base64.getDecoder().decode(content.getBytes(charset)),
				Base64.getDecoder().decode(key.getBytes(charset)));
		return new String(byt, charset);
	}

	public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(2, pubKey);
		return cipher.doFinal(data);
	}

	public static String getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return new String(Base64.getEncoder().encode(key.getEncoded()));
	}

	public static String getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(Base64.getEncoder().encode(key.getEncoded()));
	}
	
	public static String getPrivateKey(Map<String, Object> keyMap, String charset) throws UnsupportedEncodingException {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return new String(Base64.getEncoder().encode(key.getEncoded()), charset);
	}

	public static String getPublicKey(Map<String, Object> keyMap, String charset) throws UnsupportedEncodingException {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return new String(Base64.getEncoder().encode(key.getEncoded()), charset);
	}
}