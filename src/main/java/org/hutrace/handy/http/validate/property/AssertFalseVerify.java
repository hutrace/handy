package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;
import java.util.Objects;

import org.hutrace.handy.annotation.verify.AssertFalse;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>Boolean值验证, false验证, 继承{@link AbstractValidate}
 * <p>值为false时才能通过验证
 * <p>{@link AssertFalse}的具体实现
 * @author hu trace
 * @see AbstractValidate
 * @since 1.8
 * @version 1.0
 */
public class AssertFalseVerify extends AbstractValidate {

	public static final Class<AssertFalse> CLASS = AssertFalse.class;
	
	public static final AssertFalseVerify instance = new AssertFalseVerify();
	
	@Override
	public void check(Field field, Object value) {
		AssertFalse annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(!"false".equals(value) || !Objects.equals(false, value)) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "assertfalse"));
			}
		}
	}
	
}
