package org.hutrace.handy.http.validate.qs;

import java.util.Comparator;
import java.util.Map;

import org.hutrace.handy.exception.QueryStringException;
import org.hutrace.handy.utils.qs.ParseConfig;
import org.hutrace.handy.utils.qs.QueryStringObject;

/**
 * <p>QueryString对象类, 包含常用的方法
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class ValidateQueryStringObject extends QueryStringObject {
	
	private QueryStringObject qs;
	
	public ValidateQueryStringObject(QueryStringObject qs) {
		this.qs = qs;
	}
	
	/**
	 * <p>设置属性
	 * <p>设置需要对value值编码时使用的编码类型
	 * <p>设置需要对key进行排序时的排序规则
	 * @param charset {@link #charset}编码类型
	 * @param sort {@link #sort}排序规则
	 * @return QueryStringObject
	 */
	public ValidateQueryStringObject setConfig(String charset, Comparator<? super String> sort) {
		qs.setConfig(charset, sort);
		return this;
	}

	/**
	 * <p>设置属性
	 * <p>设置ParseConfig
	 * @param config 可通过config设置qs的解析规则以及储存数据规则，详情见{@link ParseConfig}
	 * @return QueryStringObject
	 * @see ParseConfig
	 */
	public ValidateQueryStringObject setConfig(ParseConfig[] config) {
		qs.setConfig(config);
		return this;
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * <p>内容为map中的所有条目
	 * @param map
	 */
	public void putAll(Map<Object, Object> map) {
		qs.putAll(map);
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * <p>内容为map中的所有条目
	 * @param map
	 */
	public void puts(Map<String, String> map) {
		qs.puts(map);
	}
	
	/**
	 * <p>向qs对象中添加内容
	 * @param key qs属性名
	 * @param value qs属性值
	 * @return qs对象
	 */
	public ValidateQueryStringObject put(String key, Object value) {
		qs.put(key, value);
		return this;
	}

	/**
	 * <p>向qs对象中添加内容
	 * @param key qs属性名
	 * @param value qs属性值
	 * @return qs对象
	 */
	public ValidateQueryStringObject put(String key, String value) {
		qs.put(key, value);
		return this;
	}
	
	/**
	 * <p>重写toString
	 * @see #toQueryString()
	 * @return queryString
	 */
	public String toString() {
		return qs.toString();
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
		return qs.toQueryString();
	}
	
	/**
	 * 获取qs对象的储存map.
	 * @return
	 */
	public Map<String, String> innerMap() {
		return qs.innerMap();
	}
	
	/**
	 * 将QS对象转为JavaBean
	 * @param <T>
	 * @param clazs
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T toJavaBean(Class<T> clazs) {
		if(clazs == Map.class) {
			return (T) innerMap();
		}
		if(clazs == ValidateQueryStringObject.class) {
			return (T) this;
		}
		if(clazs == String.class) {
			return (T) toString();
		}
		try {
			return ValidateJavaBeanParser.instance.toJavaBean(innerMap(), clazs);
		}catch (Exception e) {
			throw new QueryStringException(e);
		}
	}
	
	
}
