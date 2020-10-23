package org.hutrace.handy.utils.scan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hutrace.handy.exception.ScanningApplicationException;
import org.hutrace.handy.utils.Charset;
import org.hutrace.handy.utils.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>根据包路径与注解类型扫描得到对应的类
 * <p>可扩展methodAnnotation得到对应类下对应的方法
 * @author hu trace
 * @since 1.8
 * @version 1.0
 */
public class ScanningAnnotation {

	private Logger log = LoggerFactory.getLogger(ScanningAnnotation.class);
	private ClassLoader classLoader = ScanningAnnotation.class.getClassLoader();
	
	static final String TYPE_FILE = "file";
	static final String TYPE_JAR = "jar";
	
	private final String SCAN_CLASS_SUFFIX = ".class";
	protected List<Class<?>> classList;
	
	private ScanningAnnotationConduct scan;
	
	/**
	 * <p>构造方法
	 * @param scan {@link ScanningAnnotationConduct}
	 * @throws ScanningApplicationException 
	 */
	private ScanningAnnotation() {}
	
	public static void build(ScanningAnnotationConduct scan) throws ScanningApplicationException {
		ScanningAnnotation s = new ScanningAnnotation();
		s.classList = new ArrayList<>();
		s.scan = scan;
		s.start();
	}
	
	/**
	 * <p>开始扫描所有包
	 * @throws ScanningApplicationException
	 */
	private void start() throws ScanningApplicationException {
		for(String pac : scan.getPackages()) {
			scanClass(pac);
		}
		if(null != scan.getMethodAnnotation()) {
			scanMethod();
		}
		if(null != scan.getFieldAnnotation()) {
			sacnField();
		}
		classList = null;
	}
	
	/**
	 * <p>开始扫描单个包
	 * @param packages
	 * @return
	 * @throws ScanningApplicationException 
	 */
	private void scanClass(String packages) throws ScanningApplicationException {
		log.debug("Start scanning packets: {}", packages);
		URL url = classLoader.getResource(packages.replace(".", "/"));
		if(url == null) {
			url = classLoader.getResource(packages.replace(".", "\\"));
		}
		String protocol = url.getProtocol();
		if(TYPE_FILE.equals(protocol)) {
			String filePath = null;
			try {
				filePath = URLDecoder.decode(url.getFile(), Charset.DEFAULT);
			}catch (UnsupportedEncodingException e) {
				throw new ScanningApplicationException("Error retrieving physical path !", e);
			}
			if(OperatingSystem.isWindows()) {
				filePath = filePath.substring(1);
			}
			scanClass(packages, Paths.get(filePath));
		}else if(TYPE_JAR.equals(protocol)) {
			try {
				JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
				JarFile jarFile = jarUrlConnection.getJarFile();
				Enumeration<JarEntry> jarEntries = jarFile.entries();
				scanClass(packages, jarEntries);
			}catch (IOException e) {
				throw new ScanningApplicationException("Error retrieving physical path !", e);
			}
		}
	}
	
	private void scanClass(String packages, Enumeration<JarEntry> jarEntries) throws ScanningApplicationException {
		while(jarEntries.hasMoreElements()) {
			JarEntry jarEntry = jarEntries.nextElement();
			String jarName = jarEntry.getName().replace("/", ".").replace("\\", ".");
			if(jarName.indexOf(packages) == 0 && !jarEntry.isDirectory() && jarName.endsWith(SCAN_CLASS_SUFFIX)) {
				Class<?> clazs = null;
				try {
					clazs = Class.forName(jarName.replace(".class", ""));
				}catch (Exception e) {
					throw new ScanningApplicationException(e);
				}
				pushClass(clazs);
			}
		}
	}
	
	/**
	 * <p>递归扫描所有包
	 * @param packageName
	 * @param dir
	 * @throws ScanningApplicationException
	 */
	private void scanClass(String packageName, Path dir) throws ScanningApplicationException {
		DirectoryStream<Path> stream = null;
		try {
			stream = Files.newDirectoryStream(dir);
		}catch (IOException e) {
			throw new ScanningApplicationException("Error scanning package: " + dir.getFileName(), e);
		}
		for(Path path : stream) {
			String fileName = String.valueOf(path.getFileName());
			if(fileName.indexOf(".") > -1) {
				if(fileName.endsWith(SCAN_CLASS_SUFFIX)) {
					String className = fileName.substring(0, fileName.length() - 6);
					Class<?> clazs = null;
					try {
						clazs = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className);
					}catch (ClassNotFoundException e) {
						throw new ScanningApplicationException(e);
					}
					pushClass(clazs);
				}
			}else {
				scanClass(packageName + "." + fileName, path);
			}
		}
	}
	
	private void pushClass(Class<?> clazs) throws ScanningApplicationException {
		if(null != clazs) {
			if(null == scan.getTypeAnnotation()) {
				classList.add(clazs);
				return;
			}
			Annotation an = clazs.getAnnotation(scan.getTypeAnnotation());
			if(null != an) {
				log.debug("A class was scanned: " + clazs.toString());
				classList.add(clazs);
				scan.addClass(an, clazs);
			}
		}
	}
	
	/**
	 * <p>扫描包
	 * @throws ServerException 
	 */
	private void scanMethod() throws ScanningApplicationException {
		Method[] methods;
		Annotation an;
		for(Class<?> clazs : classList) {
			methods = clazs.getDeclaredMethods();
			for(Method method : methods) {
				an = null;
				an = method.getAnnotation(scan.getMethodAnnotation());
				if(null != an) {
					scan.addMethod(an, clazs, method);
				}
			}
		}
	}
	
	private void sacnField() throws ScanningApplicationException {
		Field[] fields;
		Annotation an;
		for(Class<?> clazs : classList) {
			fields = clazs.getDeclaredFields();
			for(Field field : fields) {
				an = null;
				an = field.getAnnotation(scan.getFieldAnnotation());
				if(null != an) {
					scan.addField(an, clazs, field);
				}
			}
		}
	}
	
}
