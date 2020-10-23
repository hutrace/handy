package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hutrace.handy.config.Pattern;

/**
 * <p>作用在服务层的注解，通过在类上面添加此注解可以规定此类为{@link Service}层
 * <p>服务层的作用在于请求数据库、业务操作、可以对数据库进行事物管理、事物回滚
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
	
	/**
	 * <p>控制器的实例化方式
	 * @return
	 */
	public Pattern pattern() default Pattern.DEFAULT;
	
}
