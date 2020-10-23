package org.hutrace.handy.config;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hutrace.handy.exception.ConfigurationNotFountException;
import org.hutrace.handy.exception.ConfigurationReadException;
import org.hutrace.handy.http.exception.ExceptionHandler;
import org.hutrace.handy.http.explorer.Explorer;
import org.hutrace.handy.http.interceptor.HttpMsgsInterceptor;
import org.hutrace.handy.http.listener.ChannelListener;
import org.hutrace.handy.http.resolver.HttpMsgsResolver;
import org.hutrace.handy.http.result.ResultHandler;
import org.hutrace.handy.http.validate.HttpMsgsValidate;
import org.hutrace.handy.http.validate.ValidateUtils;
import org.hutrace.handy.language.Logger;
import org.hutrace.handy.language.LoggerFactory;
import org.hutrace.handy.language.SystemProperty;
import org.hutrace.handy.loader.Loader;
import org.hutrace.handy.utils.TypeUtils;
import org.hutrace.handy.utils.xml.XMLSerializer;
import org.hutrace.handy.websocket.connect.WebSocketConnect;
import org.hutrace.handy.websocket.resolver.WebSocketMsgsResolver;
import org.xml.sax.SAXException;

/**
 * <p>xml配置解析设置配置信息
 * <pre>
 *  注意事项: 
 *    1. 如果同时设置了value和子（bean）标签，value的优先级最高
 *    2. value的值只能设置简单类型，不能设置JavaBean，如果要设置JavaBean，请使用子（bean）标签
 *    3. 属性标签和bean标签可以互相包含，没有层级限制。
 * </pre>
 * @author hu trace
 *
 */
@SuppressWarnings("unchecked")
public class XMLSetter extends AbstractSetter {

	private Logger log = LoggerFactory.getLogger(XMLSetter.class);
	
	/**
	 * 配置文件路径
	 */
	private String resource;
	
	/**
	 * 配置文件流
	 */
	private InputStream in;
	
	/**
	 * 解析器map
	 */
	private Map<String, Parser> parserMap;
	
	/**
	 * <p>构造方法，传入配置文件路径
	 * <p>根据文件路径读取配置
	 * @param resource 文件路径
	 */
	public XMLSetter(String resource) {
		this(resource, null);
	}
	
	/**
	 * <p>构造方法，传入配置文件路径和文件流
	 * <p>根据文件流读取配置
	 * @param resource 文件路径
	 * @param in 文件流
	 */
	public XMLSetter(String resource, InputStream in) {
		this.resource = resource(resource);
		this.in = in;
		initParserMap();
	}
	
	/**
	 * 初始化解析器map
	 */
	private void initParserMap() {
		parserMap = new HashMap<>(8);
		parserMap.put("basic", new BasicParser());
		parserMap.put("interceptors", new InterceptorParser());
		parserMap.put("loaders", new LoaderParser());
		parserMap.put("exception-handler", new ExceptionHandlerParser());
		parserMap.put("result-handler", new ResultHandlerParser());
		parserMap.put("listeners", new ListenersParser());
		parserMap.put("explorers", new ExplorersParser());
		parserMap.put("resolvers", new ResolverParser());
		parserMap.put("validate", new ValidateParser());
		parserMap.put("ws-resolvers", new WebSocketResolverParser());
		parserMap.put("ws-validate", new WebSocketValidateParser());
		parserMap.put("ws-connect", new WebSocketConnectParser());
	}

	@Override
	public void parse() throws ConfigurationReadException, ConfigurationNotFountException {
		Document root;
		if(in == null) {
			root = readXmlToDocument(resource);
		}else {
			root = readXmlToDocument(in);
		}
		Element element = root.getRootElement();
		List<Element> elems = element.elements();
		for(Element elem : elems) {
			parserMap.get(elem.getName()).parse(elem);
		}
	}
	
