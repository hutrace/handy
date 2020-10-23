package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hutrace.handy.http.RequestMethod;

/**
 * <p>绑定请求映射地址的注解，将此注解放在类或方法上可以根据value值绑定请求路径
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
	
	/**
	 * @return 需要绑定请求映射路径地址
	 */
	public String[] value() default {""};
	
	/**
	 * @return 规定请求的类型
	 */
	public RequestMethod method() default RequestMethod.GET;
	
	/**
	 * <p>当前版本暂时未实现
	 * @return 规定消息的请求头
	 */
	@Deprecated
	public String[] headers() default {"content-type=application/json"};
	
	/**
	 * @return 规定响应的消息头
	 */
	public String produces() default "";
	
}
