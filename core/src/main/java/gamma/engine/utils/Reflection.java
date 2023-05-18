package gamma.engine.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Static class to help with reflection.
 *
 * @author Nico
 */
public class Reflection {

	public static void setField(Object object, String fieldName, Object value) throws ReflectionException {
		setField(object, fieldName, object.getClass(), value);
	}

	private static void setField(Object object, String fieldName, Class<?> fieldClass, Object value) throws ReflectionException {
		try {
			Field field = fieldClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			if(value instanceof Number number) {
				Class<?> fieldType = field.getType();
				if(fieldType.equals(byte.class) || fieldType.equals(Byte.class))
					field.set(object, number.byteValue());
				else if(fieldType.equals(short.class) || fieldType.equals(Short.class))
					field.set(object, number.shortValue());
				else if(fieldType.equals(int.class) || fieldType.equals(Integer.class))
					field.set(object, number.intValue());
				else if(fieldType.equals(long.class) || fieldType.equals(Long.class))
					field.set(object, number.longValue());
				else if(fieldType.equals(float.class) || fieldType.equals(Float.class))
					field.set(object, number.floatValue());
				else if(fieldType.equals(double.class) || fieldType.equals(Double.class))
					field.set(object, number.doubleValue());
			} else {
				field.set(object, value);
			}
		} catch (NoSuchFieldException e) {
			if(fieldClass.getSuperclass().equals(Object.class))
				throw new ReflectionException("Cannot find field with name " + fieldName, e);
			else setField(object, fieldName, fieldClass.getSuperclass(), value);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static Object getField(Object object, String fieldName) throws ReflectionException {
		return getField(object, fieldName, object.getClass());
	}

	private static Object getField(Object object, String fieldName, Class<?> fieldClass) throws ReflectionException {
		try {
			Field field = fieldClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(object);
		} catch (NoSuchFieldException e) {
			if(fieldClass.getSuperclass().equals(Object.class))
				throw new ReflectionException("Cannot find field with name " + fieldName, e);
			else return getField(object, fieldName, fieldClass.getSuperclass());
		} catch (IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}

	public static Stream<String> getAllFields(Class<?> from) {
		return getAllFields(from, true);
	}

	public static Stream<String> getAllFields(Class<?> from, boolean includeSuperclass) {
		Stream<String> fields = Arrays.stream(from.getDeclaredFields()).map(Field::getName);
		if(includeSuperclass && !from.getSuperclass().equals(Object.class)) {
			return Stream.concat(fields, getAllFields(from.getSuperclass(), true));
		}
		return fields;
	}

	public static <T> T instantiate(Class<T> type, Object... arguments) throws ReflectionException {
		try {
			Class<?>[] parameters = Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new);
			Constructor<?> constructor = type.getDeclaredConstructor(parameters);
			constructor.setAccessible(true);
			return type.cast(constructor.newInstance(arguments));
		} catch (InstantiationException e) {
			throw new ReflectionException("Class " + type.getName() + " is abstract", e);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new ReflectionException(e);
		} catch (NoSuchMethodException e) {
			throw new ReflectionException("Class " + type.getName() + " does not have a constructor with the given arguments", e);
		}
	}

	public static Object instantiate(String className, Object... arguments) throws ReflectionException {
		try {
			return instantiate(Thread.currentThread().getContextClassLoader().loadClass(className), arguments);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException("Cannot find class with name " + className, e);
		}
	}

	public static Object callMethod(Object object, String methodName, Object... arguments) throws ReflectionException {
		return callMethod(object, methodName, object.getClass(), arguments);
	}

	private static Object callMethod(Object object, String methodName, Class<?> methodClass, Object... arguments) throws ReflectionException {
		try {
			Class<?>[] parameters = Arrays.stream(arguments).map(Object::getClass).toArray(Class[]::new);
			Method method = methodClass.getMethod(methodName, parameters);
			method.setAccessible(true);
			return method.invoke(object, arguments);
		} catch (NoSuchMethodException e) {
			if(methodClass.getSuperclass().equals(Object.class))
				throw new ReflectionException("Cannot find method with name " + methodName, e);
			else return callMethod(object, methodName, methodClass.getSuperclass(), arguments);
		} catch (InvocationTargetException | IllegalAccessException e) {
			throw new ReflectionException(e);
		}
	}
}
