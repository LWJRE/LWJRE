package io.github.lwjre.engine;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import io.github.lwjre.engine.utils.FileUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.function.Function;

/**
 * Static class to store application properties.
 * Reads the {@code application.properties} file at the root of the classpath.
 *
 * @author Nico
 */
public final class ApplicationProperties {

	/** Application properties */
	private static final Properties PROPERTIES = FileUtils.readPropertiesResource("application.properties");

	/**
	 * Gets a string from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 */
	public static String getString(String key) {
		String property = PROPERTIES.getProperty(key);
		if(property == null)
			throw new NoSuchElementException("There is no property with key " + key);
		return property;
	}

	/**
	 * Gets a property and applies the given function to it.
	 *
	 * @param key Key of the property to get
	 * @param function The function to apply
	 * @return The requested property
	 * @param <T> Return type of the function
	 */
	private static <T> T get(String key, Function<String, T> function) {
		return function.apply(getString(key));
	}

	/**
	 * Gets a string from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static String get(String key, String defaultValue) {
		String property = PROPERTIES.getProperty(key);
		return property != null ? property : defaultValue;
	}

	/**
	 * Gets an int from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to an integer
	 */
	public static int getInt(String key) {
		return get(key, Integer::parseInt);
	}

	/**
	 * Gets an integer from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static int get(String key, int defaultValue) {
		String property = PROPERTIES.getProperty(key);
		if(property != null) try {
			return Integer.parseInt(property);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
		return defaultValue;
	}

	/**
	 * Gets a boolean from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 */
	public static boolean getBoolean(String key) {
		return get(key, Boolean::parseBoolean);
	}

	/**
	 * Gets a boolean from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static boolean get(String key, boolean defaultValue) {
		String property = PROPERTIES.getProperty(key);
		if(property != null) {
			return Boolean.parseBoolean(property);
		}
		return defaultValue;
	}

	/**
	 * Gets a float from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static float get(String key, float defaultValue) {
		String property = PROPERTIES.getProperty(key);
		if(property != null) {
			try {
				return Float.parseFloat(property);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * Gets a float from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float
	 */
	public static float getFloat(String key) {
		return get(key, Float::parseFloat);
	}

	/**
	 * Simplifies getting vector types.
	 *
	 * @param key Key of the property to get
	 * @param function The constructor function to apply
	 * @return The requested property
	 * @param <T> Return type of the function
	 */
	private static <T> T getArray(String key, Function<String[], T> function) {
		String property = getString(key);
		String[] values = Arrays.stream(property.split(",")).map(String::trim).toArray(String[]::new);
		return function.apply(values);
	}

	/**
	 * Simplifies getting vector types.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @param expectedLength Expected length of the array
	 * @param function The constructor function to apply
	 * @return The requested property
	 * @param <T> Return type of the function
	 */
	private static <T> T getArray(String key, T defaultValue, int expectedLength, Function<String[], T> function) {
		String property = PROPERTIES.getProperty(key);
		if(property == null) {
			return defaultValue;
		}
		String[] values = property.split(",");
		if(values.length == expectedLength) try {
			return function.apply(values);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
		return defaultValue;
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec2f get(String key, Vec2f defaultValue) {
		return getArray(key, defaultValue, 2, values -> new Vec2f(Float.parseFloat(values[0]), Float.parseFloat(values[1])));
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec2f getVec2f(String key) {
		return getArray(key, values -> new Vec2f(Float.parseFloat(values[0]), Float.parseFloat(values[1])));
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec3f get(String key, Vec3f defaultValue) {
		return getArray(key, defaultValue, 3, values -> new Vec3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2])));
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec3f getVec3f(String key) {
		return getArray(key, values -> new Vec3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2])));
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec4f get(String key, Vec4f defaultValue) {
		return getArray(key, defaultValue, 4, values -> new Vec4f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
	}

	/**
	 * Gets a float vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec4f getVec4f(String key) {
		return getArray(key, values -> new Vec4f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec2i get(String key, Vec2i defaultValue) {
		return getArray(key, defaultValue, 2, values -> new Vec2i(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to an int vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec2i getVec2i(String key) {
		return getArray(key, values -> new Vec2i(Integer.parseInt(values[0]), Integer.parseInt(values[1])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec3i get(String key, Vec3i defaultValue) {
		return getArray(key, defaultValue, 3, values -> new Vec3i(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to an int vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec3i getVec3i(String key) {
		return getArray(key, values -> new Vec3i(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec4i get(String key, Vec4i defaultValue) {
		return getArray(key, defaultValue, 4, values -> new Vec4i(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3])));
	}

	/**
	 * Gets an int vector from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to an int vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Vec4i getVec4i(String key) {
		return getArray(key, values -> new Vec4i(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]), Integer.parseInt(values[3])));
	}

	/**
	 * Gets a color from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Color3f get(String key, Color3f defaultValue) {
		return getArray(key, defaultValue, 3, values -> new Color3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2])));
	}

	/**
	 * Gets a color from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Color3f getColor3f(String key) {
		return getArray(key, values -> new Color3f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2])));
	}

	/**
	 * Gets a color from application properties.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Color4f get(String key, Color4f defaultValue) {
		return getArray(key, defaultValue, 4, values -> new Color4f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
	}

	/**
	 * Gets a color from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException if there is no property with the given key
	 * @throws NumberFormatException if the given key does not map to a float vector
	 * @throws ArrayIndexOutOfBoundsException if the vector has a smaller dimension
	 */
	public static Color4f getColor4f(String key) {
		return getArray(key, values -> new Color4f(Float.parseFloat(values[0]), Float.parseFloat(values[1]), Float.parseFloat(values[2]), Float.parseFloat(values[3])));
	}
}