	/**
	 * 读取配置文件，转为Document对象
	 * @param resource
	 * @return
	 * @throws ConfigurationNotFountException
	 * @throws ConfigurationReadException
	 */
	public Document readXmlToDocument(String resource) throws ConfigurationNotFountException, ConfigurationReadException {
		log.debug("serve.config.read", "xml", resource);
		if(in == null) {
			in = this.getClass().getResourceAsStream(resource);
		}
		if(in == null) {
			throw new ConfigurationNotFountException(SystemProperty.get("serve.config.custom.notfound", resource));
		}
		return readXmlToDocument(in);
	}

	/**
	 * 读取配置文件，转为Document对象
	 * @param in
	 * @return
	 * @throws ConfigurationReadException
	 */
	public Document readXmlToDocument(InputStream in) throws ConfigurationReadException {
		XMLSerializer xml = new XMLSerializer();
		SAXReader reader;
		try {
			reader = xml.buildReader();
		}catch (SAXException e) {
			e.printStackTrace();
			throw new ConfigurationReadException(e.getMessage(), e);
		}
		try {
			return reader.read(in);
		}catch (DocumentException e) {
			e.printStackTrace();
			throw new ConfigurationReadException(e.getMessage(), e);
		}
	}
	
	/**
	 * 解析XML的公共接口
	 * @author hu trace
	 *
	 */
	private interface Parser {
		
		void parse(Element elem) throws ConfigurationReadException;
		
	}
	
	/**
	 * 解析XML各种高级标签的公共抽象类
	 * @author hu trace
	 * @param <T>
	 */
	private abstract class AbstraceParser<T> implements Parser {
		
		/**
		 * 各种高级bean的储存集合
		 */
		private List<T> list = new ArrayList<>();
		
		/**
		 * 解析高级父级标签
		 */
		public void parseList(Element root) throws ConfigurationReadException {
			List<Element> elems = root.elements();
			if(elems.size() > 0) {
				for(int i = 0; i < elems.size(); i++) {
					parseOne(elems.get(i));
				}
			}
			append(list);
		}
		
		/**
		 * 解析高级子标签
		 * @param root
		 * @throws ConfigurationReadException
		 */
		public void parseOne(Element root) throws ConfigurationReadException {
			String className = root.attributeValue("class");
			Class<?> clazs;
			Object instance;
			try {
				clazs = Class.forName(className);
				instance = clazs.newInstance();
			}catch (Exception e) {
				throw new ConfigurationReadException(root.asXML(), e);
			}
			T t;
			try {
				t = (T) instance;
			}catch (ClassCastException e) {
				String tName = ((ParameterizedType) this.getClass().getGenericSuperclass())
						.getActualTypeArguments()[0].getTypeName();
				throw new ConfigurationReadException(SystemProperty.get(
						"serve.config.type.error", className, tName) +
						"\r\n" + root.asXML());
			}
			List<Element> elems = root.elements();
			if(elems.size() > 0) {
				for(int i = 0; i < elems.size(); i++) {
					parseProperty(elems.get(i).elements(), elems.get(i), clazs, instance);
				}
			}
			list.add(t);
		}
		
