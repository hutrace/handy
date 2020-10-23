package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.NotBlank;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>非空验证, 继承{@link AbstractValidate}
 * <p>{@link NotBlank}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see NotBlank
 * @since 1.8
 * @version 1.0
 */
public class NotBlankVerify extends AbstractValidate {

	public static final Class<NotBlank> CLASS = NotBlank.class;
	
	public static final NotBlankVerify instance = new NotBlankVerify();
	
	@Override
	public void check(Field field, Object value) {
		NotBlank annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null || value.toString().trim().equals("")) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notblank"));
			}
		}
	}
	
}
