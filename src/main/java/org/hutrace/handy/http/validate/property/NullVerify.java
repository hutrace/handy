package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.Null;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>空验证, 继承{@link AbstractValidate}
 * <p>{@link Null}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see Null
 * @since 1.8
 * @version 1.0
 */
public class NullVerify extends AbstractValidate {

	public static final Class<Null> CLASS = Null.class;
	
	public static final NullVerify instance = new NullVerify();
	
	@Override
	public void check(Field field, Object value) {
		Null annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value != null) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "null"));
			}
		}
	}
	
}
