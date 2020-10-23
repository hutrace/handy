package org.hutrace.handy.utils.qs;

import java.net.URLEncoder;

/**
 * <p>解析qs对象的配置信息
 * <p>可以使用配置对qs参数进行想要实现的处理
 * @author hutrace
 */
public enum ParseConfig {
	
	/**
	 * <p>对key按ASCII码字典排序，默认排序规则是升序，可以传入Comparator自定义排序规则。
	 * <p>默认不会对key进行排序
	 */
	KEY_SORT,
	
	/**
	 * <p>使用{@link URLEncoder#encode(String, String)}对value进行编码。
	 * <p>默认会对value进行编码
	 */
	NOT_ENCODE_VALUE,
	
	/**
	 * <p>将qs字符串解析成对象时，是否使用URLDecoder解码
	 * <p>默认会对value进行解码
	 */
	NOT_DECODE_VALUE,
	
	/**
	 * <p>将为null的值转换为空字符串储存
	 * <p>此属性必须在设置过{@link #NULL_VALUE_APPEND}和{@link BLANK_VALUE_APPEND}才会生效
	 */
	NULL_VALUE_TO_BLANK,
	
	/**
	 * <p>当value为null时，需要将键值对解析到qs字符串。
	 * <p>默认value为null时，会直接跳过。
	 */
	NULL_VALUE_APPEND,
	
	/**
	 * <p>当value使用trim()后为空字符串，需要将键值对解析到qs字符串。
	 * <p>默认value使用trim()后为空字符串时，会直接跳过。
	 */
	BLANK_VALUE_APPEND,
	
	/**
	 * <p>使用trim()去掉value的首尾空格
	 * <p>默认不会对value做处理。
	 */
	TRIM_VALUE
	
}
