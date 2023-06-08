package gamma.engine.core;

import gamma.engine.core.resources.FileUtils;

import java.util.Properties;

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
	 * @return The requested property or an empty string if that property is not defined
	 */
	public static String getString(String key) {
		return get(key, "");
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
	 * Gets an integer from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property or zero if that property is not defined
	 */
	public static int getInt(String key) {
		return get(key, 0);
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
		if(property != null) {
			try {
				return Integer.parseInt(property);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * Gets a boolean from application properties.
	 *
	 * @param key Key of the property to get
	 * @return The requested property or false if that property is not defined
	 */
	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(PROPERTIES.getProperty(key));
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
		if(property != null && (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))) {
			return Boolean.parseBoolean(property);
		}
		return defaultValue;
	}

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

	public static float getFloat(String key) {
		return get(key, 0.0f);
	}
}
