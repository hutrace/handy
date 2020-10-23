package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.Number;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>数字(int)验证, 继承{@link AbstractValidate}
 * <p>验证值与{@link Number}设定的值是否匹配
 * <p>{@link Number}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see Number
 * @since 1.8
 * @version 1.0
 */
public class NumberVerify extends AbstractValidate {

	public static final Class<Number> CLASS = Number.class;
	
	public static final NumberVerify instance = new NumberVerify();
	
	@Override
	public void check(Field field, Object value) {
		Number annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null) {
				if(!annota.allowNull()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notnull"));
				}
			}else {
				int intValue;
				if(value.getClass() == int.class) {
					intValue = (int) value;
				}else {
					try {
						intValue = Integer.parseInt(value.toString());
					}catch (Exception e) {
						intValue = 0;
					}
				}
				if(intValue < annota.min() || intValue > annota.max()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "number"));
				}
			}
		}
	}
	
}
