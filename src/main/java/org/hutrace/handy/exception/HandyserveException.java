package org.hutrace.handy.exception;

/**
 * <p>公共异常类
 * <p>可定义code、msg
 * <p>继承{@link Exception}
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class HandyserveException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 异常码，可用于判断错误类型
	 */
	private int code = -1;
	
	/**
	 * <p>异常构造方法
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 */
	public HandyserveException(String msg) {
		super(msg);
	}
	
	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 */
	public HandyserveException(int code, String msg) {
		this(msg);
		this.code = code;
	}

	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 * @param e 其它异常
	 */
	public HandyserveException(int code, String msg, Throwable e) {
		super(msg, e);
		this.code = code;
	}

	/**
	 * <p>异常构造方法
	 * @param msg 异常信息，可通过{@link HandyserveException#getMessage()}获取
	 * @param e 其它异常
	 */
	public HandyserveException(String msg, Throwable e) {
		super(msg, e);
	}

	/**
	 * <p>异常构造方法
	 * @param code 异常码，可通过{@link #code()}获取
	 * @param e 其它异常
	 */
	public HandyserveException(int code, Throwable e) {
		super(e);
		this.code = code;
	}

	/**
	 * <p>异常构造方法
	 * @param e 其它异常
	 */
	public HandyserveException(Throwable e) {
		super(e);
	}
	
	/**
	 * 获取异常码，可用于判断错误类型
	 * @return 异常码
	 */
	public int code() {
		return code;
	}
	
}
