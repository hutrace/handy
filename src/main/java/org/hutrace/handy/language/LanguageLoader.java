package org.hutrace.handy.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.hutrace.handy.exception.AppLoaderException;
import org.hutrace.handy.loader.Loader;

public class LanguageLoader implements Loader {
	
	private SystemProperty systemProperty;
	
	private final String languageFilePath = "/language.txt";
	
	public static final LanguageLoader instance = new LanguageLoader();
	
	private LanguageLoader() {}
	
	private boolean useAppLanguage = false;
	
	public boolean isUseAppLanguage() {
		return useAppLanguage;
	}

	public void setUseAppLanguage(boolean useAppLanguage) {
		this.useAppLanguage = useAppLanguage;
	}

	/**
	 * <p>设置系统使用的语言包
	 * <p>此方法只能选择框架自带的语言包
	 * @param language
	 */
	public void setSystemDefaultLanguage(SystemLanguage language) {
		systemProperty = new SystemProperty(language);
	}

	/**
	 * <p>设置系统使用的语言包
	 * <p>此方法需要自定义语言包，并将文件流传入
	 * <p><b>注意：语言包文件的格式是a=b回车c=d</b></p>
	 * <pre> 栗子：
	 *  a=b
	 *  c=d
	 * </pre>
	 * <p>语言包中可以写注释，以字符'#'开头的行为注释行
	 * <p>一行内容中不要出现两个等号（=），框架只会解析第一个等号（=），第二个等号以及后面的都会舍去。
	 * <p>值支持表达式（=后面的内容），表达式的格式是#{表达式名}
	 * @param in
	 */
	public void setSystemDefaultLanguage(InputStream in) {
		systemProperty = new SystemProperty(in);
	}
	
	/**
	 * 获取当前设置的系统语言
	 * @return
	 */
	public SystemLanguage getSystemLanguage() {
		return systemProperty.language;
	}
	
	/**
	 * 自定义扩展系统语言
	 * @param path
	 * @throws AppLoaderException
	 */
	public void extendSystem(String path) throws AppLoaderException {
		extendSystem(SystemProperty.getFileInputStream(path));
	}
	
	/**
	 * 自定义扩展系统语言
	 * @param in
	 * @throws AppLoaderException
	 */
	public void extendSystem(InputStream in) throws AppLoaderException {
		systemProperty.read(in);
	}
	
	/**
	 * 自定义扩展程序语言
	 * <p>该方法会根据传入的路径获取语言名称（路径格式必须是xxx/xxx.../xx.zh-CN.txt格式）
	 * @param path
	 * @throws AppLoaderException
	 */
	public void extendApp(String path) throws AppLoaderException {
		String[] arr = path.split("\\.");
		String name;
		try {
			name = arr[arr.length - 2];
		}catch (Exception e) {
			throw new AppLoaderException(SystemProperty.get("serve.language.loadapp.errorname", path), e);
		}
		extendApp(name, path);
	}
	
	/**
	 * 自定义扩展程序语言
	 * @param name
	 * @param path
	 * @throws AppLoaderException
	 */
	public void extendApp(String name, String path) throws AppLoaderException {
		ApplicationProperty.addSupport(disposeAppLanguageName(name, path), path);
	}
	
	/**
	 * 自定义扩展程序语言
	 * @param name
	 * @param in
	 * @throws AppLoaderException
	 */
	public void extendApp(String name, InputStream in) throws AppLoaderException {
		ApplicationProperty.addSupport(disposeAppLanguageName(name, null), in);
	}
	
	@Override
	public void execute() throws AppLoaderException {
		Language language = loadConfigFile();
		if(systemProperty == null) {
			systemProperty = initSystemConfig(language);
		}
		if(systemProperty == null) {
			systemProperty = new SystemProperty();
		}
		systemProperty.init();
		initApplicationProperty(language);
		initSystemExtend(language);
		initAppExtend(language);
	}
	
