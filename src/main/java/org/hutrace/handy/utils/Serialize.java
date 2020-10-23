package org.hutrace.handy.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Java原生序列化类
 * <p>通过此类可以对Java对象进行本地序列化或者序列化成字节
 * @author HuTrace
 * @since 1.8
 * @version 1.0
 * @time 2020年2月25日
 */
public class Serialize {
	
	private static Logger log = LoggerFactory.getLogger(Serialize.class);
	
	/**
	 * 序列化生成文件的后缀
	 * <p>该值为".j"
	 */
	private static final String SUFFIX = ".j";
	
	/**
	 * <p>序列化Java对象
	 * <p>将序列化的结果储存为文件，文件会自动添加后缀{@link #SUFFIX}}
	 * @param obj Java对象
	 * @param path 储存文件的路径及文件名，不需要传入后缀，会自动添加后缀{@link #SUFFIX}}
	 * @throws IOException
	 */
	public static void toFile(Object obj, String path) throws IOException {
		path = path + SUFFIX;
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(path));
			out.writeObject(obj);
			log.debug("Serialized object successfully, path: " + path);
		}catch (IOException e) {
			log.error("Failed to serialize the object, path: " + path);
			throw e;
		}finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * <p>序列化Java对象
	 * <p>将序列化的结果作为字节进行返回，可用于网络传输
	 * @param obj Java对象
	 * @return 序列化后的字节
	 * @throws IOException
	 */
	public static byte[] toByte(Object obj) throws IOException {
		ObjectOutputStream out = null;
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try {
			out = new ObjectOutputStream(byteOut);
			out.writeObject(obj);
			log.debug("Serialized object successfully");
			return byteOut.toByteArray();
		}catch (IOException e) {
			log.error("Failed to serialize the object");
			throw e;
		}finally {
			if(out != null) {
				out.close();
			}
		}
	}

	/**
	 * 反序列化，将序列化后的文件反序列化为Java对象
	 * @param path 文件路径，不要加后缀，会自动添加后缀{@link #SUFFIX}
	 * @return 泛型，使用泛型应注意类型匹配问题
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deserializeByFile(String path) throws IOException, ClassNotFoundException {
		return deserialize(new FileInputStream(path + SUFFIX));
	}

	/**
	 * 反序列化，将序列化后的字节反序列化为Java对象
	 * @param bytes 序列化字节
	 * @return 泛型，使用泛型应注意类型匹配问题
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deserializeByByte(byte[] bytes) throws IOException, ClassNotFoundException {
		return deserialize(new ByteArrayInputStream(bytes));
	}
	
	/**
	 * 反序列化，将序列化后的字符串反序列化为Java对象
	 * @param bytes 序列化字符串
	 * @return 泛型，使用泛型应注意类型匹配问题
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static <T> T deserializeByByte(String bytes) throws IOException, ClassNotFoundException {
		return deserializeByByte(bytes.getBytes());
	}
	
	/**
	 * 反序列化，将序列化后的输入流反序列化为Java对象
	 * @param input 输入流
	 * @return 泛型，使用泛型应注意类型匹配问题
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T deserialize(InputStream input) throws ClassNotFoundException, IOException {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(input);
			T t =  (T) in.readObject();
			log.debug("Deserialized object successfully");
			return t;
		}catch (IOException e) {
			log.error("The deserialization object failed");
			throw e;
		}finally {
			if(in != null) {
				in.close();
			}
		}
	}
	
	/**
	 * 删除序列化文件
	 * @param file 文件路径，不需要传入后缀，会自动添加{@link #SUFFIX}
	 */
	public static void remove(String file) {
		File f = new File(file + SUFFIX);
		if (f.exists() && f.isFile()) {
			f.delete();
		}
	}
}