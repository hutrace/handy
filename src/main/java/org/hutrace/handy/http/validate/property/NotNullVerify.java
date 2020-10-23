package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.NotNull;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>非空验证, 继承{@link AbstractValidate}
 * <p>{@link NotNull}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see NotNull
 * @since 1.8
 * @version 1.0
 */
public class NotNullVerify extends AbstractValidate {
	
	public static final Class<NotNull> CLASS = NotNull.class;
	public static final NotNullVerify instance = new NotNullVerify();

	@Override
	public void check(Field field, Object value) {
		NotNull annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(null == value) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notnull"));
			}
		}
	}
	
}
