package org.hutrace.handy.http.validate;

import java.lang.reflect.Field;

import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.LanguageLoader;

/**
 * <p>校验规范
 * <p>通过继承此类, 重新{@link #check(Field, Object)}方法可以实现自定义验证的目的
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public abstract class AbstractValidate {
	
	public abstract void check(Field field, Object value);
	
	/**
	 * 获取提示信息
	 * @param msg 错误信息，注解的信息
	 * @param mandatory 是否强制使用语言包
	 * @return 错误提示信息
	 */
	protected String getMsg(String name, String msg, boolean mandatory, String property) {
		if(msg.isEmpty()) {
			mandatory = true;
			msg = "serve.validate." + property;
			return name + " " + ApplicationProperty.get(msg);
		}
		if(mandatory || LanguageLoader.instance.isUseAppLanguage()) {
			return ApplicationProperty.get(msg);
		}else {
			return msg;
		}
	}
	
}
