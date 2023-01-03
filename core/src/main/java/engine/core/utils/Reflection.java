package engine.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class Reflection {

	public static Object instance(String className, Object... constructorParams) {
		try {
			return instance(Class.forName(className), constructorParams);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Cannot find class with name " + className, e);
		}
	}

	public static <T> T instance(Class<T> type, Object... constructorParams) {
		try {
			return type.cast(type.getConstructor(Arrays.stream(constructorParams).map(Object::getClass).toArray(Class[]::new)).newInstance(constructorParams));
		} catch (InstantiationException e) {
			throw new RuntimeException("Cannot instantiate object of type" + type, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot access constructor of class " + type, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Exception thrown when instantiating object of class " + type, e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Cannot find constructor with parameters " + Arrays.toString(constructorParams), e);
		}
	}

	public static void setField(Object object, String fieldName, Object value) {
		setField(object, object.getClass(), fieldName, value);
	}

	private static void setField(Object object, Class<?> fromClass, String fieldName, Object value) {
		try {
			Field field = fromClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		} catch (NoSuchFieldException e) {
			Class<?> superclass = fromClass.getSuperclass();
			if(superclass != null) setField(object, superclass, fieldName, value);
			else throw new RuntimeException("Cannot find field with name " + fieldName + " in class " + object.getClass(), e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot access field " + fieldName + " in object " + object, e);
		}
	}
}
