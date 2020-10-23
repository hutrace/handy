package org.hutrace.handy.http.validate;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.hutrace.handy.exception.ValidateRuntimeException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * <p>继承{@link TypeUtils}, 重新定义转换方法
 * @author hutrace
 * @see TypeUtils
 * @since 1.8
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValidateUtils extends TypeUtils {

	public static <T> T cast(Object obj, ParameterizedType type, ParserConfig mapping) {
		return TypeUtils.cast(obj, type, mapping);
	}

	public static <T> T castToJavaBean(Object obj, Class<T> clazz) {
		return cast(obj, clazz, ParserConfig.getGlobalInstance());
	}

	public static <T> T castToJavaBean(Map<String, Object> map, Class<T> clazz, ParserConfig config) {
		if (clazz == StackTraceElement.class) {
			String declaringClass = (String) map.get("className");
			String methodName = (String) map.get("methodName");
			String fileName = (String) map.get("fileName");
			int lineNumber;
			{
				Number value = (Number) map.get("lineNumber");
				if (value == null) {
					lineNumber = 0;
				} else {
					lineNumber = value.intValue();
				}
			}
			return (T) new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
		}

		{
			Object iClassObject = map.get(JSON.DEFAULT_TYPE_KEY);
			if (iClassObject instanceof String) {
				String className = (String) iClassObject;
				Class<?> loadClazz;
				if (config == null) {
					config = ParserConfig.global;
				}
				loadClazz = config.checkAutoType(className, null);
				if (loadClazz == null) {
					throw new ValidateRuntimeException(className + " not found");
				}
				if (!loadClazz.equals(clazz)) {
					return (T) castToJavaBean(map, loadClazz, config);
				}
			}
		}

		if (clazz.isInterface()) {
			JSONObject object;
			if (map instanceof JSONObject) {
				object = (JSONObject) map;
			} else {
				object = new JSONObject(map);
			}
			if (config == null) {
				config = ParserConfig.getGlobalInstance();
			}
			ObjectDeserializer deserializer = config.getDeserializers().get(clazz);
			if (deserializer != null) {
				String json = JSON.toJSONString(object);
				return (T) JSON.parseObject(json, clazz);
			}
			return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[] { clazz },
					object);
		}

		if (clazz == Locale.class) {
			Object arg0 = map.get("language");
			Object arg1 = map.get("country");
			if (arg0 instanceof String) {
				String language = (String) arg0;
				if (arg1 instanceof String) {
					String country = (String) arg1;
					return (T) new Locale(language, country);
				} else if (arg1 == null) {
					return (T) new Locale(language);
				}
			}
		}

		if (clazz == String.class && map instanceof JSONObject) {
			return (T) map.toString();
		}

		if (clazz == LinkedHashMap.class && map instanceof JSONObject) {
			JSONObject jsonObject = (JSONObject) map;
			Map innerMap = jsonObject.getInnerMap();
			if (innerMap instanceof LinkedHashMap) {
				return (T) innerMap;
			} else {
				LinkedHashMap linkedHashMap = new LinkedHashMap();
				linkedHashMap.putAll(innerMap);
			}
		}

		if (config == null) {
			config = ParserConfig.getGlobalInstance();
		}

		JavaBeanDeserializer javaBeanDeser = null;
		ObjectDeserializer deserizer = config.getDeserializer(clazz);
		if (deserizer instanceof JavaBeanDeserializer) {
			javaBeanDeser = (JavaBeanDeserializer) deserizer;
		}

		if (javaBeanDeser == null) {
			throw new JSONException("can not get javaBeanDeserializer. " + clazz.getName());
		}
		try {
			return (T) javaBeanDeser.createInstance(map, config);
		} catch (IllegalArgumentException e) {
			throw new ValidateRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new ValidateRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new ValidateRuntimeException(e);
		}
	}

	public static <T> T cast(Object obj, Class<T> clazz, ParserConfig config) {
		if (obj == null) {
			if (clazz == int.class) {
				return (T) Integer.valueOf(0);
			} else if (clazz == long.class) {
				return (T) Long.valueOf(0);
			} else if (clazz == short.class) {
				return (T) Short.valueOf((short) 0);
			} else if (clazz == byte.class) {
				return (T) Byte.valueOf((byte) 0);
			} else if (clazz == float.class) {
				return (T) Float.valueOf(0);
			} else if (clazz == double.class) {
				return (T) Double.valueOf(0);
			} else if (clazz == boolean.class) {
				return (T) Boolean.FALSE;
			}
			return null;
		}
		if (clazz == null) {
			throw new IllegalArgumentException("clazz is null");
		}
		if (clazz == obj.getClass()) {
			return (T) obj;
		}
		if (obj instanceof Map) {
			if (clazz == Map.class) {
				return (T) obj;
			}
			Map map = (Map) obj;
			if (clazz == Object.class && !map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
				return (T) obj;
			}
			return castToJavaBean((Map<String, Object>) obj, clazz, config);
		}
		if (clazz.isArray()) {
			if (obj instanceof Collection) {
				Collection collection = (Collection) obj;
				int index = 0;
				Object array = Array.newInstance(clazz.getComponentType(), collection.size());
				for (Object item : collection) {
					Object value = cast(item, clazz.getComponentType(), config);
					Array.set(array, index, value);
					index++;
				}
				return (T) array;
			}
			if (clazz == byte[].class) {
				return (T) castToBytes(obj);
			}
		}
		if (clazz.isAssignableFrom(obj.getClass())) {
			return (T) obj;
		}
		if (clazz == boolean.class || clazz == Boolean.class) {
			return (T) castToBoolean(obj);
		}
		if (clazz == byte.class || clazz == Byte.class) {
			return (T) castToByte(obj);
		}
		if (clazz == char.class || clazz == Character.class) {
			return (T) castToChar(obj);
		}
		if (clazz == short.class || clazz == Short.class) {
			return (T) castToShort(obj);
		}
		if (clazz == int.class || clazz == Integer.class) {
			return (T) castToInt(obj);
		}
		if (clazz == long.class || clazz == Long.class) {
			return (T) castToLong(obj);
		}
		if (clazz == float.class || clazz == Float.class) {
			return (T) castToFloat(obj);
		}
		if (clazz == double.class || clazz == Double.class) {
			return (T) castToDouble(obj);
		}
		if (clazz == String.class) {
			return (T) castToString(obj);
		}
		if (clazz == BigDecimal.class) {
			return (T) castToBigDecimal(obj);
		}
		if (clazz == BigInteger.class) {
			return (T) castToBigInteger(obj);
		}
		if (clazz == Date.class) {
			return (T) castToDate(obj);
		}
		if (clazz == java.sql.Date.class) {
			return (T) castToSqlDate(obj);
		}
		if (clazz == java.sql.Time.class) {
			return (T) castToSqlTime(obj);
		}
		if (clazz == java.sql.Timestamp.class) {
			return (T) castToTimestamp(obj);
		}
		if (clazz.isEnum()) {
			return (T) castToEnum(obj, clazz, config);
		}
		if (Calendar.class.isAssignableFrom(clazz)) {
			Date date = castToDate(obj);
			Calendar calendar;
			if (clazz == Calendar.class) {
				calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
			} else {
				try {
					calendar = (Calendar) clazz.newInstance();
				} catch (Exception e) {
					throw new JSONException("can not cast to : " + clazz.getName(), e);
				}
			}
			calendar.setTime(date);
			return (T) calendar;
		}

		String className = clazz.getName();
		if (className.equals("javax.xml.datatype.XMLGregorianCalendar")) {
			Date date = castToDate(obj);
			Calendar calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
			calendar.setTime(date);
			return (T) CalendarCodec.instance.createXMLGregorianCalendar(calendar);
		}

		if (obj instanceof String) {
			String strVal = (String) obj;
			if (strVal.length() == 0 //
					|| "null".equals(strVal) //
					|| "NULL".equals(strVal)) {
				return null;
			}
			if (clazz == java.util.Currency.class) {
				return (T) java.util.Currency.getInstance(strVal);
			}
			if (clazz == java.util.Locale.class) {
				return (T) toLocale(strVal);
			}

			if (className.startsWith("java.time.")) {
				String json = JSON.toJSONString(strVal);
				return JSON.parseObject(json, clazz);
			}
		}
		throw new JSONException("can not cast to : " + clazz.getName());
	}

	public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
		return TypeUtils.cast(obj, type, mapping);
	}
	
	public static void error(String msg) {
		throw new ValidateRuntimeException(msg);
	}

}
