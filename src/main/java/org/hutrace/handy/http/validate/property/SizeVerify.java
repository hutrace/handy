package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.hutrace.handy.annotation.verify.Size;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>长度验证, 继承{@link AbstractValidate}
 * <p>{@link Size}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see Size
 * @since 1.8
 * @version 1.0
 */
public class SizeVerify extends AbstractValidate {

	public static final Class<Size> CLASS = Size.class;
	
	public static final SizeVerify instance = new SizeVerify();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void check(Field field, Object value) {
		Size annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null) {
				if(!annota.allowNull()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notnull"));
				}
			}else {
				Class<?> valueClass = value.getClass();
				int length = -1;
				if(value instanceof String) {
					length = value.toString().length();
				}else if(valueClass.isArray()) {
					length = Array.getLength(value);
				}else if(value instanceof Collection) {
					length = ((Collection) value).size();
				}else if(value instanceof Map) {
					length = ((Map) value).size();
				}
				if(length < annota.min() || length > annota.max()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "size"));
				}
			}
		}
	}
	
}
