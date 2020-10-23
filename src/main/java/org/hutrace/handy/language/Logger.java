package org.hutrace.handy.language;

/**
 * <p>使用语言包的日志
 * <p>使用该类可以直接传入语言包的key进行调用
 * <pre>
 *  栗子: 
 *    Logger log = LoggerFactory.getLogger(this.getClass());
 *    log.debug("语言包中对应的名称key");
 *    log.error("语言包中对应的名称key", "对应值包含的变量", "变量对应的值");
 *    log.info("语言包中对应的名称key", "对应值包含的变量数组", "变量对应的值数组");
 * <pre>
 * @author hu trace
 */
public class Logger {
	
	/**
	 * @see org.slf4j.Logger#ROOT_LOGGER_NAME
	 */
	public static final String ROOT_LOGGER_NAME = "ROOT";
	
	/**
	 * @see org.slf4j.Logger
	 */
	protected org.slf4j.Logger log;
	
	Logger(org.slf4j.Logger log) {
		this.log = log;
	}

	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#debug(String)
	 */
	public void debug(String msg) {
		log.debug(SystemProperty.get(msg));
	}
	
	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param use 是否使用语语言包
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#debug(String)
	 */
	public void debug(String msg, boolean use) {
		if(use) {
			log.debug(SystemProperty.get(msg));
		}else {
			log.debug(msg);
		}
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#debug(String, Object)
	 */
	public void debug(String msg, Object arg) {
		log.debug(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#debug(String, Object...)
	 */
	public void debug(String msg, Object... arg) {
		log.debug(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#debug(String, Throwable)
	 */
	public void debug(String msg, Throwable t) {
		log.debug(SystemProperty.get(msg), t);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @param arg 值
	 * @see SystemProperty#get(String, String...)
	 * @see org.slf4j.Logger#debug(String, Throwable)
	 */
	public void debug(String msg, Throwable t, String... arg) {
		log.debug(SystemProperty.get(msg, arg), t);
	}

	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#error(String)
	 */
	public void error(String msg) {
		log.error(SystemProperty.get(msg));
	}
	
	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param use 是否使用语语言包
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#error(String)
	 */
	public void error(String msg, boolean use) {
		if(use) {
			log.error(SystemProperty.get(msg));
		}else {
			log.error(msg);
		}
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#error(String, Object)
	 */
	public void error(String msg, Object arg) {
		log.error(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#error(String, Object...)
	 */
	public void error(String msg, Object... arg) {
		log.error(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#error(String, Throwable)
	 */
	public void error(String msg, Throwable t) {
		log.error(SystemProperty.get(msg), t);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @param arg 值
	 * @see SystemProperty#get(String, String...)
	 * @see org.slf4j.Logger#error(String, Throwable)
	 */
	public void error(String msg, Throwable t, String... arg) {
		log.error(SystemProperty.get(msg, arg), t);
	}

	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#info(String)
	 */
	public void info(String msg) {
		log.info(SystemProperty.get(msg));
	}
	
	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param use 是否使用语语言包
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#info(String)
	 */
	public void info(String msg, boolean use) {
		if(use) {
			log.info(SystemProperty.get(msg));
		}else {
			log.info(msg);
		}
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#info(String, Object)
	 */
	public void info(String msg, Object arg) {
		log.info(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#info(String, Object...)
	 */
	public void info(String msg, Object... arg) {
		log.info(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#info(String, Throwable)
	 */
	public void info(String msg, Throwable t) {
		log.info(SystemProperty.get(msg), t);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @param arg 值
	 * @see SystemProperty#get(String, String...)
	 * @see org.slf4j.Logger#info(String, Throwable)
	 */
	public void info(String msg, Throwable t, String... arg) {
		log.info(SystemProperty.get(msg, arg), t);
	}

	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#trace(String)
	 */
	public void trace(String msg) {
		log.trace(SystemProperty.get(msg));
	}
	
	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param use 是否使用语语言包
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#trace(String)
	 */
	public void trace(String msg, boolean use) {
		if(use) {
			log.trace(SystemProperty.get(msg));
		}else {
			log.trace(msg);
		}
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#trace(String, Object)
	 */
	public void trace(String msg, Object arg) {
		log.trace(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#trace(String, Object...)
	 */
	public void trace(String msg, Object... arg) {
		log.trace(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#trace(String, Throwable)
	 */
	public void trace(String msg, Throwable t) {
		log.trace(SystemProperty.get(msg), t);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @param arg 值
	 * @see SystemProperty#get(String, String...)
	 * @see org.slf4j.Logger#trace(String, Throwable)
	 */
	public void trace(String msg, Throwable t, String... arg) {
		log.trace(SystemProperty.get(msg, arg), t);
	}

	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#warn(String)
	 */
	public void warn(String msg) {
		log.warn(SystemProperty.get(msg));
	}
	
	/**
	 * 使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param use 是否使用语语言包
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#warn(String)
	 */
	public void warn(String msg, boolean use) {
		if(use) {
			log.warn(SystemProperty.get(msg));
		}else {
			log.warn(msg);
		}
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#warn(String, Object)
	 */
	public void warn(String msg, Object arg) {
		log.warn(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param value 值
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#warn(String, Object...)
	 */
	public void warn(String msg, Object... arg) {
		log.warn(SystemProperty.get(msg), arg);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @see SystemProperty#get(String)
	 * @see org.slf4j.Logger#warn(String, Throwable)
	 */
	public void warn(String msg, Throwable t) {
		log.warn(SystemProperty.get(msg), t);
	}

	/**
	 * <p>使用语言包进行日志打印
	 * @param msg 语言包键值对的名字
	 * @param t 异常
	 * @param arg 值
	 * @see SystemProperty#get(String, String...)
	 * @see org.slf4j.Logger#warn(String, Throwable)
	 */
	public void warn(String msg, Throwable t, String... arg) {
		log.warn(SystemProperty.get(msg, arg), t);
	}
	
	/**
	 * 获取slf4j的logger对象
	 * @return
	 */
	public org.slf4j.Logger getLogger() {
		return log;
	}
	
}
