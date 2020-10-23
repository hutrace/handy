package org.hutrace.handy.http;

/**
 * <p>返回数据定义
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public interface ServerResult {
	
	/**
	 * <p>返回的自定义约束状态值
	 * @return 自定义约束状态值
	 */
	int getCode();
	
	/**
	 * <p>返回的http状态值
	 * @return http状态值
	 */
	int getStatus();
	
	/**
	 * <p>返回的提示信息
	 * @return 提示信息
	 */
	String getMsg();
	
	/**
	 * <p>返回的有效数据
	 * @return 有效数据
	 */
	Object getData();
	
	/**
	 * <p>时间戳
	 * @return
	 */
	default long getTimestamp() {
		return System.currentTimeMillis();
	}
	
}
