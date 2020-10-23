package org.hutrace.handy.exception;

/**
 * <p>扫描应用程序中包（package）时抛出的异常
 * <p>此异常用于扫描包出错时抛出
 * @author hutrace
 * @since 1.8
 * @version 1.0
 * @time 2019年3月22日
 */
@SuppressWarnings("serial")
public class ScanningApplicationException extends AppLoaderException {
	
	public ScanningApplicationException(Throwable e) {
		super(e);
	}
	
	public ScanningApplicationException(String msg) {
		super(msg);
	}
	
	public ScanningApplicationException(String msg, Throwable e) {
		super(msg, e);
	}
	
}