		/**
		 * <p>解析标签的属性
		 * <p>根据属性名称调用对应的setter方法，此处是调用方法，所以哪怕是Java类中不存在属性也没关系，必须存在setter方法
		 * <p>如果属性标签包含value值，则直接将value值作为参数调用setter方法
		 * <p>如果不包含value值，则会检查子（bean）标签，如果存在，则调用解析beans方法进行映射绑定
		 * <p>如果属性标签的value值和子（bean）标签都不存在，则会抛出异常
		 * <pre>
		 *  注意事项: 
		 *    1. 如果同时设置了value和子（bean）标签，value的优先级最高
		 *    2. value的值只能设置简单类型，不能设置JavaBean，如果要设置JavaBean，请使用子（bean）标签
		 * </pre>
		 * @param elems 子（bean）标签
		 * @param self 当前属性标签
		 * @param clazs 当前属性标签所属的class
		 * @param instance 当前属性标签所属的class实例
		 * @throws ConfigurationReadException
		 * <p> 1. 如果属性标签的value值和子（bean）标签都不存在。
		 * <p> 2. 如果setter方法不存在或调用失败。
		 * <p> 3. 如果value值不是简单类型。
		 */
		private void parseProperty(List<Element> elems, Element self, Class<?> clazs,
				Object instance) throws ConfigurationReadException {
			String name = self.attributeValue("name");
			String value = self.attributeValue("value");
			name = "set" + String.valueOf(name.charAt(0)).toUpperCase() + name.substring(1);
			Method[] methods = clazs.getDeclaredMethods();
			for(Method method : methods) {
				if(method.getName().equals(name)) {
					Class<?>[] type = method.getParameterTypes();
					if(value != null) {
						if(TypeUtils.simpleType(type[0])) {
							try {
								method.invoke(instance, ValidateUtils.cast(value, type[0], null));
							}catch (Exception e) {
								throw new ConfigurationReadException(self.asXML(), e);
							}
						}else {
							throw new ConfigurationReadException(SystemProperty.get(
									"serve.config.method.parameter", clazs.getName(), name) +
									"\r\n" + self.asXML());
						}
					}else {
						if(elems.size() == 0) {
							throw new ConfigurationReadException(SystemProperty.get("serve.config.read.property") +
									"\r\n" + self.asXML());
						}
						String n = elems.get(0).getName();
						Object obj;
						if(n.equals("value")) {
							obj = parseValues(elems, type[0]);
						}else if(n.equals("bean")) {
							obj = parseBeans(elems, type[0]);
						}else {
							throw new ConfigurationReadException(SystemProperty.get("serve.config.elem.nonsupport") +
									"\r\n" + self.asXML());
						}
						if(obj == null) {
							throw new ConfigurationReadException(SystemProperty.get(
									"serve.config.read.bean", self.attributeValue("name")) +
									"\r\n" + self.asXML());
						}
						try {
							method.invoke(instance, obj);
						}catch (Exception e) {
							throw new ConfigurationReadException(self.asXML(), e);
						}
					}
				}
			}
		}
		
		/**
		 * <p>开始解析子（bean）标签集合
		 * <p>当属性标签没有value属性且有子标签（bean）时，开始解析子（bean）标签
		 * <p>如果存在多个子标签，你可是使用List或者数组来接收此属性
		 * <p>如果只有一个子（bean）标签，你就使用class属性对应的类进行接收
		 * <p>方法会根据你的setter方法对应的参数类型来决定使用List或数组进行传参调用
		 * <pre>
		 *  注意事项:
		 *    如果是多个子（bean）标签，你需要保证每个子（bean）标签的class属性是相同的类（至少是所属相同的接口或类）
		 * </pre>
		 * @param elems 子（bean）标签集合
		 * @param clazs 需要调用setter方法传入的参数的类型
		 * @return 
		 * <p> 返回子（bean）标签class属性对应的class实例
		 * <p> 如果有多个子（bean）标签，但setter的参数类型不是List或数组时，会返回null
		 * @throws ConfigurationReadException 当构造实例失败时
		 */
		private Object parseBeans(List<Element> elems, Class<?> clazs) throws ConfigurationReadException {
			if(clazs.isArray()) {
				Object objs = Array.newInstance(clazs.getComponentType(), elems.size());
				for(int i = 0; i < elems.size(); i++) {
					Array.set(objs, i, parseBean(elems.get(i)));
				}
				return objs;
			}else if(clazs.isAssignableFrom(List.class)) {
				List<Object> list = new ArrayList<>();
				for(int i = 0; i < elems.size(); i++) {
					list.add(parseBean(elems.get(i)));
				}
				return list;
			}else if(elems.size() == 1) {
				return parseBean(elems.get(0));
			}
			return null;
		}
		
