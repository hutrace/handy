package org.hutrace.handy.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.hutrace.handy.exception.AppLoaderException;

/**
 * 程序的语言属性类
 * <p>集成了程序中使用到的语言包
 * @author hu trace
 */
public class ApplicationProperty {
	
	/**
	 * 语言支持
	 */
	private static Support[] supports;
	
	/**
	 * 默认语言包
	 */
	private static Map<String, String> defaultData;

	/**
	 * 解决多线程访问语言包方法
	 * <p>ps: 这里没有使用ThreadLocal，
	 */
	private static ThreadLocal<Map<String, String>> threadMap = new ThreadLocal<>();
	
	static {
		try {
			initDefaultSuports();
		}catch (AppLoaderException e) {}
	}
	
	/**
	 * 初始化自带语言包
	 * @throws AppLoaderException
	 */
	static synchronized void initDefaultSuports() throws AppLoaderException {
		String className = ApplicationProperty.class.getName().replace("ApplicationProperty", "").replace(".", "/");
		addSupport("zh-CN", className + "APP.zh-CN.txt");
		addSupport("en-US", className + "APP.en-US.txt");
		setDefaultLanguage("zh-CN");
	}
	
	/**
	 * 设置默认使用的语言包（在没有找到对应支持语言包的情况下使用）
	 * @param language
	 * @throws AppLoaderException
	 */
	public static void setDefaultLanguage(String language) throws AppLoaderException {
		Map<String, String> data = getSupport(language);
		if(data == null) {
			throw new AppLoaderException("没有找到语言包，请先使用'#addSupport()'方法设置语言包");
		}
		defaultData = data;
	}
	
	/**
	 * 添加语言包支持
	 * <p>需要注意的是，这里是使用ClassLoader#getResourceAsStream(path)来获取文件的
	 * <p>如果已经加载过语言包，将会合并至原本的语言包中
	 * <p>合并时如果遇见新的属性，会直接替换原来的属性，新属性生效
	 * @param language
	 * @param path
	 * @throws AppLoaderException
	 */
	public static void addSupport(String language, String path) throws AppLoaderException {
		addSupport(language, SystemProperty.getFileInputStream(path));
	}
	
	/**
	 * 添加语言包支持
	 * <p>如果已经加载过语言包，将会合并至原本的语言包中
	 * <p>合并时如果遇见新的属性，会直接替换原来的属性，新属性生效
	 * @param language
	 * @param in
	 * @throws AppLoaderException
	 */
	public static void addSupport(String language, InputStream in) throws AppLoaderException {
		Map<String, String> data = getSupport(language);
		if(data == null) {
			data = new HashMap<>();
			addSupport(language, data);
		}
		try {
			put(data, in);
		}catch (IOException e) {
			throw new AppLoaderException(e);
		}
	}
	
	/**
	 * 添加语言包支持
	 * <p>如果已经加载过语言包，将会合并至原本的语言包中
	 * <p>合并时如果遇见新的属性，会直接替换原来的属性，新属性生效
	 * @param language
	 * @param data
	 */
	public static void addSupport(String language, Map<String, String> data) {
		Support support = new Support();
		support.name = language;
		support.data = data;
		if(supports == null) {
			supports = new Support[1];
			supports[0] = support;
		}else {
			Support[] newSupports = new Support[supports.length + 1];
			System.arraycopy(supports, 0, newSupports, 0, supports.length);
			newSupports[supports.length] = support;
			supports = newSupports;
		}
	}
	
	/**
	 * 向语言包中合并新的语言
	 * <p>如果已经包含了某个属性，会直接替换
	 * @param data
	 * @param in
	 * @throws AppLoaderException
	 * @throws IOException
	 */
	private static void put(Map<String, String> data, InputStream in) throws AppLoaderException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		char identifies = '#';
		String split = "=";
		StringTokenizer tokenizer;
		String key = null;
		int lineNum = 0;
		try {
			while((line = br.readLine()) != null) {
				if(!line.isEmpty() && !line.trim().isEmpty() && line.charAt(0) != identifies) {
					tokenizer = new StringTokenizer(line, split);
					while(tokenizer.hasMoreElements()) {
						if(key == null) {
							key = tokenizer.nextToken().trim();
						}else {
							data.put(key, tokenizer.nextToken().trim());
							key = null;
							break;
						}
					}
					if(key != null) {
						throw new AppLoaderException("The wrong line [" + lineNum + "] : " + key);
					}
					lineNum ++;
				}
			}
		}catch (IOException e) {
			throw e;
		}finally {
			br.close();
		}
	}
	
	/**
	 * 获取默认语言包
	 * @return
	 */
	public static Map<String, String> getDefault() {
		return defaultData;
	}
	
	/**
	 * 根据语言获取语言包
	 * <p>如果没有找到语言包，将会返回null
	 * <p>你可以再使用{@link #getDefault()}来获取默认包使用
	 * @param language
	 * @return
	 * @throws AppLoaderException
	 */
	public static Map<String, String> getSupport(String language) throws NullPointerException {
		if(language == null) {
			throw new NullPointerException("The parameter 'name' cannot be null");
		}
		if(supports == null) {
			return null;
		}
		for(int i = 0; i < supports.length; i++) {
			if(supports[i].name.equals(language)) {
				return supports[i].data;
			}
		}
		return null;
	}
	
	/**
	 * 设置当前线程（请求）的语言包
	 * @param language
	 */
	public static void setThreadMap(String language) {
		Map<String, String> data = null;
		if(language == null || language.isEmpty()) {
			data = getDefault();
		}
		if(data == null) {
			data = getSupport(language);
		}
		if(data == null) {
			data = getDefault();
		}
		threadMap.set(data);
	}
	
	/**
	 * 清除当前线程（请求）的语言包
	 */
	public static void clearThreadMap() {
		threadMap.remove();
	}
	
	/**
	 * 获取当前线程（请求）的语言包
	 * @return
	 */
	public static Map<String, String> get() {
		Map<String, String> map = threadMap.get();
		return map == null ? getDefault() : map;
	}
	
	/**
	 * 根据name获取语言包中的值
	 * @param name
	 * @return 值
	 */
	public static String get(String name) {
		String res = get().get(name);
		if(res == null) {
			throw new NullPointerException(SystemProperty.get("serve.language.notfound", name));
		}
		return res;
	}
	
	/**
	 * 根据name获取语言包中的值
	 * <p>并对值做表达式替换
	 * @param name
	 * @param value 表达式对应的值
	 * @return 值
	 */
	public static String get(String name, String... values) {
		StringBuilder val = new StringBuilder(get(name));
		String variable = "{}";
		int index = 0;
		for(int i = 0; i < values.length; i++) {
			index = val.indexOf(variable, index);
			if(index <= -1) {
				break;
			}
			val.delete(index, index + 2);
			val.insert(index, values[i]);
		}
		return val.toString();
	}
	
	static class Support {
		private String name;
		private Map<String, String> data;
	}
	
}
