package org.hutrace.handy.utils.xml;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * <p>解析xml数据
 * <p>调用dome4j解析xml
 * <p>详细描述，通常用一段或者多段话来详细描述该类的作用，一般每段话都以英文句号作为结束
 * @author hutrace
 * @see SAXReader
 * @see DocumentHelper
 * @see Document
 * @since 1.8
 * @version 1.0
 */
public class XMLSerializer {
	
	/**
	 * <p>根据netty中的{@link ByteBuf}解析数据
	 * @param buf {@link ByteBuf}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 * @throws DocumentException 当解析数据错误时抛出
	 * @throws SAXException 当初始化解析器错误时抛出
	 */
	public Map<String, Object> parse(ByteBuf buf) throws DocumentException, SAXException {
		return parse(new ByteBufInputStream(buf));
	}
	
	/**
	 * <p>根据byte数组解析数据
	 * @param bytes {@link byte[]}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 * @throws DocumentException 当解析数据错误时抛出
	 * @throws SAXException 当初始化解析器错误时抛出
	 */
	public Map<String, Object> parse(byte[] bytes) throws DocumentException, SAXException {
		return parse(new ByteArrayInputStream(bytes));
	}
	
	/**
	 * <p>根据{@link InputStream}解析数据
	 * @param in {@link InputStream}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 * @throws DocumentException 当解析数据错误时抛出
	 * @throws SAXException 当初始化解析器错误时抛出
	 */
	public Map<String, Object> parse(InputStream in) throws DocumentException, SAXException {
		SAXReader reader = buildReader();
		Document doc = reader.read(in);
		return parse(doc);
	}

	/**
	 * <p>根据字符串{@link String}解析数据
	 * @param text {@link String}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 * @throws DocumentException 当解析数据错误时抛出
	 */
	public Map<String, Object> parse(String text) throws DocumentException {
		Document doc = DocumentHelper.parseText(text);
		return parse(doc);
	}
	
	/**
	 * <p>根据字符串{@link Document}解析数据
	 * @param doc {@link Document}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> parse(Document doc) {
		Map<String, Object> map = new HashMap<>();
		Element root = doc.getRootElement();
		List<Element> elems = root.elements();
		for(Element elem : elems) {
			List<Element> list = elem.elements();
			if(list.size() > 0) {
				Object val = map.get(elem.getName());
				if(val == null) {
					map.put(elem.getName(), parse(elem));
				}else if(val instanceof Collection) {
					((Collection<Object>) val).add(parse(elem));
				}else {
					List<Object> values = new ArrayList<>();
					values.add(val);
					values.add(parse(elem));
					map.put(elem.getName(), values);
				}
			}else {
				Object val = map.get(elem.getName());
				if(val == null) {
					map.put(elem.getName(), elem.getText());
				}else if(val instanceof Collection) {
					((Collection<Object>) val).add(elem.getText());
				}else {
					List<Object> values = new ArrayList<>();
					values.add(val);
					values.add(elem.getText());
					map.put(elem.getName(), values);
				}
			}
		}
		return map;
	}
	
	/**
	 * <p>根据字符串{@link Element}解析数据
	 * @param doc {@link Element}
	 * @return 解析好的{@link Map}, 数据中可能会有多层
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> parse(Element e) {
		Map<String, Object> map = new HashMap<>();
		List<Element> list = e.elements();
		if(list.size() > 0) {
			ArrayList<Object> mapl = new ArrayList<Object>();
			for(Element elem : list) {
				addList(map, elem, mapl);
			}
		}
		return map;
	}
	
	private void addList(Map<String, Object> map, Element elem, List<Object> mapl) {
		Map<String, Object> value = parse(elem);
		if(elem.elements().size() > 0) {
			if(map.get(elem.getName()) != null) {
				addList(map, value, elem, mapl);
			}else {
				map.put(elem.getName(), value);
			}
		}else {
			if(map.get(elem.getName()) != null) {
				addList(map, elem.getText(), elem, mapl);
			}else {
				map.put(elem.getName(), elem.getText());
			}
		}
	}
	
	@SuppressWarnings({"unchecked", "rawtypes" })
	private void addList(Map<String, Object> map, Object value, Element elem, List<Object> mapl) {
		Object obj = map.get(elem.getName());
		if(obj.getClass().getName().equals("java.util.ArrayList")) {
			mapl = (List) obj;
			mapl.add(value);
		}else {
			mapl = new ArrayList<Object>();
			mapl.add(obj);
			mapl.add(value);
		}
		map.put(elem.getName(), mapl);
	}
	
	/**
	 * <p>创建{@link SAXReader}
	 * <p>该方法定义了{@link SAXReader}的底层特性, 针对微信支付的xml数据解析时, 如果不这样做, 会有被xml攻击的风险, 使用此方法创建将避免
	 * @return {@link SAXReader}
	 * @throws SAXException 在设置底层特性时, 此处异正常情况不会出现, 当出现异常时请不要使用该方法或者使用该方法最新的替代品
	 */
	public SAXReader buildReader() throws SAXException {
		SAXReader reader = new SAXReader();
		reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		reader.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
		reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
		reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		return reader;
	}
	
}
