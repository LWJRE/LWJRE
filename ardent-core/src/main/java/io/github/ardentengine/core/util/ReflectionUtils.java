package io.github.ardentengine.core.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Static class containing utility methods for reflection.
 */
public final class ReflectionUtils {

    /**
     * Prevents the instantiation of a static class.
     */
    private ReflectionUtils() {

    }

    /**
     * Creates a new instance of a class with the given name.
     * The given class must have a public no-args constructor for it to be instantiated by this method.
     *
     * @param className Name of the class to instantiate.
     * @return A new instance of the class with the given name.
     * @throws ReflectionException If an exception occur while trying to instantiate the class.
     */
    public static Object newInstance(String className) {
        try {
            return Class.forName(className).getConstructor().newInstance();
        } catch(ClassNotFoundException e) {
            throw new ReflectionException("Could not find class with name " + className, e);
        } catch(InvocationTargetException e) {
            throw new ReflectionException("Exception occurred while instantiating class " + className, e);
        } catch(InstantiationException e) {
            throw new ReflectionException("Cannot instantiate class " + className + " because it is abstract", e);
        } catch(IllegalAccessException | NoSuchMethodException e) {
            throw new ReflectionException("Could not find a public no-args constructor in class " + className, e);
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

    /**
     * Private method used to look for fields in superclasses when getting fields.
     * <p>
     *     Calls itself recursively with the superclass of the given class.
     *     Stops when the given class is {@link Object}.
     * </p>
     *
     * @param object Object from which the fields' values are to be extracted.
     * @param fromClass Class where to look for fields.
     * @param result The map where to store the result.
     * @throws ReflectionException If any of the fields could not be accessed.
     */
    private static void getAllFields(Object object, Class<?> fromClass, Map<String, Object> result) {
        if(!fromClass.equals(Object.class)) {
            getAllFields(object, fromClass.getSuperclass(), result);
        }
        for(var field : fromClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                result.put(field.getName(), field.get(object));
            } catch(IllegalAccessException e) {
                throw new ReflectionException("Could not access field " + field.getName() + " in " + fromClass, e);
            }
        }
    }

    // TODO: Should this get transient fields?

    /**
     * Gets name and value of all fields in the given object and adds them to the given map, including private fields.
     * Looks for fields in the class of the given object and all of its superclasses.
     *
     * @param object Object from which the fields' values are to be extracted.
     * @param result The map where to store the result.
     * @throws ReflectionException If any of the fields could not be accessed.
     */
    public static void getAllFields(Object object, Map<String, Object> result) {
        getAllFields(object, object.getClass(), result);
    }
}
