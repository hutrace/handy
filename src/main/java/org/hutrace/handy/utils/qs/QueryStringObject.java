package org.hutrace.handy.utils.qs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hutrace.handy.utils.Charset;

import java.util.Set;

/**
 * <p>QueryString对象类, 包含常用的方法
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class QueryStringObject extends QueryString {
	
	/**
	 * 在使用{@link #toQueryString()}时，是否需要对{@link #data}的key进行排序
	 * @see #toQueryString()
	 * @see ParseConfig#KEY_SORT
	 */
	private boolean keySort;
	
	/**
	 * 在使用{@link #toQueryString()}时，是否需要对{@link #data}的value进行URLEncoder编码
	 * @see #toQueryString()
	 * @see ParseConfig#VALUE_ENCODE
	 */
	private boolean valueEncode = true;
	
	/**
	 * 将qs字符串解析成对象时，是否对value值进行URLDecoder解码
	 */
	private boolean valueDecode = true;
	
	/**
	 * <p>在添加数据时，当value为null时，是否需要将其转换为空字符串
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see ParseConfig#NULL_VALUE_TOBLANK
	 * @see #QueryStringObject(Map, ParseConfig[])
	 * @see #putAll(Map)
	 * @see #puts(Map)
	 * @see #put(String, Object)
	 * @see #put(String, String)
	 */
	private boolean nullValueToBlank;
	
	/**
	 * <p>在添加数据时，当value为null时，是否需要将其添加至{@link #data}
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see ParseConfig#NULL_VALUE_APPEND
	 * @see #QueryStringObject(Map, ParseConfig[])
	 * @see #putAll(Map)
	 * @see #puts(Map)
	 * @see #put(String, Object)
	 * @see #put(String, String)
	 */
	private boolean nullValueAppend;
	
	/**
	 * <p>在添加数据时，当value经过{@link String#trim()}处理后为空字符串时，是否需要将其添加至{@link #data}
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see ParseConfig#BLANK_VALUE_APPEND
	 * @see #QueryStringObject(Map, ParseConfig[])
	 * @see #putAll(Map)
	 * @see #puts(Map)
	 * @see #put(String, Object)
	 * @see #put(String, String)
	 */
	private boolean blankValueAppend;
	
	/**
	 * <p>在添加数据时，是否需要对value做{@link String#trim()}处理
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see ParseConfig#BLANK_VALUE_APPEND
	 * @see #QueryStringObject(Map, ParseConfig[])
	 * @see #putAll(Map)
	 * @see #puts(Map)
	 * @see #put(String, Object)
	 * @see #put(String, String)
	 */
	private boolean trimValue;
	
	/**
	 * <p>在使用{@link #toQueryString()}时，需要对{@link #data}的value进行URLEncoder编码时，使用的编码格式
	 * <p>默认是{@link Charset#DEFAULT}
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see #toQueryString()
	 * @see #appendEncodeQS(StringBuilder, String, String)
	 * @see #appendQS(StringBuilder, String, String)
	 */
	private String charset = Charset.DEFAULT;
	
	/**
	 * <p>排序规则
	 * <p>在使用{@link #toQueryString()}时，需要对{@link #data}的key进行排序时，使用的排序规则
	 * <p>如果不指定，则默认按照ASCII编码升序
	 * <p>这会影响后面的生成qs字符串的结果
	 * @see #setConfig(String, Comparator)
	 * @see #toSortString()
	 */
	private Comparator<? super String> sort;
	
	/**
	 * 当前对象的数据储存变量，所有的内容都放在此变量中进行储存。
	 */
	private Map<String, String> data;
	
	/**
	 * <p>构造方法
	 * <p>无参构造
	 * <p>初始化缓存空间
	 */
	public QueryStringObject() {
		this.data = new HashMap<>();
	}
	
	/**
	 * <p>构造方法
	 * <p>指定初始化缓存空间的大小
	 * @param size 缓存空间的大小
	 */
	public QueryStringObject(int size) {
		this.data = new HashMap<>(size);
	}
	
	/**
	 * <p>构造方法
	 * <p>指定初始化缓存空间的大小
	 * <p>可通过config设置qs的解析规则以及储存数据规则，详情见{@link ParseConfig}
	 * @param size 缓存空间的大小
	 * @param config 解析配置
	 * @see ParseConfig
	 * @see #setConfig(ParseConfig[])
	 */
	public QueryStringObject(int size, ParseConfig[] config) {
		this.data = new HashMap<>(size);
		setConfig(config);
	}
	
	/**
	 * <p>构造方法
	 * <p>指定缓存Map构造
	 * @param data 缓存值
	 * @see #QueryStringObject(Map, ParseConfig[])
	 */
	public QueryStringObject(Map<String, String> data) {
		this(data, null);
	}

	/**
	 * <p>构造方法
	 * <p>指定缓存Map构造
	 * <p>可通过config设置qs的解析规则以及储存数据规则，详情见{@link ParseConfig}
	 * <p>此构造方法会重新创建一个Map，传入的Map不会有任何变化
	 * @param data 缓存值
	 * @param config 解析配置
	 * @see ParseConfig
	 * @see #setConfig(ParseConfig[])
	 * @see #puts(Map)
	 */
	public QueryStringObject(Map<String, String> data, ParseConfig[] config) {
		setConfig(config);
		if(nullValueAppend && blankValueAppend && !trimValue) {
			this.data = data;
		}else {
			if(!nullValueAppend && !blankValueAppend) {
				this.data = new HashMap<>(data.size());
			}else {
				this.data = new HashMap<>();
			}
			puts(data);
		}
	}
	
	/**
	 * <p>设置属性
	 * <p>设置需要对value值编码时使用的编码类型
	 * <p>设置需要对key进行排序时的排序规则
	 * @param charset {@link #charset}编码类型
	 * @param sort {@link #sort}排序规则
	 * @return QueryStringObject
	 */
	public QueryStringObject setConfig(String charset, Comparator<? super String> sort) {
		if(charset != null) {
			this.charset = charset;
		}
		if(sort != null) {
			this.sort = sort;
		}
		return this;
	}

	/**
	 * <p>设置属性
	 * <p>设置ParseConfig
	 * @param config 可通过config设置qs的解析规则以及储存数据规则，详情见{@link ParseConfig}
	 * @return QueryStringObject
	 * @see ParseConfig
	 */
	public QueryStringObject setConfig(ParseConfig[] config) {
		if(config == null) {
			return this;
		}
		for(int i = 0; i < config.length; i++) {
			switch (config[i]) {
				case KEY_SORT:
					keySort = true;
					break;
				case NOT_ENCODE_VALUE:
					valueEncode = false;
					break;
				case NOT_DECODE_VALUE:
					valueDecode = false;
					break;
				case NULL_VALUE_TO_BLANK:
					nullValueToBlank = true;
					break;
				case NULL_VALUE_APPEND:
					nullValueAppend = true;
					break;
				case BLANK_VALUE_APPEND:
					blankValueAppend = true;
					break;
				case TRIM_VALUE:
					trimValue = true;
					break;
				default:
					break;
			}
		}
		return this;
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * <p>内容为map中的所有条目
	 * @param map
	 */
	public void putAll(Map<Object, Object> map) {
		for(Entry<Object, Object> entry : map.entrySet()) {
			put(entry.getKey() == null ? null : entry.getKey().toString(), entry.getValue());
		}
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * <p>内容为map中的所有条目
	 * @param map
	 */
	public void puts(Map<String, String> map) {
		for(Entry<String, String> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * @param key qs属性名
	 * @param value qs属性值
	 * @return qs对象
	 */
	public QueryStringObject put(String key, Object value) {
		if(value == null) {
			return put(key, null);
		}else {
			return put(key, value.toString());
		}
	}

	/**
	 * <p>向qs对象中添加内容
	 * @param key qs属性名
	 * @param value qs属性值
	 * @return qs对象
	 */
	public QueryStringObject put(String key, String value) {
		if(value == null) {
			if(nullValueAppend) {
				value = "null";
				if(nullValueToBlank) {
					value = "";
				}
			}
		}
		if(value == null) {
			return this;
		}
		String val1 = value.trim();
		if(val1.isEmpty()) {
			if(!blankValueAppend) {
				return this;
			}
		}
		if(trimValue) {
			if(valueDecode) {
				try {
					data.put(key, URLDecoder.decode(val1, charset));
				}catch (UnsupportedEncodingException e) {
					data.put(key, val1);
				}
			}else {
				data.put(key, val1);
			}
		}else {
			if(valueDecode) {
				try {
					data.put(key, URLDecoder.decode(value, charset));
				}catch (UnsupportedEncodingException e) {
					data.put(key, value);
				}
			}else {
				data.put(key, value);
			}
		}
		return this;
	}
	
	/**
	 * <p>重写toString
	 * @see #toQueryString()
	 * @return queryString
	 */
	public String toString() {
		return toQueryString();
	}
	
	/**
	 * <p>将QueryStringObject对象的值转换为qs字符串返回
	 * <p>通过{@link #keySort}判断是否需要对key进行排序
	 * <p>排序默认按照ASCII编码升序
	 * <p>你也可以通过设置{@link #sort}来指定排序规则
	 * @see #toNoSortString()
	 * @see #toSortString()
	 * @return queryString
	 */
	public String toQueryString() {
		if(keySort) {
			return toSortString();
		}else {
			return toNoSortString();
		}
	}
	
	/**
	 * <p>将QueryStringObject对象的值转换为qs字符串返回
	 * <p>此方法转换时不会对key进行排序
	 * <p>根据{@link #valueEncode}判断是否需要对value进行URLEncoder编码
	 * @see #appendEncodeQS(StringBuilder, String, Object)
	 * @see #appendQS(StringBuilder, String, Object)
	 * @return queryString
	 */
	private String toNoSortString() {
		Set<String> keySet = data.keySet();
		StringBuilder sb = new StringBuilder();
		if(valueEncode) {
			for(String k : keySet) {
				appendEncodeQS(sb, k, data.get(k));
			}
		}else {
			for(String k : keySet) {
				appendQS(sb, k, data.get(k));
			}
		}
		if(sb.length() > 0) {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}
		return null;
	}

	/**
	 * <p>将QueryStringObject对象的值转换为qs字符串返回
	 * <p>此方法转换时会对key进行排序
	 * <p>如果设置过排序规则（{@link #sort}），则会使用排查规则进行排序
	 * <p>默认按照ASCII编码升序
	 * <p>根据{@link #valueEncode}判断是否需要对value进行URLEncoder编码
	 * @see #appendEncodeQS(StringBuilder, String, Object)
	 * @see #appendQS(StringBuilder, String, Object)
	 * @return queryString
	 */
	private String toSortString() {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		if(sort != null) {
			Arrays.sort(keyArray, sort);
		}else {
			Arrays.sort(keyArray);
		}
		StringBuilder sb = new StringBuilder();
		if(valueEncode) {
			for(String k : keyArray) {
				appendEncodeQS(sb, k, data.get(k));
			}
		}else {
			for(String k : keyArray) {
				appendQS(sb, k, data.get(k));
			}
		}
		if(sb.length() > 0) {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}
		return null;
	}
	
	/**
	 * <p>向qs字符串中添加一条键值对
	 * <p>此方法添加不会对value进行URLEncoder处理
	 * @param strb qs字符串对象
	 * @param k 键
	 * @param value 值
	 */
	private void appendQS(StringBuilder strb, String k, String value) {
		strb.append(k).append("=").append(value.toString()).append("&");
	}

	/**
	 * <p>向qs字符串中添加一条键值对
	 * <p>此方法添加会对value进行URLEncoder处理
	 * @param strb qs字符串对象
	 * @param k 键
	 * @param value 值
	 */
	private void appendEncodeQS(StringBuilder strb, String k, String value) {
		try {
			strb.append(k).append("=").append(URLEncoder.encode(value.toString(), charset)).append("&");
		}catch (UnsupportedEncodingException e) {
			strb.append(k).append("=").append(value.toString()).append("&");
		}
	}
	
	/**
	 * 获取qs对象的储存map.
	 * @return
	 */
	public Map<String, String> innerMap() {
		return data;
	}
	
	public String get(String name) {
		return data.get(name);
	}
	
	/**
	 * 将QS对象转为JavaBean
	 * @param <T>
	 * @param clazs
	 * @return
	 */
	public <T> T toJavaBean(Class<T> clazs) {
		return super.toJavaBean(this, clazs);
	}
	
}
