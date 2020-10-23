package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hutrace.handy.config.Pattern;

/**
 * <p>WebSocket主键，使用此注解可以将类添加成WebSocket映射
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocket {
	
	/**
	 * <p>控制器的实例化方式
	 * @return
	 */
	public Pattern pattern() default Pattern.DEFAULT;

}
