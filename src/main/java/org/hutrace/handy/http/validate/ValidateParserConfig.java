package org.hutrace.handy.http.validate;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

/**
 * <p>配置自定义参数验证反序列化类
 * @author hutrace
 * @see ParserConfig
 * @see ValidateJavaBeanDeserializer
 * @since 1.8
 * @version 1.0
 */
public class ValidateParserConfig extends ParserConfig {
	
	public static final ValidateParserConfig instance = new ValidateParserConfig();

	public ValidateParserConfig() {
		super();
	}
	
	@Override
	public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
		return new ValidateJavaBeanDeserializer(this, clazz, type);
	}
	
}
