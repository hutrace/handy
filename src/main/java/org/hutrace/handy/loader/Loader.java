package org.hutrace.handy.loader;

import org.hutrace.handy.exception.AppLoaderException;

/**
 * <p>加载器规范接口
 * <p>加载器必须实现此接口才能配置
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd Hutrace</a>
 * @since 1.8
 * @version 1.0
 * @time 2019年5月15日
 */
public interface Loader {
	
	/**
	 * <p>开始执行的方法
	 * @throws AppLoaderException
	 */
	public void execute() throws AppLoaderException;
	
}
