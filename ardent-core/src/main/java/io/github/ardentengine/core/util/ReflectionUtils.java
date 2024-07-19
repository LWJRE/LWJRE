package io.github.ardentengine.core.util;

import java.lang.reflect.InvocationTargetException;

/**
 * Static class containing utility methods for reflection.
 */
public final class ReflectionUtils {

    /**
     * Creates a new instance of the given class.
     * The given class must have a public no-args constructor for it to be instantiated by this method.
     *
     * @param ofClass The class to instantiate.
     * @return A new instance of the given class.
     * @param <T> The type of object to instantiate.
     * @throws ReflectionException If an exception occurs while trying to instantiate the class.
     */
    public static <T> T newInstance(Class<T> ofClass) {
        try {
            return ofClass.getConstructor().newInstance();
        } catch(InvocationTargetException e) {
            throw new ReflectionException("Exception occurred while instantiating " + ofClass, e);
        } catch(InstantiationException e) {
            throw new ReflectionException("Cannot instantiate " + ofClass + " because it is abstract", e);
        } catch(IllegalAccessException | NoSuchMethodException e) {
            throw new ReflectionException("Could not find a public no-args constructor in " + ofClass, e);
        }
    }

    /**
     * Creates a new instance of a class with the given name.
     * The given class must have a public no-args constructor for it to be instantiated by this method.
     *
     * @param className Name of the class to instantiate.
     * @return A new instance of the class with the given name.
     * @throws ReflectionException If an exception occurs while trying to instantiate the class.
     */
    public static Object newInstance(String className) {
        try {
            return newInstance(Class.forName(className));
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Could not find class with name " + className, e);
        }
    }

    /**
     * Private method used to look for field names in superclasses when setting a field.
     * <p>
     *     Calls itself recursively with the superclass of the given class if a field with the given name is not found.
     *     Returns false if the given class is {@link Object}.
     * </p>
     *
     * @param object The object whose field should be set.
     * @param objectType Class where to look for a field with the given name.
     * @param fieldName Name of the field to set.
     * @param value The new value for the field.
     * @return True if the field was set correctly, false if a field with the given name does not exist.
     * @throws ReflectionException If the field could not be accessed.
     */
    private static boolean setField(Object object, Class<?> objectType, String fieldName, Object value) {
        try {
            // Get the field and make it accessible
            var field = objectType.getDeclaredField(fieldName);
            field.setAccessible(true);
            var fieldType = field.getType();
            if(fieldType.isAssignableFrom(value.getClass())) {
                // Set the field and return true if the type correspond
                field.set(object, value);
                return true;
            } else if(value instanceof Number numberValue) {
                // Numeric fields need to be converted to the correct value
                if(fieldType.equals(byte.class) || fieldType.equals(Byte.class)) {
                    field.set(object, numberValue.byteValue());
                } else if(fieldType.equals(short.class) || fieldType.equals(Short.class)) {
                    field.set(object, numberValue.shortValue());
                } else if(fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(object, numberValue.intValue());
                } else if(fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                    field.set(object, numberValue.longValue());
                } else if(fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                    field.set(object, numberValue.floatValue());
                } else if(fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    field.set(object, numberValue.doubleValue());
                }
                return true;
            } else if(value instanceof Boolean booleanValue) {
                // Boolean values need to be converted to the boolean primitive
                if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    field.set(object, booleanValue);
                }
                return true;
            }
            // Return false if the type does not match
            return false;
        } catch(NoSuchFieldException e) {
            // Look for a field with the same name in the superclass
            if(!objectType.equals(Object.class)) {
                return setField(object, objectType.getSuperclass(), fieldName, value);
            }
            // The field does not exist if the given class has no superclass
            return false;
        } catch(IllegalAccessException e) {
            throw new ReflectionException("Could not access field " + fieldName + " in " + objectType, e);
        }
    }

    /**
     * Sets a field with the given name in the given object to the given value, even if it is private.
     * If a field with the given name does not exist or its type does not match the given value, nothing happens and this method returns false.
     *
     * @param object The object whose field should be set.
     * @param fieldName Name of the field to set.
     * @param value The new value for the field.
     * @return True if the field was set correctly, false if a field with the given name does not exist.
     * @throws ReflectionException If the field could not be accessed.
     */
    public static boolean setField(Object object, String fieldName, Object value) {
        return setField(object, object.getClass(), fieldName, value);
    }
}