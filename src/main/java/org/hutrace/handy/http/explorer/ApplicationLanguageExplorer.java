package org.hutrace.handy.http.explorer;

import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.http.exception.ExceptionBean;
import org.hutrace.handy.language.ApplicationProperty;
import org.hutrace.handy.language.LanguageLoader;
import org.hutrace.handy.server.ServerRequest;
import org.hutrace.handy.server.ServerResponse;

/**
 * 程序语言包资源管理器，通过它开启与关闭语言资源
 * @author hu trace
 *
 */
public class ApplicationLanguageExplorer implements Explorer {
	
	public ApplicationLanguageExplorer() {
		LanguageLoader.instance.setUseAppLanguage(true);
	}

	/**
	 * 开启语言包资源
	 * <p>通过向当前线程绑定对应的语言包
	 */
	@Override
	public void open(ServerRequest request, ServerResponse response) {
		Object obj = request.parameter("language");
		String language = null;
		if(obj != null) {
			try {
				language = LanguageLoader.disposeAppLanguageName(obj.toString(), null);
			}catch (AppLoaderException e) {}
		}
		ApplicationProperty.setThreadMap(language);
	}

	/**
	 * 因为框架使用线程池的模式
	 * <p>线程会复用
	 * <p>避免资源浪费，在此处需要清除
	 */
	@Override
	public void close(ServerRequest request, ServerResponse response, ExceptionBean eb) {
		ApplicationProperty.clearThreadMap();
	}

}
