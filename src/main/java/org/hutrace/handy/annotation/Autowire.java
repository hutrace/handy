package org.hutrace.handy.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>controller注入service类需要的注解
 * <p>默认会采用初始化配置中的值
 * <p>通过注解可以更改当前注入模式（单列还是多例）
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowire {
}