	/**
	 * 初始化扩展程序语言包
	 * @param language
	 * @throws AppLoaderException
	 */
	private void initAppExtend(Language language) throws AppLoaderException {
		if(language.appExtend != null) {
			String[] arr;
			String name;
			for(String ae : language.appExtend) {
				arr = ae.split("\\.");
				try {
					name = arr[arr.length - 2];
				}catch (Exception e) {
					throw new AppLoaderException(SystemProperty.get("serve.language.loadapp.errorname", ae), e);
				}
				ApplicationProperty.addSupport(disposeAppLanguageName(name, ae), ae);
			}
		}
	}
	
	/**
	 * 将不规则的语言标识（名称）处理为语言框架需要的标准标识（名称）
	 * @param name 语言标识（如: zh-cn/en-us）
	 * @param exceptionMsg 错误信息，外部调用此处传入null即可
	 * @return
	 * @throws AppLoaderException
	 */
	public static String disposeAppLanguageName(String name, String exceptionMsg) throws AppLoaderException {
		String[] nameArr = name.split("-");
		if(nameArr.length != 2) {
			throw new AppLoaderException(SystemProperty.get("serve.language.loadapp.errorname", exceptionMsg));
		}
		return nameArr[0].toLowerCase() + "-" + nameArr[1].toUpperCase();
	}
	
	/**
	 * 初始化系统扩展语言包
	 * @param language
	 * @throws AppLoaderException
	 */
	private void initSystemExtend(Language language) throws AppLoaderException {
		if(language.extend != null) {
			for(String path : language.extend) {
				systemProperty.read(SystemProperty.getFileInputStream(path));
			}
		}
	}
	
	/**
	 * 初始化程序默认使用语言
	 * @param language
	 * @throws AppLoaderException
	 */
	private void initApplicationProperty(Language language) throws AppLoaderException {
		if(language.app != null) {
			ApplicationProperty.setDefaultLanguage(language.app);
		}
	}
	
	/**
	 * 初始化系统默认使用语言
	 * @param language
	 * @return
	 * @throws AppLoaderException
	 */
	private SystemProperty initSystemConfig(Language language) throws AppLoaderException {
		if(language.system != null) {
			try {
				return new SystemProperty(SystemLanguage.valueOf(language.system.replace('-', '_').toUpperCase()));
			}catch (Exception e) {
				throw new AppLoaderException("系统语言错误，请根据org.hutrace.handy.language.SystemLanguage中包含的值设定.", e);
			}
		}
		return null;
	}
	
	/**
	 * 加载语言配置文件配置
	 * @return
	 * @throws AppLoaderException 
	 */
	private Language loadConfigFile() throws AppLoaderException {
		InputStream in = this.getClass().getResourceAsStream(languageFilePath);
		if(in == null) {
			return new Language();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		char identifies = '#';
		String split = "=";
		StringTokenizer tokenizer;
		String key = null;
		String systemId = "SYSTEM";
		String appId = "APP";
		String extendId = "SYSTEM.extend";
		String appExtendId = "APP.extend";
		Language language = new Language();
		try {
			while((line = br.readLine()) != null) {
				if(!line.isEmpty() && !line.trim().isEmpty() && line.charAt(0) != identifies) {
					tokenizer = new StringTokenizer(line, split);
					while(tokenizer.hasMoreElements()) {
						if(key == null) {
							key = tokenizer.nextToken().trim();
						}else {
							if(key.equals(systemId)) {
								language.system = tokenizer.nextToken().trim();
							}else if(key.equals(appId)) {
								language.app = tokenizer.nextToken().trim();
							}else if(key.equals(extendId)) {
								if(language.extend == null) {
									language.extend = new ArrayList<>();
								}
								language.extend.add(tokenizer.nextToken().trim());
							}else if(key.equals(appExtendId)) {
								if(language.appExtend == null) {
									language.appExtend = new ArrayList<>();
								}
								language.appExtend.add(tokenizer.nextToken().trim());
							}
							key = null;
							break;
						}
					}
				}
			}
			return language;
		}catch (IOException e) {
			throw new AppLoaderException(e);
		}finally {
			try {
				br.close();
			}catch (IOException e) {}
		}
	}
	
	private class Language {
		String system = "en-US";
		String app = "en-US";
		List<String> extend = null;
		List<String> appExtend = null;
	}
	
}