		/**
		 * <p>解析单个子（bean）标签
		 * <p>如果bean标签还包含属性标签，则会调用{@link #parseProperty(List, Element, Class, Object)}继续执行
		 * @param root 子（bean）标签
		 * @return 返回当前bean标签class属性的实例
		 * @throws ConfigurationReadException
		 * <p> 1. 实例化class属性失败时
		 * <p> 2. 调用{@link #parseProperty(List, Element, Class, Object)}所产生的
		 */
		private Object parseBean(Element root) throws ConfigurationReadException {
			if(!root.getName().equals("bean")) {
				throw new ConfigurationReadException(SystemProperty.get("serve.config.elem.disunion") +
						"\r\n" + root.asXML());
			}
			String className = root.attributeValue("class");
			Class<?> clazs;
			Object instance;
			try {
				clazs = Class.forName(className);
				instance = clazs.newInstance();
			}catch (Exception e) {
				throw new ConfigurationReadException(root.asXML(), e);
			}
			List<Element> elems = root.elements();
			if(elems.size() > 0) {
				for(int i = 0; i < elems.size(); i++) {
					parseProperty(elems.get(i).elements(), elems.get(i), clazs, instance);
				}
			}
			return instance;
		}
		
		private Object parseValues(List<Element> elems, Class<?> clazs) throws ConfigurationReadException {
			if(clazs.isArray()) {
				Class<?> comType = clazs.getComponentType();
				Object objs = Array.newInstance(comType, elems.size());
				for(int i = 0; i < elems.size(); i++) {
					Array.set(objs, i, parseValue(elems.get(i), comType));
				}
				return objs;
			}else if(clazs.isAssignableFrom(List.class)) {
				throw new ConfigurationReadException(SystemProperty.get("serve.config.cast.list"));
			}else if(elems.size() == 1) {
				return parseValue(elems.get(0), clazs);
			}
			return null;
		}
		
		private Object parseValue(Element root, Class<?> clazs) throws ConfigurationReadException {
			if(!root.getName().equals("value")) {
				throw new ConfigurationReadException(SystemProperty.get("serve.config.elem.disunion") +
						"\r\n" + root.asXML());
			}
			String val = root.attributeValue("value");
			if(val == null) {
				val = root.getStringValue();
			}
			return com.alibaba.fastjson.util.TypeUtils.cast(val, clazs, null);
		}
		
		protected void append() throws ConfigurationReadException {
			append(list);
		}
		
		/**
		 * 抽象方法，继承者实现此方法，向{@link Configuration}中写入配置
		 * @param list
		 */
		abstract void append(List<T> list) throws ConfigurationReadException;
	}
	
	/**
	 * 解析XML各种高级标签的公共抽象类
	 * <p>解析单标签的专用公共类
	 * @author hu trace
	 * @param <T>
	 */
	private abstract class AbstraceOneParser<T> extends AbstraceParser<T> {
		@Override
		public void parse(Element root) throws ConfigurationReadException {
			parseOne(root);
			append();
		}
	}
	
	/**
	 * 解析XML各种高级标签的公共抽象类
	 * <p>解析多标签的专用公共类
	 * @author hu trace
	 * @param <T>
	 */
	private abstract class AbstraceListParser<T> extends AbstraceParser<T> {
		
		@Override
		public void parse(Element root) throws ConfigurationReadException {
			parseList(root);
		}
		
	}
	
	/**
	 * Loader加载器解析
	 * @author hu trace
	 *
	 */
	private class LoaderParser extends AbstraceListParser<Loader> {

		@Override
		void append(List<Loader> list) throws ConfigurationReadException {
			setLoaders(list);
		}
		
	}
	
	/**
	 * 异常处理器解析
	 * @author hu trace
	 *
	 */
	private class ExceptionHandlerParser extends AbstraceOneParser<ExceptionHandler> {

		@Override
		void append(List<ExceptionHandler> list) throws ConfigurationReadException {
			setExceptionHandler(list.get(0));
		}
		
	}
	
	/**
	 * HTTP响应数据处理器
	 * @author hu trace
	 *
	 */
	private class ResultHandlerParser extends AbstraceOneParser<ResultHandler> {

		@Override
		void append(List<ResultHandler> list) throws ConfigurationReadException {
			setResultHandler(list.get(0));
		}
		
	}
	
	/**
	 * 监听器解析
	 * @author hu trace
	 *
	 */
	private class ListenersParser extends AbstraceListParser<ChannelListener> {

		@Override
		void append(List<ChannelListener> list) throws ConfigurationReadException {
			setListeners(list);
		}
		
	}
	
	/**
	 * 资源管理器解析
	 * @author hu trace
	 *
	 */
	private class ExplorersParser extends AbstraceListParser<Explorer> {

