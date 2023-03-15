package gamma.engine.core;

import gamma.engine.core.utils.FileUtils;

import java.util.Properties;

public final class ApplicationProperties {

	private static final Properties PROPERTIES = FileUtils.readPropertiesResource("/application.properties");

	public static String getString(String key) {
		return get(key, "");
	}

	public static String get(String key, String defaultValue) {
		String property = PROPERTIES.getProperty(key);
		return property != null ? property : defaultValue;
	}

	public static int getInt(String key) {
		return get(key, 0);
	}

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

	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(PROPERTIES.getProperty(key));
	}

	public static boolean get(String key, boolean defaultValue) {
		String property = PROPERTIES.getProperty(key);
		if(property != null && (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))) {
			return Boolean.parseBoolean(property);
		}
		return defaultValue;
	}
}
