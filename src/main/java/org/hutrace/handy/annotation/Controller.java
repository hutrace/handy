package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hutrace.handy.config.Pattern;

/**
 * <p>作用在控制器上的注解，添加此注解会将此类添加至控制器
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
	
	/**
	 * <p>控制器的实例化方式
	 * @return
	 */
	public Pattern pattern() default Pattern.DEFAULT;
	
}
