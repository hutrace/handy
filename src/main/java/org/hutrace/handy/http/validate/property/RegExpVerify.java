package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import org.hutrace.handy.annotation.verify.RegExp;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>自定义正则表达式验证, 继承{@link AbstractValidate}
 * <p>{@link RegExp}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see RegExp
 * @since 1.8
 * @version 1.0
 */
public class RegExpVerify extends AbstractValidate {

	public static final Class<RegExp> CLASS = RegExp.class;
	
	public static final RegExpVerify instance = new RegExpVerify();
	
	@Override
	public void check(Field field, Object value) {
		RegExp annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null) {
				if(!annota.allowNull()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notnull"));
				}
			}else {
				if(value instanceof CharSequence) {
					if(!Pattern.matches(annota.reg(), value.toString())) {
						ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "regexp"));
					}
				}else {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "regexp"));
				}
			}
		}
	}
	
}
