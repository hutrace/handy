package org.hutrace.handy.http.result;

/**
 * 响应类标准接口
 * @author hu trace
 *
 */
public interface ResultBean {
	
	/**
	 * 设置请求ID
	 * @return
	 */
	ResultBean setReqId(String reqId);
	
	/**
	 * 设置响应ID
	 * @return
	 */
	ResultBean setRspId(String rspId);
	
	/**
	 * 设置响应code
	 * @return
	 */
	ResultBean setCode(int code);
	
	/**
	 * 设置响应提示消息
	 * @return
	 */
	ResultBean setMsg(String msg);
	
	/**
	 * 设置响应数据
	 * @return
	 */
	ResultBean setData(Object data);
	
	/**
	 * 设置响应时间戳
	 * @return
	 */
	ResultBean setTimestamp(long timestamp);
	
	/**
	 * 设置响应消息头文本类型
	 * @param contentType
	 * @return
	 */
	ResultBean setContentType(String contentType);
	
	/**
	 * 获取当前响应的类型
	 * @return
	 */
	String contentType();
	
	/**
	 * 最终组成的响应数据字符串
	 * @return
	 */
	String getString();
	
	/**
	 * 获取内部储存元数据
	 * @return
	 */
	Object inner();
	
}
