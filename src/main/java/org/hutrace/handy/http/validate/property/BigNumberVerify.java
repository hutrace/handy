package org.hutrace.handy.http.validate.property;

import java.lang.reflect.Field;

import org.hutrace.handy.annotation.verify.BigNumber;
import org.hutrace.handy.http.validate.AbstractValidate;
import org.hutrace.handy.http.validate.ValidateUtils;

/**
 * <p>数字(Long)验证, 继承{@link AbstractValidate}
 * <p>验证值与{@link BigNumber}设定的值是否匹配
 * <p>{@link BigNumber}的具体实现
 * @author hutrace
 * @see AbstractValidate
 * @see BigNumber
 * @since 1.8
 * @version 1.0
 */
public class BigNumberVerify extends AbstractValidate {
	
	public static final Class<BigNumber> CLASS = BigNumber.class;
	
	public static final BigNumberVerify instance = new BigNumberVerify();
	
	@Override
	public void check(Field field, Object value) {
		BigNumber annota = field.getAnnotation(CLASS);
		if(annota != null) {
			if(value == null) {
				if(!annota.allowNull()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "notnull"));
				}
			}else {
				long longValue;
				if(value.getClass() == long.class) {
					longValue = (long) value;
				}else {
					try {
						longValue = Long.parseLong(value.toString());
					}catch (Exception e) {
						longValue = 0;
					}
				}
				if(longValue < annota.min() || longValue > annota.max()) {
					ValidateUtils.error(getMsg(field.getName(), annota.msg(), annota.mandatory(), "bignumber"));
				}
			}
		}
	}
	
}
