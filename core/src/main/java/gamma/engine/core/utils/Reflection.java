package gamma.engine.core.utils;

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

	/**
	 * Sets a field with the given name in the given object.
	 * This method can set public as well as private fields even if they are declared in the object's superclass.
	 * This method cannot set final fields.
	 *
	 * @param object The object that holds the given field
	 * @param fieldName Name of the field
	 * @param value Value to set to the field
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws NullPointerException If any of the given values except for the last one is null
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws IllegalArgumentException If type of the given value is not assignable to the field's type
	 */
	public static void setField(Object object, String fieldName, Object value) {
		setField(object, fieldName, object.getClass(), value);
	}

	/**
	 * Sets a field with the given name in the given object.
	 * This method can set public as well as private fields even if they are declared in the object's superclass.
	 * This method cannot set final fields.
	 *
	 * @param object The object that holds the given field
	 * @param fieldName Name of the field
	 * @param fieldClass Class where to look for the field
	 * @param value Value to set to the field
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws NullPointerException If any of the given values except for the last one is null
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws IllegalArgumentException If type of the given value is not assignable to the field's type
	 */
	private static void setField(Object object, String fieldName, Class<?> fieldClass, Object value) {
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

	/**
	 * Gets a {@link Stream} of {@link String}s that represent the names of the fields in the given class, including private and static fields.
	 * This includes fields declared in any superclass of the given one.
	 *
	 * @param from Class where to look for the fields
	 *
	 * @return A {@code Stream} containing the names of all the declared fields in the given class
	 *
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws NullPointerException If {@code from} is null
	 *
	 * @see Reflection#getAllFields(Class, boolean)
	 */
	public static Stream<String> getAllFields(Class<?> from) {
		return getAllFields(from, true);
	}

	/**
	 * Gets a {@link Stream} of {@link String}s that represent the names of the fields in the given class, including private and static fields.
	 * If {@code includeSuperclass} is true, this also includes fields declared in all superclasses up to {@link Object}.
	 *
	 * @param from Class where to look for the fields
	 * @param includeSuperclass True to include superclasses, false to limit to the given class
	 *
	 * @return A {@code Stream} containing the names of all the declared fields in the given class
	 *
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws NullPointerException If {@code from} is null
	 *
	 * @see Class#getDeclaredFields()
	 */
	public static Stream<String> getAllFields(Class<?> from, boolean includeSuperclass) {
		Stream<String> fields = Arrays.stream(from.getDeclaredFields()).map(Field::getName);
		if(includeSuperclass && !from.getSuperclass().equals(Object.class)) {
			return Stream.concat(fields, getAllFields(from.getSuperclass(), true));
		}
		return fields;
	}

	/**
	 * Instantiates an object of the given class using the given arguments as constructor arguments.
	 *
	 * @param type The class to instantiate
	 * @param arguments Constructor arguments
	 *
	 * @return The instantiated object
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws NullPointerException If {@code type} is null
	 *
	 * @see Class#getDeclaredConstructor(Class[])
	 * @see Constructor#newInstance(Object...)
	 *
	 * @param <T> Type of the object to instantiate
	 */
	public static <T> T instantiate(Class<T> type, Object... arguments) {
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

	/**
	 * Instantiates an object of the given class using the given arguments as constructor arguments.
	 *
	 * @param className Name of the class to instantiate
	 * @param arguments Constructor arguments
	 *
	 * @return The instantiated object
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws SecurityException If a security manager is preventing to set the field
	 * @throws NullPointerException If {@code type} is null
	 *
	 * @see Class#forName(String)
	 * @see Reflection#instantiate(Class, Object...)
	 */
	public static Object instantiate(String className, Object... arguments) {
		try {
			return instantiate(Class.forName(className), arguments);
		} catch (ClassNotFoundException e) {
			throw new ReflectionException("Cannot find class with name " + className, e);
		}
	}

	/**
	 * Calls a method with the given name and given arguments on the given object.
	 * This method can call public as well as private methods even if they are declared in the object's superclass.
	 *
	 * @param object The object on which the method is called
	 * @param methodName The name of the method
	 * @param arguments The method's arguments
	 *
	 * @return The result of the method call
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws NullPointerException If any of the given arguments is null
	 * @throws SecurityException If a security manager is preventing to set the field
	 *
	 * @see Class#getMethod(String, Class[])
	 * @see Method#invoke(Object, Object...)
	 */
	public static Object callMethod(Object object, String methodName, Object... arguments) {
		return callMethod(object, methodName, object.getClass(), arguments);
	}

	/**
	 * Calls a method with the given name and given arguments on the given object.
	 * This method can call public as well as private methods even if they are declared in the object's superclass.
	 *
	 * @param object The object on which the method is called
	 * @param methodName The name of the method
	 * @param methodClass The class where to look for the method
	 * @param arguments The method's arguments
	 *
	 * @return The result of the method call
	 *
	 * @throws ReflectionException If an exception involving reflection occurs
	 * @throws NullPointerException If any of the given arguments is null
	 * @throws SecurityException If a security manager is preventing to set the field
	 *
	 * @see Reflection#callMethod(Object, String, Object...)
	 */
	private static Object callMethod(Object object, String methodName, Class<?> methodClass, Object... arguments) {
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
