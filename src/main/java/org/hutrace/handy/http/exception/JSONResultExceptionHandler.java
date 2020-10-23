package org.hutrace.handy.http.exception;

import java.lang.reflect.InvocationTargetException;

import org.hutrace.handy.exception.CustomException;
import org.hutrace.handy.exception.HandyserveException;
import org.hutrace.handy.exception.ValidateRuntimeException;
import org.hutrace.handy.http.HttpRequest;
import org.hutrace.handy.http.HttpResponse;
import org.hutrace.handy.http.ResponseCode;
import org.hutrace.handy.http.result.JSONResultBean;
import org.hutrace.handy.http.result.ResultBean;

/**
 * 异常处理器
 * <p>将异常信息响应为JSON类型数据
 * @author hu trace
 *
 */
public class JSONResultExceptionHandler implements ExceptionHandler {
	
	/**
	 * 此处直接构造{@link JSONResultBean}实例进行返回
	 */
	@Override
	public Object dispose(HttpRequest request, HttpResponse response, ExceptionBean ex) {
		ResultBean rb = new JSONResultBean();
		Throwable e = ex.getEx();
		if(e instanceof InvocationTargetException) {
			e = ((InvocationTargetException) e).getTargetException();
		}
		if(e instanceof ValidateRuntimeException) {
			rb.setMsg(e.getMessage());
			rb.setCode(ResponseCode.ERROR_PARAMETER);
		}else if(e instanceof HandyserveException) {
			rb.setMsg(e.getMessage());
			rb.setCode(((HandyserveException) e).code());
		}else if(e instanceof CustomException) {
			rb.setMsg(e.getMessage());
			rb.setCode(((CustomException) e).getCode());
		}else {
			rb.setMsg(ex.getMsg());
			rb.setCode(ex.getCode());
		}
		rb.setData(null);
		rb.setTimestamp(System.currentTimeMillis());
		rb.setReqId(request.id());
		rb.setRspId(response.id());
		return rb;
	}
	
}
