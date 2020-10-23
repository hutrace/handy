package org.hutrace.handy.utils.scan;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.hutrace.handy.exception.ScanningApplicationException;
import org.hutrace.handy.utils.Charset;
import org.hutrace.handy.utils.OperatingSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScanningSuffix {
	
	private Logger log = LoggerFactory.getLogger(ScanningSuffix.class);
	
	private ClassLoader classLoader = ScanningSuffix.class.getClassLoader();
	
	private ScanningSuffixConduct scan;
	
	public void setScanning(ScanningSuffixConduct scan) {
		this.scan = scan;
	}
	
	public void start() throws ScanningApplicationException {
		if(scan == null) {
			throw new ScanningApplicationException("The 'ScanningSuffixConduct' parameter cannot be initialized to null");
		}
		for(String pakage : scan.getPackages()) {
			scanClass(pakage);
		}
	}
	
	/**
	 * <p>开始扫描单个包
	 * @param packages
	 * @return
	 * @throws ScanningApplicationException 
	 */
	private void scanClass(String packages) throws ScanningApplicationException {
		log.debug("Start scanning packets: " + packages);
		URL url = classLoader.getResource(packages.replace(".", "/"));
		if(url == null) {
			url = classLoader.getResource(packages.replace(".", "\\"));
		}
		String protocol = url.getProtocol();
		if(ScanningAnnotation.TYPE_FILE.equals(protocol)) {
			String filePath = null;
			try {
				filePath = URLDecoder.decode(url.getFile(), Charset.DEFAULT);
			}catch (UnsupportedEncodingException e) {
				throw new ScanningApplicationException("Error retrieving package physical path: ", e);
			}
			if(OperatingSystem.isWindows()) {
				filePath = filePath.substring(1);
			}
			scanClass(packages, Paths.get(filePath));
		}else if(ScanningAnnotation.TYPE_JAR.equals(protocol)) {
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
			String jarName = jarEntry.getName().replace("/", ".");
			if(jarName.indexOf(packages) == 0 && !jarEntry.isDirectory()) {
				scan.add(jarName);
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
			throw new ScanningApplicationException("Scan packet error: " + dir.getFileName(), e);
		}
		for(Path path : stream) {
			String fileName = String.valueOf(path.getFileName());
			if(fileName.indexOf(".") > -1) {
				if(scan.supports(fileName)) {
					scan.add(packageName + "." + fileName);
				}
			}else {
				scanClass(packageName + "." + fileName, path);
			}
		}
	}
	
}
