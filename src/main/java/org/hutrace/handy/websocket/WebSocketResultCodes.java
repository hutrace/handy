package org.hutrace.handy.websocket;

/**
 * <p>WebSocket返回字段code列表
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class WebSocketResultCodes {

	/**
	 * 成功, 0
	 */
	public static final int SUCCESS = 0x0;
	
	/**
	 * 数据格式错误, 4001
	 */
	public static final int MSG_HEADER_ERROR = 0xfa1;
	
	/**
	 * 找不到请求的地址, 4002
	 */
	public static final int NOTFOUND_URL = 0xfa2;
	
	/**
	 * 找不到请求的地址, 4003
	 */
	public static final int DATA_FORMAT_ERROR = 0xfa3;
	
	/**
	 * <p>找不到参数解析器, 4004
	 */
	public static final int NOTFOUND_RESOLVER = 0xfa4;
	
	/**
	 * <p>找不到参数转换器器, 4005
	 */
	public static final int NOTFOUND_CONVERTER = 0xfa5;
	
	/**
	 * 系统错误, 5001
	 */
	public static final int SYSTEM_ERROR = 0x1389;
	/**
	 * <p>参数转换器转换过程错误, 5002
	 */
	public static final int ERROR_SYSTEM_CONVERTER = 0x138a;
	/**
	 * <p>参数转换器转换过程错误, 5003
	 */
	public static final int ERROR_PARAMETER = 0x138b;
	/**
	 * <p>自定义异常, 5005
	 */
	public static final int ERROR_CUSTOM = 0x138d;
	
}
