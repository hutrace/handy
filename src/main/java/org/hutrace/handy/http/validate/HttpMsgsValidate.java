package org.hutrace.handy.http.validate;

import java.lang.reflect.Field;

import org.hutrace.handy.exception.ValidateRuntimeException;

/**
 * <p>参数验证接口规范，通过实现此接口可以对Control中接收的参数进行验证
 * @author hutrace
 * @see ValidateRuntimeException
 * @since 1.8
 * @version 1.0
 */
public interface HttpMsgsValidate {
	
	/**
	 * <p>验证的方法，通过传入的Field及value值进行验证，
	 * <p>如果验证未通过，需要抛出{@link ValidateRuntimeException}
	 * @param field
	 * @param value
	 */
	public void validate(Field field, Object value);
	
}
