package org.hutrace.handy.config;


/**
 * <p>App采用对象模式，支持单例和多例
 * <p>可以指定App采用对象模式，默认以指定模式为准
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.8
 * @version 1.0
 * @time 2019年3月22日
 */
public enum Pattern {
	
	/**
	 * <p>多例模式
	 */
	MULTITON, 
	/**
	 * <p>单例模式
	 */
	SINGLETON,
	/**
	 * <p>默认（根据配置文件而定）
	 */
	DEFAULT
	
}