		@Override
		void append(List<Explorer> list) throws ConfigurationReadException {
			setExplorers(list);
		}
		
	}
	
	/**
	 * HTTP拦截器解析
	 * @author hu trace
	 *
	 */
	private class InterceptorParser extends AbstraceListParser<HttpMsgsInterceptor> {

		@Override
		void append(List<HttpMsgsInterceptor> list) throws ConfigurationReadException {
			setInterceptors(list);
		}
		
	}
	
	/**
	 * HTTP消息解析器解析
	 * @author hu trace
	 *
	 */
	private class ResolverParser extends AbstraceListParser<HttpMsgsResolver> {

		@Override
		void append(List<HttpMsgsResolver> list) throws ConfigurationReadException {
			setResolvers(list);
		}
		
	}
	
	/**
	 * HTTP参数验证器解析
	 * @author hu trace
	 *
	 */
	private class ValidateParser extends AbstraceOneParser<HttpMsgsValidate> {

		@Override
		void append(List<HttpMsgsValidate> list) throws ConfigurationReadException {
			setValidate(list.get(0));
		}
		
	}
	
	/**
	 * WebSocket消息解析器解析
	 * @author hu trace
	 *
	 */
	private class WebSocketResolverParser extends AbstraceListParser<WebSocketMsgsResolver> {

		@Override
		void append(List<WebSocketMsgsResolver> list) throws ConfigurationReadException {
			setWsResolvers(list);
		}
		
	}
	
	/**
	 * HTTP参数验证器解析
	 * @author hu trace
	 *
	 */
	private class WebSocketValidateParser extends AbstraceOneParser<HttpMsgsValidate> {

		@Override
		void append(List<HttpMsgsValidate> list) throws ConfigurationReadException {
			setWsValidate(list.get(0));
		}
		
	}

	/**
	 * HTTP参数验证器解析
	 * @author hu trace
	 *
	 */
	private class WebSocketConnectParser extends AbstraceOneParser<WebSocketConnect> {

		@Override
		void append(List<WebSocketConnect> list) throws ConfigurationReadException {
			setWsConnect(list.get(0));
		}
		
	}
	
	/**
	 * 基本信息数据解析
	 * @author hu trace
	 *
	 */
	private class BasicParser implements Parser {

		@Override
		public void parse(Element root) throws ConfigurationReadException {
			List<Element> elems = root.elements();
			if(elems.size() > 0) {
				Element elem;
				for(int i = 0; i < elems.size(); i++) {
					elem = elems.get(i);
					parse(elem.getName(), elem);
				}
			}
		}
		
		private void parse(String name, Element elem) throws ConfigurationReadException {
			if(name.equals("name")) {
				setName(elem.attributeValue("value"));
			}else if(name.equals("port")) {
				setPort(Integer.parseInt(elem.attributeValue("value")));
			}else if(name.equals("appVersion")) {
				setAppVersion(elem.attributeValue("value"));
			}else if(name.equals("pattern")) {
				setPattern(Pattern.valueOf(elem.attributeValue("value")));
			}else if(name.equals("charset")) {
				setCharset(elem.attributeValue("value"));
			}else if(name.equals("max-content-length")) {
				setMaxContentLength(Integer.parseInt(elem.attributeValue("value")));
			}else if(name.equals("thread-pools")) {
				setThreadPools(Integer.parseInt(elem.attributeValue("value")));
			}else if(name.equals("thread-name")) {
				setThreadName(elem.attributeValue("value"));
			}else if(name.equals("common-dao")) {
				setCommonDao(elem.attributeValue("value"));
			}else if(name.equals("scan")) {
				List<Element> elems = elem.elements();
				if(elems.size() == 0) {
					throw new ConfigurationReadException(SystemProperty.get("serve.config.scan.nothave") +
							"\r\n" + elem.asXML());
				}
				String[] arr = new String[elems.size()];
				for(int i = 0; i < arr.length; i++) {
					arr[i] = elems.get(i).attributeValue("value");
				}
				setScan(arr);
			}
		}
		
	}
	
}
