package gamma.engine.core;

import gamma.engine.core.utils.YamlParser;

import java.util.Map;

/**
 * Static class containing application settings. Used to get properties from the {@code /application.yaml} file.
 *
 * @author Nico
 */
public final class ApplicationSettings {

	/** Application settings */
	private static final Map<String, Object> SETTINGS = YamlParser.loadAsMap("/application.yaml");

	/**
	 * Gets an object at the specified path in the settings.
	 *
	 * @param path Settings path
	 * @return The requested object or null if that object could not be found
	 */
	private static Object get(String path) {
		Object result = SETTINGS;
		for(String property : path.split("/")) {
			if(result instanceof Map)
				result = ((Map<?, ?>) result).get(property);
		}
		return result;
	}

	/**
	 * Gets a string at the specified path in the settings.
	 *
	 * @param path Settings path
	 * @param defaultValue The value to return in case that setting is not found
	 * @return The requested string or the default value
	 */
	public static String get(String path, String defaultValue) {
		Object result = get(path);
		return result instanceof String ? (String) result : defaultValue;
	}

	/**
	 * Gets an integer at the specified path in the settings.
	 *
	 * @param path Settings path
	 * @param defaultValue The value to return in case that setting is not found
	 * @return The requested integer or the default value
	 */
	public static int get(String path, int defaultValue) {
		Object result = get(path);
		return result instanceof Integer ? (int) result : defaultValue;
	}

	/**
	 * Gets a boolean value at the specified path in the settings.
	 *
	 * @param path Settings path
	 * @param defaultValue The value to return in case that setting is not found
	 * @return The requested boolean value or the default value
	 */
	public static boolean get(String path, boolean defaultValue) {
		Object result = get(path);
		return result instanceof Boolean ? (boolean) result : defaultValue;
	}
}
