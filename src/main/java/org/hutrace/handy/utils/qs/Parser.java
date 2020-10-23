package org.hutrace.handy.utils.qs;

import java.util.Comparator;

import org.hutrace.handy.exception.QueryStringException;

/**
 * <p>qs解析器接口
 * @author hutrace
 */
public interface Parser {
	
	/**
	 * 解析接口
	 * @param object 需要解析的对象
	 * @param config 设置的配置属性
	 * @param charset 当value需要decode时的编码
	 * @param sort 当需要对key排序时的排序规则
	 * @return QS对象
	 * @throws QueryStringException
	 */
	QueryStringObject parse(Object object, ParseConfig[] config, String charset,
			Comparator<? super String> sort) throws QueryStringException;
	
	/**
	 * 转换接口
	 * <p>将qs对象转换为JavaBean对象
	 * @param <T>
	 * @param clazs JavaBean类
	 * @return
	 */
	<T> T transform(Class<T> clazs);
	
}
