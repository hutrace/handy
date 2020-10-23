package org.hutrace.handy.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.hutrace.handy.exception.AppLoaderException;

/**
 * <p>系统语言包属性类
 * <p>可调用<code>get()</code>方法获取语言包的值
 * <pre>
 *  栗子: 
 *  SystemProperty.get(name)
 * </pre>
 * @author hutrace
 * @since 1.8
 * @version 1.0
 */
public class SystemProperty {
	
	private static final HashMap<SystemLanguage, String> LANGUAGE_MAP = new HashMap<>();
	
	static {
		String className = SystemProperty.class.getName().replace("SystemProperty", "").replace(".", "/");
		LANGUAGE_MAP.put(SystemLanguage.EN_US, className + "SYSTEM.en-US.txt");
		LANGUAGE_MAP.put(SystemLanguage.ZH_CN, className + "SYSTEM.zh-CN.txt");
	}
	
	public static InputStream getFileInputStream(String path) {
		InputStream is = SystemProperty.class.getClassLoader().getResourceAsStream(path);
		if(is == null) {
			path = path.replace('\\', File.separatorChar).replace('/', File.separatorChar);
			try {
				if(path.charAt(0) == File.separatorChar) {
					is = new FileInputStream(new File(System.getProperty("user.dir") + path));
				}else {
					is = new FileInputStream(new File(System.getProperty("user.dir") + File.separator + path));
				}
			}catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return is;
	}
	
	/**
	 * 使用的语言包
	 */
	SystemLanguage language = SystemLanguage.EN_US;
	
	/**
	 * 自定义的语言包
	 */
	private InputStream in;
	
	/**
	 * 系统语言包缓存
	 * TODO -- 优化方向
	 * <p>此处暂时使用HashMap来作为缓存，后续可优化。
	 * <p>所有HashMap的key可以作为变量存放，避免多次在HashMap中取值
	 * <p>目前考虑到系统语言包内容不多，就先使用HashMap了
	 */
	private static final HashMap<String, String> BUFFER = new HashMap<>();
	
	/**
	 * 根据name获取语言包中的值
	 * @param name
	 * @return 值
	 */
	public static String get(String name) {
		String res = BUFFER.get(name);
		if(res == null) {
			throw new NullPointerException(get("serve.language.notfound", name));
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
	
	/**
	 * <p>语言包枚举构造函数
	 * <p>此构造函数只能选择框架自带的语言包
	 * @param language
	 */
	public SystemProperty(SystemLanguage language) {
		this.language = language;
	}
	
	/**
	 * <p>语言包枚举构造函数
	 * <p>此构造函数需要自定义语言包，并将文件流传入
	 * @param in
	 */
	public SystemProperty(InputStream in) {
		this.in = in;
	}
	
	/**
	 * <p>语言包枚举构造函数
	 * <p>无参构造，使用默认语言包，默认是{@link SystemLanguage#EN_US}
	 */
	public SystemProperty() {}
	
	/**
	 * <p>初始化
	 * <p>开始加载语言包
	 * @throws AppLoaderException
	 * @throws IOException
	 */
	public void init() throws AppLoaderException {
		if(in != null) {
			read(in);
		}else {
			read(language);
		}
	}
	
	/**
	 * <p>根据枚举读取系统语言包
	 * @param language
	 * @throws IOException
	 * @throws AppLoaderException
	 */
	private void read(SystemLanguage language) throws AppLoaderException {
		read(this.getClass().getClassLoader().getResourceAsStream(LANGUAGE_MAP.get(language)));
	}
	
	/**
	 * <p>根据IO流读取系统语言包
	 * @param in
	 * @throws AppLoaderException
	 */
	public void read(InputStream in) throws AppLoaderException {
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
							BUFFER.put(key, tokenizer.nextToken().trim());
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
			throw new AppLoaderException(e);
		}finally {
			try {
				br.close();
			}catch (IOException e) {}
		}
	}
	
}
