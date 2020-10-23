package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>绑定请求映射地址的注解，将此注解放在类或方法上可以根据value值绑定请求路径
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketMapping {
	
	/**
	 * @return 需要绑定请求映射路径地址
	 */
	public String[] value() default {""};
	
	/**
	 * @return 请求数据类型，根据此值使用对应的解析器
	 */
	public String[] type() default {"json"};
	
}
