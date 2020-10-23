package org.hutrace.handy.utils.qs;

import java.util.Comparator;
import java.util.Map;

import org.hutrace.handy.exception.QueryStringException;
import org.hutrace.handy.utils.qs.parser.JavaBeanParser;
import org.hutrace.handy.utils.qs.parser.MapParser;

/**
 * <p>qs工具类
 * <p>可以使用此类对qs格式的字符串与Map等Java对象进行转换
 * <p>也可以将Java对象转换成qs字符串
 * @author hutrace
 */
public class QueryString {

	/**
	 * 将qs字符串解析为qs对象
	 * @param text qs字符串
	 * @return qs对象
	 */
	public static QueryStringObject parse(String text) {
		return parse(text, null, null);
	}

	/**
	 * 将qs字符串解析为qs对象
	 * @param text qs字符串
	 * @param config 解析配置
	 * @return qs对象
	 */
	public static QueryStringObject parse(String text, ParseConfig[] config) {
		return parse(text, config, null);
	}

	/**
	 * 将qs字符串解析为qs对象
	 * @param text qs字符串
	 * @param charset 对value解码的编码格式
	 * @return qs对象
	 */
	public static QueryStringObject parse(String text, String charset) {
		return parse(text, null, charset);
	}
	
	/**
	 * 将qs字符串解析为qs对象
	 * @param text qs字符串
	 * @param config 解析配置
	 * @param charset 对value解码的编码格式
	 * @return qs对象
	 */
	public static QueryStringObject parse(String text, ParseConfig[] config, String charset) {
		// 后面可优化此方式
		String [] arr = text.split("&");
		QueryStringObject qs = new QueryStringObject(arr.length);
		qs.setConfig(config);
		String[] oneData;
		for(int i = 0; i < arr.length; i++) {
			if(arr[i].isEmpty()) {
				break;
			}
			oneData = arr[i].split("=");
			if(oneData.length == 1) {
				qs.put(oneData[0], null);
			}else {
				qs.put(oneData[0], oneData[1]);
			}
		}
		return qs;
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object) {
		return toQS(object, null, null, null);
	}

	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param config 转换时需要设置的属性
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, ParseConfig[] config) {
		return toQS(object, config, null, null);
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param charset 使用URLEncoder时的编码格式
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, String charset) {
		return toQS(object, null, charset, null);
	}

	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param sort 排序规则
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, Comparator<? super String> sort) {
		return toQS(object, null, null, sort);
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param charset 使用URLEncoder时的编码格式
	 * @param sort 排序规则
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, String charset, Comparator<? super String> sort) {
		return toQS(object, null, charset, sort);
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param config 转换时需要设置的属性
	 * @param sort 排序规则
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, ParseConfig[] config, Comparator<? super String> sort) {
		return toQS(object, config, null, sort);
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param config 转换时需要设置的属性
	 * @param charset 使用URLEncoder时的编码格式
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, ParseConfig[] config, String charset) {
		return toQS(object, config, charset, null);
	}
	
	/**
	 * <p>将JavaBean/Map转成{@link QueryStringObject}对象
	 * @param object 需要转换的Map/JavaBean
	 * @param config 转换时需要设置的属性
	 * @param charset 使用URLEncoder时的编码格式
	 * @param sort 排序规则
	 * @return QueryStringObject
	 */
	public static QueryStringObject toQS(Object object, ParseConfig[] config,
			String charset, Comparator<? super String> sort) {
		if(object == null) {
			return null;
		}
		if(object instanceof QueryStringObject) {
			return (QueryStringObject) object;
		}
		if(object instanceof Map) {
			return MapParser.instance.parse(object, config, charset, sort);
		}
		return JavaBeanParser.instance.parse(object, config, charset, sort);
	}
	
	/**
	 * 将qs字符串转换成JavaBean
	 * @param <T>
	 * @param qs qs字符串
	 * @param clazs JavaBean的class
	 * @return 泛型
	 */
	public static <T> T toJavaBean(String qs, Class<T> clazs) {
		return toJavaBean(parse(qs), clazs);
	}
	
	/**
	 * 将QS对象转换成JavaBean
	 * @param <T>
	 * @param qs qs对象
	 * @param clazs JavaBean的class
	 * @return 泛型
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toJavaBean(QueryStringObject qs, Class<T> clazs) {
		if(clazs == Map.class) {
			return (T) qs.innerMap();
		}
		if(clazs == QueryStringObject.class) {
			return (T) qs;
		}
		if(clazs == String.class) {
			return (T) qs.toString();
		}
		try {
			return JavaBeanParser.instance.toJavaBean(qs.innerMap(), clazs);
		}catch (Exception e) {
			throw new QueryStringException(e);
		}
	}
	
}
