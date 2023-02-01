package gamma.engine.core;

import gamma.engine.core.utils.PropertiesFile;

public final class ApplicationProperties {

	private static final PropertiesFile SETTINGS = new PropertiesFile("/application.properties");

	public static int getInt(String key) {
		return SETTINGS.getInt(key);
	}

	public static int get(String key, int defaultValue) {
		return SETTINGS.get(key, defaultValue);
	}

	public static String getString(String key) {
		return SETTINGS.getString(key);
	}

	public static String get(String key, String defaultValue) {
		return SETTINGS.get(key, defaultValue);
	}

	public static boolean getBoolean(String key) {
		return SETTINGS.getBoolean(key);
	}

	public static boolean get(String key, boolean defaultValue) {
		return SETTINGS.get(key, defaultValue);
	}
}
