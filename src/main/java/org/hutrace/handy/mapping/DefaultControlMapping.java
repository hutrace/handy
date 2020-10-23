package org.hutrace.handy.mapping;

import org.hutrace.handy.annotation.Controller;
import org.hutrace.handy.annotation.RequestMapping;

/**
 * <p>默认空映射
 * <p>针对在{@link Controller}注解下没有包含{@link RequestMapping}的注解, 使用此空映射配置
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
@RequestMapping
public class DefaultControlMapping {
	
}
