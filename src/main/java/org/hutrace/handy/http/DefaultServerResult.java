package org.hutrace.handy.http;

/**
 * <p>默认使用的返回类
 * <p>成功code和status都为0
 * <p>code的可选值,'0' 或 '-1','0'--成功,'-1'--失败
 * <pre>
 *  status的值:
 *      0 -- 成功
 *      4xxx -- 未找到资源对应错误
 *      5xxx -- 系统错误
 *      60xx -- 中间层错误（框架层）
 *      61xx -- 业务错误
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @see ServerResult
 * @since 1.8
 * @version 1.0
 * @time 2019年3月25日
 */
public class DefaultServerResult implements ServerResult {

	private int code;
	private int status;
	private String msg;
	private Object data;
	public DefaultServerResult() {
		this.status = 0;
	}
	public DefaultServerResult(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public static DefaultServerResult build() {
		return new DefaultServerResult();
	}
	@Override
	public int getCode() {
		return code;
	}
	@Override
	public int getStatus() {
		return status;
	}
	@Override
	public String getMsg() {
		return msg;
	}
	@Override
	public Object getData() {
		return data;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public DefaultServerResult success(String msg, Object data) {
		this.code = 0;
		this.msg = msg;
		this.data = data;
		return this;
	}
	public DefaultServerResult success(Object data) {
		return success("ok", data);
	}
	public DefaultServerResult error(String msg, Object data, int status) {
		this.status = status;
		return error(msg, data);
	}
	public DefaultServerResult error(String msg, int status) {
		return error(msg, null, status);
	}
	public DefaultServerResult error(String msg, Object data) {
		this.code = -1;
		this.msg = msg;
		this.data = data;
		return this;
	}
	public DefaultServerResult error(Object data) {
		return error("error", data);
	}
	@Override
	public String toString() {
		return "DefaultResponse [code=" + code + ", status=" + status + ", msg=" + msg + ", data=" + data + "]";
	}

}
