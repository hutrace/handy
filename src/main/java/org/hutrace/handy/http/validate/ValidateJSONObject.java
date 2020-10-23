package org.hutrace.handy.http.validate;

import java.lang.reflect.Type;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * <p>参数需要验证时使用的反序列化类, 继承{@link JSONObject}
 * <p>通过此类进行反序列化可以实现参数验证的效果
 * @author hutrace
 * @see JSONObject
 * @since 1.8
 * @version 1.0
 */
public class ValidateJSONObject extends JSONObject {

	private static final long serialVersionUID = 1L;
	
	public ValidateJSONObject(Map<String, Object> map){
        super(map);
    }
	
	public static ValidateJSONObject parseObject(String text) {
        Object obj = parse(text);
        if(obj instanceof JSONObject) {
            return (ValidateJSONObject) obj;
        }
        try {
            return (ValidateJSONObject) JSON.toJSON(obj);
        }catch (RuntimeException e) {
            throw new JSONException("can not cast to JSONObject.", e);
        }
    }
	
	public ValidateJSONObject(JSONObject json) {
		super(json.getInnerMap());
	}

	@Override
	public <T> T toJavaObject(Class<T> clazz) {
		return ValidateUtils.cast(this, clazz, ValidateParserConfig.instance);
	}

	public static <T> T toJavaObject(JSON json, Class<T> clazz) {
		return ValidateUtils.cast(json, clazz, ValidateParserConfig.instance);
	}

	/**
	 * @since 1.2.33
	 */
	public <T> T toJavaObject(Type type) {
		return ValidateUtils.cast(this, type, ValidateParserConfig.instance);
	}

	/**
	 * @since 1.2.33
	 */
	@SuppressWarnings("rawtypes")
	public <T> T toJavaObject(TypeReference typeReference) {
		Type type = typeReference != null ? typeReference.getType() : null;
		return ValidateUtils.cast(this, type, ValidateParserConfig.instance);
	}

}
