package org.hutrace.handy.mapping;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.DAO;
import org.hutrace.handy.annotation.Service;

/**
 * <p>{@link DAO}表（包装类）
 * <p>用于储存{@link DAO}的实列
 * <p>此包装类存在于{@link Service}对{@link DAO}的引用，引用多次会存在多次
 * <pre>
 *  例如：在3个不同的Service中引用同一个Dao，这里就会存在3个包装类，当然，前提还得是Service为多例模式；
 *  如果是单例模式，则不会存在此包装类
 * @author hutrace
 * @see Field
 * @since 1.8
 * @version 1.0
 */
public class TableDao {
	
	/**
	 * {@link DAO}的实列
	 */
	Object instance;
	
	/**
	 * {@link DAO}的字段属性
	 */
	Field field;
	
	/**
	 * <p>通过{@link DAO}的实列以及字段属性构造
	 * @param instance
	 * @param field
	 */
	TableDao(Object instance, Field field) {
		this.instance = instance;
		this.field = field;
	}
	
	/**
	 * <p>设置{@link Service}中引用的{@link DAO}字段
	 * @param parentInstance
	 * @throws Exception
	 */
	void setServiceField(Object parentInstance) throws Exception {
		field.set(parentInstance, instance);
	}
	
}
