package org.hutrace.handy.utils.qs.parser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.hutrace.handy.exception.QueryStringException;
import org.hutrace.handy.utils.qs.ParseConfig;
import org.hutrace.handy.utils.qs.QueryStringObject;
import org.hutrace.handy.utils.qs.annotation.QSField;

import com.alibaba.fastjson.util.TypeUtils;

/**
 * <p>JavaBean解析器
 * <p>实现将JavaBean转换为QueryString对象
 * @author hutrace
 *
 */
public class JavaBeanParser extends AbstractParser {
	
	public static final JavaBeanParser instance = new JavaBeanParser();
	
	private JavaBeanParser() {}
	
	public QueryStringObject parse(Object object, ParseConfig[] config) {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(object.getClass());
		}catch (IntrospectionException e) {
			throw new QueryStringException(e);
		}
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		CacheField[] fields = initFields(object.getClass().getDeclaredFields());
		QueryStringObject qs = new QueryStringObject(pds.length, config);
		PropertyDescriptor pd;
		Method method;
		for(int i = 0; i < pds.length; i++) {
			pd = pds[i];
			method = pd.getReadMethod();
			if(method != null && !"class".equals(pd.getName())) {
				try {
					qs.put(getName(pd.getName(), fields), method.invoke(object));
				}catch (Exception e) {
					throw new QueryStringException(e);
				}
			}
		}
		return qs;
	}
	
	private String getName(String name, CacheField[] fields) {
		CacheField field;
		for (int i = 0; i < fields.length; i++) {
			field = fields[i];
			if(field != null && field.name.equals(name)) {
				return field.alias;
			}
		}
		return name;
	}
	
	private CacheField[] initFields(Field[] fields) {
		CacheField[] cache = new CacheField[fields.length];
		QSField qsField;
		for(int i = 0; i < cache.length; i++) {
			qsField = fields[i].getAnnotation(QSField.class);
			if(qsField == null) {
				cache[i] = null;
			}else {
				cache[i] = new CacheField(fields[i].getName(), qsField.name());
			}
		}
		return cache;
	}
	
	private class CacheField {
		private String name;
		private String alias;
		private CacheField(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}
	}
	
	public <T> T toJavaBean(Map<String, String> map, Class<T> clazs) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(clazs);
		}catch (IntrospectionException e) {
			throw new QueryStringException(e);
		}
		PropertyDescriptor[] pds = info.getPropertyDescriptors();
		CacheField[] fields = initFields(clazs.getDeclaredFields());
		PropertyDescriptor pd;
		T obj = clazs.newInstance();
		String value;
		for(int i = 0; i < pds.length; i++) {
			pd = pds[i];
			value = map.get(getName(pd.getName(), fields));
			if(value != null) {
				pd.getWriteMethod().invoke(obj, TypeUtils.castToJavaBean(value, pd.getPropertyType()));
			}
		}
		return obj;
	}
	
}
