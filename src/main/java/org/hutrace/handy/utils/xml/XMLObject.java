package org.hutrace.handy.utils.xml;

import java.io.InputStream;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.hutrace.handy.exception.ResolverException;
import org.hutrace.handy.http.validate.ValidateJSONObject;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;

/**
 * <p>XML对象工具类
 * <p>此类中包含将XML的各种数据源解析为{@link JSONObject}以及{@link ValidateJSONObject}
 * <p>因为{@link JSONObject}中包含了解析成JavaBean的方法, 故在此处没有进一步封装类似的方法
 * <p>此方法是对{@link XMLSerializer}的进一步封装
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @see XMLSerializer
 * @see JSONObject
 * @see ValidateJSONObject
 * @since 1.8
 * @version 1.0
 * @time 2019年6月29日
 */
public class XMLObject {
	
	public static final XMLSerializer xml = new XMLSerializer();
	
	/**
	 * <p>根据XML字符串{@link String}解析成{@link JSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(String)}
	 * @param text {@link String}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parse(String text) throws ResolverException {
		try {
			return toJSON(xml.parse(text));
		}catch (DocumentException e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据XML字节{@link byte[]}解析成{@link JSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(byte[])}
	 * @param bytes {@link byte[]}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parse(byte[] bytes) throws ResolverException {
		try {
			return toJSON(xml.parse(bytes));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据netty缓冲流{@link ByteBuf}解析成{@link JSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(ByteBuf)}
	 * @param buf {@link ByteBuf}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parse(ByteBuf buf) throws ResolverException {
		try {
			return toJSON(xml.parse(buf));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据输入流{@link InputStream}解析成{@link JSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(InputStream)}
	 * @param in {@link InputStream}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parse(InputStream in) throws ResolverException {
		try {
			return toJSON(xml.parse(in));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据字符串{@link Document}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(Document)}
	 * @param doc {@link Document}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 */
	public static JSONObject parse(Document doc) {
		return toJSON(xml.parse(doc));
	}
	
	/**
	 * <p>根据字符串{@link Element}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(Element)}
	 * @param elem {@link Element}
	 * @see XMLSerializer
	 * @return {@link JSONObject}
	 */
	public static JSONObject parse(Element elem) {
		return toJSON(xml.parse(elem));
	}
	
	
	/**
	 * <p>根据XML字符串{@link String}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(String)}
	 * @param text {@link String}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parseVerify(String text) throws ResolverException {
		try {
			return toValidateJSON(xml.parse(text));
		}catch (DocumentException e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据XML字节{@link byte[]}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(byte[])}
	 * @param bytes {@link byte[]}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parseVerify(byte[] bytes) throws ResolverException {
		try {
			return toValidateJSON(xml.parse(bytes));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据netty缓冲流{@link ByteBuf}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(ByteBuf)}
	 * @param buf {@link ByteBuf}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parseVerify(ByteBuf buf) throws ResolverException {
		try {
			return toValidateJSON(xml.parse(buf));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据输入流{@link InputStream}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(InputStream)}
	 * @param in {@link InputStream}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 * @throws ResolverException 当解析数据错误时抛出
	 */
	public static JSONObject parseVerify(InputStream in) throws ResolverException {
		try {
			return toValidateJSON(xml.parse(in));
		}catch (Exception e) {
			throw new ResolverException("Xml data parsing failed", e);
		}
	}
	
	/**
	 * <p>根据字符串{@link Document}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(Document)}
	 * @param doc {@link Document}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 */
	public static JSONObject parseVerify(Document doc) {
		return toValidateJSON(xml.parse(doc));
	}
	
	/**
	 * <p>根据字符串{@link Element}解析成{@link ValidateJSONObject}数据
	 * <p>调用{@link XMLSerializer#parse(Element)}
	 * @param elem {@link Element}
	 * @see XMLSerializer
	 * @return {@link ValidateJSONObject}
	 */
	public static JSONObject parseVerify(Element elem) {
		return toValidateJSON(xml.parse(elem));
	}
	
	
	/**
	 * <p>将{@link Map}转换成{@link ValidateJSONObject}, 以便进行参数验证
	 * @param map {@link Map}
	 * @return {@link ValidateJSONObject}
	 */
	public static JSONObject toValidateJSON(Map<String, Object> map) {
		return new ValidateJSONObject(map);
	}

	/**
	 * <p>将{@link Map}转换成{@link JSONObject}
	 * @param map {@link Map}
	 * @return {@link JSONObject}
	 */
	public static JSONObject toJSON(Map<String, Object> map) {
		return new JSONObject(map);
	}
	
}
