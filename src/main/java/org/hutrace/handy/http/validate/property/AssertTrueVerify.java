package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;
import java.util.Objects;

import org.hutrace.handy.annotation.verify.AssertTrue;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>Boolean值验证, true验证, 继承{@link AbstractValidate}
 * <p>值为true时才能通过验证
 * <p>{@link AssertTrue}的具体实现
 * @author hu trace
 * @see AbstractValidate
 * @since 1.8
 * @version 1.0
 */
public class AssertTrueVerify extends AbstractValidate {

	public static final Class<AssertTrue> CLASS = AssertTrue.class;
	
	public static final AssertTrueVerify instance = new AssertTrueVerify();
	
	@Override
	public void check(Field field, Object value) {
		AssertTrue annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(!"true".equals(value) || !Objects.equals(true, value)) {
				ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "asserttrue"));
			}
		}
	}
	
}
