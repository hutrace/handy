package org.hutrace.handy.utils.scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.hutrace.handy.exception.ScanningApplicationException;

/**
 * <p>提供需要扫描的接口规范
 * <p>当需要扫描包时继承此接口
 * @author <a href="http://www.wayakeji.net/"> Waya Co.,Ltd tracy</a>
 * @since 1.8
 * @version 1.0
 * @time 2019年3月22日
 */
public interface ScanningAnnotationConduct {
	
	/**
	 * <p>设置需要扫描的包数组
	 * @return 需要扫描的包数组
	 */
	public String[] getPackages();
	
	/**
	 * <p>设置需要扫描类的注解
	 * @return 需要扫描类的注解
	 */
	public Class<? extends Annotation> getTypeAnnotation();
	
	/**
	 * <p>设置需要扫描类中方法的注解
	 * @return 需要扫描类中方法的注解
	 */
	public Class<? extends Annotation> getMethodAnnotation();
	
	/**
	 * <p>设置需要扫描类中属性的主键
	 * @return 需要扫描类的注解
	 */
	public Class<? extends Annotation> getFieldAnnotation();
	
	/**
	 * <p>执行方法，当扫描到规定要求的类时调用此方法执行
	 * @param t {@link #getTypeAnnotation()}的值
	 * @param clazs 扫描到的类
	 */
	public void addClass(Annotation a, Class<?> clazs) throws ScanningApplicationException;
	
	/**
	 * <p>执行方法，当扫描到规定要求的方法时调用此方法执行
	 * @param m {@link #getMethodAnnotation()}的值
	 * @param clazs 扫描到的类
	 * @param method 扫描到的方法
	 */
	public void addMethod(Annotation a, Class<?> clazs, Method method) throws ScanningApplicationException;
	
	/**
	 * <p>执行方法，当扫描到规定要求的属性时调用此方法执行
	 * @param m {@link #getFieldAnnotation()}的值
	 * @param clazs 扫描到的类
	 * @param method 扫描到的方法
	 */
	public void addField(Annotation a, Class<?> clazs, Field field) throws ScanningApplicationException;
	
}
