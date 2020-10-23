package org.hutrace.handy.annotation.verify;

/**
 * <p>自定义正则表达式验证
 * <p>被注解元素需要设置验证的正则表达式, 也可以选择设定{@link #allowNull()}是否允许值为null, 默认不允许
 * @author hu trace
 * @since 1.8
 * @version 1.0
 * @time 2019年5月15日
 */
public @interface RegExp {
	
	String msg() default "";
	
	String reg();
	
	/**
	 * <p>是否允许被注解的属性为null
	 * @return
	 */
	boolean allowNull() default false;
	
	/**
	 * 是否强制使用语言包
	 * <p>哪怕是配置中没有配置语言包的资源管理器，也会强制使用语言包
	 * <p>此属性针对公共型框架中适用
	 * @return
	 */
	boolean mandatory() default false;
	
}
