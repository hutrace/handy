package org.hutrace.handy.http;

import java.lang.reflect.Method;

import org.hutrace.handy.mapping.TableControlMethod;

/**
 * <p>用于记录{@link TableControlMethod}, 并添加{@link #method()}和{@link #parameterNames()}方法
 * @author hutrace
 * @see TableControlMethod
 * @since 1.8
 * @version 1.0
 */
public class HttpMethodRequest {
	
	private TableControlMethod controlMethod;
	
	public void setControlMethod(TableControlMethod controlMethod) {
		if(this.controlMethod == null) {
			this.controlMethod = controlMethod;
		}
	}
	
	/**
	 * <p>获取当前控制器中映射的方法
	 * @return 当前控制器中映射的方法
	 */
	public Method method() {
		return controlMethod.method();
	}
	
	/**
	 * <p>获取当前控制器中映射的方法中的参数名称
	 * @return 当前控制器中映射的方法中的参数名称
	 */
	public String[] parameterNames() {
		return controlMethod.parameters();
	}
	
}
