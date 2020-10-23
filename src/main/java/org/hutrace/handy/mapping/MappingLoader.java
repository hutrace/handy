package org.hutrace.handy.mapping;

import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.loader.Loader;

/**
 * <p>映射加载器
 * <p>通过{@link MappingExecutor}一键加载
 * @author hutrace
 * @see Loader
 * @see MappingExecutor
 * @since 1.8
 * @version 1.0
 */
public class MappingLoader implements Loader {
	
	@Override
	public void execute() throws AppLoaderException {
		MappingExecutor executor = new MappingExecutor();
		executor.start();
	}
	
}
