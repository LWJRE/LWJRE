package io.github.lwjre.engine;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import io.github.lwjre.engine.utils.YamlParser;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class ApplicationSettings {

	/** Stores the application settings */
	private static final HashMap<String, Object> SETTINGS = new HashMap<>();

	static {
		((Map<?, ?>) YamlParser.parseResource("settings.yaml")).forEach((key, value) -> {
			if(value instanceof Map<?, ?>) {
				setSettings(key.toString(), (Map<?, ?>) value);
			} else {
				SETTINGS.put(key.toString(), value);
			}
		});
	}

	/**
	 * Stores the settings of the given map.
	 * Called from the static initializer above.
	 *
	 * @param name Previous key
	 * @param map Map to read
	 */
	private static void setSettings(String name, Map<?, ?> map) {
		map.forEach((key, value) -> {
			if(value instanceof Map<?, ?>) {
				setSettings(name + '.' + key, (Map<?, ?>) value);
			} else {
				SETTINGS.put(name + '.' + key, value);
			}
		});
	}

	/**
	 * Gets a generic property.
	 *
	 * @param key Key of the property to get
	 * @param type Class of the property
	 * @return The requested property
	 * @param <T> Type of the property to get
	 * @throws NoSuchElementException If there is no property with the given key
	 */
	private static <T> T get(String key, Class<T> type) {
		if(SETTINGS.containsKey(key)) {
			return type.cast(SETTINGS.get(key));
		}
		throw new NoSuchElementException("There is no property with key " + key);
	}

	/**
	 * Gets a generic property.
	 *
	 * @param key Key of the property to get
	 * @param type Class of the property
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 * @param <T> Type of the property to get
	 */
	private static <T> T get(String key, Class<T> type, T defaultValue) {
		if(SETTINGS.containsKey(key)) {
			Object value = SETTINGS.get(key);
			if(value.getClass().isAssignableFrom(type)) {
				return type.cast(value);
			}
		}
		return defaultValue;
	}

	/**
	 * Gets a string from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 */
	public static String getString(String key) {
		if(SETTINGS.containsKey(key)) {
			return SETTINGS.get(key).toString();
		}
		throw new NoSuchElementException("There is no property with key " + key);
	}

	/**
	 * Gets a string from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static String get(String key, String defaultValue) {
		if(SETTINGS.containsKey(key)) {
			return SETTINGS.get(key).toString();
		}
		return defaultValue;
	}

	/**
	 * Gets an integer from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not an integer
	 */
	public static int getInt(String key) {
		return get(key, Integer.class);
	}

	/**
	 * Gets an integer from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static int get(String key, int defaultValue) {
		return get(key, Integer.class, defaultValue);
	}

	/**
	 * Gets a boolean from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a boolean
	 */
	public static boolean getBoolean(String key) {
		return get(key, Boolean.class);
	}

	/**
	 * Gets a boolean from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static boolean get(String key, boolean defaultValue) {
		return get(key, Boolean.class, defaultValue);
	}

	/**
	 * Gets a float from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static float get(String key, float defaultValue) {
		return get(key, Float.class, defaultValue);
	}

	/**
	 * Gets a float from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a float
	 */
	public static float getFloat(String key) {
		return get(key, Float.class);
	}

	/**
	 * Gets a {@link Vec2f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec2f get(String key, Vec2f defaultValue) {
		return get(key, Vec2f.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec2f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec2f}
	 */
	public static Vec2f getVec2f(String key) {
		return get(key, Vec2f.class);
	}

	/**
	 * Gets a {@link Vec3f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec3f get(String key, Vec3f defaultValue) {
		return get(key, Vec3f.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec3f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec3f}
	 */
	public static Vec3f getVec3f(String key) {
		return get(key, Vec3f.class);
	}

	/**
	 * Gets a {@link Vec4f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec4f get(String key, Vec4f defaultValue) {
		return get(key, Vec4f.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec4f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec4f}
	 */
	public static Vec4f getVec4f(String key) {
		return get(key, Vec4f.class);
	}

	/**
	 * Gets a {@link Vec2i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec2i get(String key, Vec2i defaultValue) {
		return get(key, Vec2i.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec2i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec2i}
	 */
	public static Vec2i getVec2i(String key) {
		return get(key, Vec2i.class);
	}

	/**
	 * Gets a {@link Vec3i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec3i get(String key, Vec3i defaultValue) {
		return get(key, Vec3i.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec3i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec3i}
	 */
	public static Vec3i getVec3i(String key) {
		return get(key, Vec3i.class);
	}

	/**
	 * Gets a {@link Vec4i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Vec4i get(String key, Vec4i defaultValue) {
		return get(key, Vec4i.class, defaultValue);
	}

	/**
	 * Gets a {@link Vec4i} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Vec4i}
	 */
	public static Vec4i getVec4i(String key) {
		return get(key, Vec4i.class);
	}

	/**
	 * Gets a {@link Color3f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Color3f get(String key, Color3f defaultValue) {
		return get(key, Color3f.class, defaultValue);
	}

	/**
	 * Gets a {@link Color3f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Color3f}
	 */
	public static Color3f getColor3f(String key) {
		return get(key, Color3f.class);
	}

	/**
	 * Gets a {@link Color4f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @param defaultValue Default value to get in case the property is not defined
	 * @return The requested property or the default value if that property is not defined
	 */
	public static Color4f get(String key, Color4f defaultValue) {
		return get(key, Color4f.class, defaultValue);
	}

	/**
	 * Gets a {@link Color4f} from application settings.
	 *
	 * @param key Key of the property to get
	 * @return The requested property
	 * @throws NoSuchElementException If there is no property with the given key
	 * @throws ClassCastException If this property is not a {@code Color4f}
	 */
	public static Color4f getColor4f(String key) {
		return get(key, Color4f.class);
	}
}
