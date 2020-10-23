package org.hutrace.handy.http.result;

import com.alibaba.fastjson.JSONObject;

public class JSONResultBean implements ResultBean {
	
	private JSONObject json;
	
	private String contentType = "application/json";
	
	public JSONResultBean() {
		json = new JSONObject();
	}

	@Override
	public ResultBean setReqId(String reqId) {
		json.put("reqId", reqId);
		return this;
	}

	@Override
	public ResultBean setRspId(String rspId) {
		json.put("rspId", rspId);
		return this;
	}

	@Override
	public ResultBean setCode(int code) {
		json.put("code", code);
		return this;
	}

	@Override
	public ResultBean setMsg(String msg) {
		json.put("msg", msg);
		return this;
	}

	@Override
	public ResultBean setData(Object data) {
		json.put("data", data);
		return this;
	}

	@Override
	public ResultBean setTimestamp(long timestamp) {
		json.put("timestamp", timestamp);
		return this;
	}

	@Override
	public ResultBean setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	@Override
	public String getString() {
		return json.toString();
	}

	@Override
	public String toString() {
		return getString();
	}

	@Override
	public String contentType() {
		return contentType;
	}

	@Override
	public Object inner() {
		return json;
	}
	
}
