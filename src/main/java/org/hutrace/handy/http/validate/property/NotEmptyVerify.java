package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.NotEmpty;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>非空验证, 继承{@link AbstractValidate}
 * <p>{@link NotEmpty}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see NotEmpty
 * @since 1.8
 * @version 1.0
 */
public class NotEmptyVerify extends AbstractValidate {

	public static final Class<NotEmpty> CLASS = NotEmpty.class;
	
	public static final NotEmptyVerify instance = new NotEmptyVerify();
	
	@Override
	public void check(Field field, Object value) {
		NotEmpty annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null || value.toString().equals("")) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notempty"));
			}
		}
	}
	
}
