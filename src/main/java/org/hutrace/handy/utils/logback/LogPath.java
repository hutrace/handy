package org.hutrace.handy.utils.logback;

import ch.qos.logback.core.PropertyDefinerBase;

/**
 * 获取日志文件的储存路径
 * <p>可以设置relative属性，relative默认为"log"
 * <p>返回路径会以当前项目路径拼接relative的值，默认会保留"/"
 * @author hu trace
 */
public class LogPath extends PropertyDefinerBase {
	
	private String absolute;
	
	public void setAbsolute(String absolute) {
		this.absolute = absolute;
	}
	private String relative;
	
	public void setRelative(String relative) {
		this.absolute = relative;
	}
	
	@Override
	public String getPropertyValue() {
		if(relative == null && absolute == null) {
			relative = "log";
		}
		if(relative != null) {
			return (System.getProperty("user.dir").replace("\\", "/") + "/" + relative + "/").replace("//", "/");
		}else {
			return absolute;
		}
	}
	
}
