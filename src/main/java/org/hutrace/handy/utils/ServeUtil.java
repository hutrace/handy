package org.hutrace.handy.utils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Map;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ServeUtil {
	
	private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE = new IdentityHashMap<Class<?>, Class<?>>(8);
	
	static {
		PRIMITIVE_WRAPPER_TYPE.put(Boolean.class, boolean.class);
		PRIMITIVE_WRAPPER_TYPE.put(Byte.class, byte.class);
		PRIMITIVE_WRAPPER_TYPE.put(Character.class, char.class);
		PRIMITIVE_WRAPPER_TYPE.put(Double.class, double.class);
		PRIMITIVE_WRAPPER_TYPE.put(Float.class, float.class);
		PRIMITIVE_WRAPPER_TYPE.put(Integer.class, int.class);
		PRIMITIVE_WRAPPER_TYPE.put(Long.class, long.class);
		PRIMITIVE_WRAPPER_TYPE.put(Short.class, short.class);
	}
	
	/**
	 * <p>判断Object是否是简单类型
	 * @param obj {@link Object}
	 * @return true/false
	 */
	public static boolean simpleType(Object obj) {
		return simpleType(obj.getClass());
	}
	
	/**
	 * <p>判断Class是否是简单类型
	 * @param obj {@link Class}
	 * @return true/false
	 */
	public static boolean simpleType(Class<?> clazs) {
		nullMsg(clazs, "Parameter 'Class' must not be null");
		return simpleValueType(clazs) || (clazs.isArray() && simpleValueType(clazs.getComponentType()));
	}
	
	/**
	 * <p>判断Class是否是简单值类型
	 * @param obj {@link Object}
	 * @return true/false
	 */
	public static boolean simpleValueType(Class<?> clazs) {
		return (primitiveOrWrapperType(clazs) || clazs.isEnum() ||
				CharSequence.class.isAssignableFrom(clazs) ||
				Number.class.isAssignableFrom(clazs) ||
				Date.class.isAssignableFrom(clazs) ||
				URI.class == clazs || URL.class == clazs ||
				Locale.class == clazs || Class.class == clazs);
	}
	
	/**
	 * <p>判断Class类型是否是基本类型及基本封装类型
	 * @param clazs {@link Class}
	 * @return true/false
	 */
	public static boolean primitiveOrWrapperType(Class<?> clazs) {
		nullMsg(clazs, "Parameter 'Class' must not be null");
		return (clazs.isPrimitive() || PRIMITIVE_WRAPPER_TYPE.containsKey(clazs));
	}
	
	/**
	 * <p>判断参数object的值如果为null则抛出{@link IllegalArgumentException}异常，信息为message
	 * @param object 参数值
	 * @param message 异常信息
	 */
	public static void nullMsg(Object object, String message) {
		if(object == null) {
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * <p>获取方法中的参数名称列表
	 * @param method 方法{@link Method}
	 * @return 参数名称数组
	 * @throws IOException
	 */
	public static String[] getMethodParametersNames(final Method method) throws IOException {
		final String methodName = method.getName();
		final Class<?>[] methodParameterTypes = method.getParameterTypes();
		final int methodParameterCount = methodParameterTypes.length;
		final String className = method.getDeclaringClass().getName();
		final boolean isStatic = Modifier.isStatic(method.getModifiers());
		final String[] methodParametersNames = new String[methodParameterCount];
		ClassReader cr = new ClassReader(className);
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		cr.accept(new ClassAdapter(cw) {
			public MethodVisitor visitMethod(int access, String name, String desc, String signature,
					String[] exceptions) {
				MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
				final Type[] argTypes = Type.getArgumentTypes(desc);
				// 参数类型不一致
				if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
					return mv;
				}
				return new MethodAdapter(mv) {
					public void visitLocalVariable(String name, String desc, String signature,
							Label start, Label end, int index) {
						// 如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
						int methodParameterIndex = isStatic ? index : index - 1;
						if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
							methodParametersNames[methodParameterIndex] = name;
						}
						super.visitLocalVariable(name, desc, signature, start, end, index);
					}
				};
			}
		}, 0);
		return methodParametersNames;
	}
	
	
	private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
		if (types.length != parameterTypes.length) {
			return false;
		}
		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(parameterTypes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}
	
}
