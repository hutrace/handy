package org.hutrace.handy.http;

/**
 * <p>Http返回状态码常量
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class ResponseCode {
	
	/**
	 * <p>找不到请求地址, 4001
	 */
	public static final int NOTFOUND_URL = 4001;
	/**
	 * <p>找不到请求类型（GET/POST/PUT...）, 4002
	 */
	public static final int NOTFOUND_METHOD = 4002;
	/**
	 * <p>找不到参数解析器, 4003
	 */
	public static final int NOTFOUND_RESOLVER = 4003;
	/**
	 * <p>找不到参数转换器器, 4004
	 */
	public static final int NOTFOUND_CONVERTER = 4004;
	/**
	 * <p>内容长度超出限制, 4005
	 */
	public static final int CONTENT_LENGTH_EXCEEDED = 4005;
	
	
	/**
	 * <p>系统错误，未知系统错误, 5000
	 */
	public static final int ERROR_SYSTEM_UNKNOWN = 5000;
	/**
	 * <p>系统错误，执行业务层异常, 5001
	 */
	public static final int ERROR_SYSTEM = 5001;
	/**
	 * <p>参数转换器转换过程错误, 5002
	 */
	public static final int ERROR_SYSTEM_CONVERTER = 5002;
	/**
	 * <p>参数转换器转换过程错误, 5003
	 */
	public static final int ERROR_PARAMETER = 5003;
	/**
	 * <p>参数解析器过程错误, 5004
	 */
	public static final int ERROR_RESOLVER = 5004;
	/**
	 * <p>在使用多例控制器时初始化控制器错误, 5005
	 */
	public static final int ERROR_INIT_CONTROL = 5005;
	/**
	 * <p>自定义异常, 5006
	 */
	public static final int ERROR_CUSTOM = 5006;
	
}
