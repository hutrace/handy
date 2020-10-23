package org.hutrace.handy.language;

/**
 * <p>自定义语言包Logger工厂
 * <p>用于构建语言包Logger
 * <pre>
 *  栗子: 
 *    Logger log = LoggerFactory.getLogger(this.getClass());
 *    log.debug("语言包中对应的名称key");
 *    log.error("语言包中对应的名称key", "对应值包含的变量", "变量对应的值");
 *    log.info("语言包中对应的名称key", "对应值包含的变量数组", "变量对应的值数组");
 * </pre>
 * @author hu trace
 * @see Logger
 */
public class LoggerFactory {
	
	public static Logger getLogger(Class<?> clazs) {
		return getLogger(clazs.getName());
	}
	
	public static Logger getLogger(String clazsName) {
		return getLogger(org.slf4j.LoggerFactory.getLogger(clazsName));
	}
	
	public static Logger getLogger(org.slf4j.Logger log) {
		return new Logger(log);
	}
	
}
