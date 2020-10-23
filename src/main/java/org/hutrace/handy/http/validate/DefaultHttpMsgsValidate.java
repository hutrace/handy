package org.hutrace.handy.http.validate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.hutrace.handy.exception.ValidateRuntimeException;
import org.hutrace.handy.http.validate.property.AssertFalseVerify;
import org.hutrace.handy.http.validate.property.AssertTrueVerify;
import org.hutrace.handy.http.validate.property.BigNumberVerify;
import org.hutrace.handy.http.validate.property.NotBlankVerify;
import org.hutrace.handy.http.validate.property.NotEmptyVerify;
import org.hutrace.handy.http.validate.property.NotNullVerify;
import org.hutrace.handy.http.validate.property.NullVerify;
import org.hutrace.handy.http.validate.property.NumberVerify;
import org.hutrace.handy.http.validate.property.RegExpVerify;
import org.hutrace.handy.http.validate.property.SizeVerify;

/**
 * <p>默认的参数验证器
 * <p>此验证器针对{@link ValidateJSONObject}转换的数据可用
 * @author hutrace
 * @see HttpMsgsValidate
 * @see ValidateRuntimeException
 * @see ValidateJSONObject
 * @since 1.8
 * @version 1.0
 */
public class DefaultHttpMsgsValidate implements HttpMsgsValidate {
	
	private static final Map<Type, AbstractValidate> VERIFY_PROCEDURE_MAP = new HashMap<>();
	
	static {
		VERIFY_PROCEDURE_MAP.put(NullVerify.CLASS, NullVerify.instance);
		VERIFY_PROCEDURE_MAP.put(NotNullVerify.CLASS, NotNullVerify.instance);
		VERIFY_PROCEDURE_MAP.put(NotEmptyVerify.CLASS, NotEmptyVerify.instance);
		VERIFY_PROCEDURE_MAP.put(NotBlankVerify.CLASS, NotBlankVerify.instance);
		VERIFY_PROCEDURE_MAP.put(AssertTrueVerify.CLASS, AssertTrueVerify.instance);
		VERIFY_PROCEDURE_MAP.put(AssertFalseVerify.CLASS, AssertFalseVerify.instance);
		VERIFY_PROCEDURE_MAP.put(NumberVerify.CLASS, NumberVerify.instance);
		VERIFY_PROCEDURE_MAP.put(BigNumberVerify.CLASS, BigNumberVerify.instance);
		VERIFY_PROCEDURE_MAP.put(SizeVerify.CLASS, SizeVerify.instance);
		VERIFY_PROCEDURE_MAP.put(RegExpVerify.CLASS, RegExpVerify.instance);
	}
	
	@Override
	public void validate(Field field, Object value) throws ValidateRuntimeException {
		Annotation[] annotations = field.getAnnotations();
		for(int i = 0; i < annotations.length; i++) {
			VERIFY_PROCEDURE_MAP.get(annotations[i].annotationType()).check(field, value);
		}
	}

}
