package org.hutrace.handy.annotation.verify;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>非空验证，校验注解字段是否为null
 * @author hu trace
 * @since 1.8
 * @version 1.0
 * @time 2019年3月27日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {
	
	String msg() default "";
	
	/**
	 * 是否强制使用语言包
	 * <p>哪怕是配置中没有配置语言包的资源管理器，也会强制使用语言包
	 * <p>此属性针对公共型框架中适用
	 * @return
	 */
	boolean mandatory() default false;
	
}
