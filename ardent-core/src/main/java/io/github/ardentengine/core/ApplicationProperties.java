package io.github.ardentengine.core;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class ApplicationProperties {

    /**
     * Application properties.
     */
    private static Properties properties;

    /**
     * Private method that returns {@link ApplicationProperties#properties}.
     * Loads the properties the first time this method is called.
     *
     * @return The {@link Properties} object.
     */
    private static Properties getProperties() {
        if(properties == null) {
            properties = new Properties();
            try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
                if(inputStream == null) {
                    System.err.println("Could not find application.properties file");
                } else {
                    properties.load(inputStream);
                }
            } catch(IOException e) {
                System.err.println("Could not read application.properties file");
                e.printStackTrace();
            }
        }
        return properties;
    }

    /**
     * Gets a string from the application properties.
     *
     * @param key Key of the requested property.
     * @param defaultValue Default value to return in case the property does not exist.
     * @return The requested property or the given default value if it does not exist.
     */
    public static String getString(String key, String defaultValue) {
        return getProperties().getProperty(key, defaultValue);
    }

    /**
     * Private method used to get a property and map it to another type using the given function.
     *
     * @param key Key of the requested property.
     * @param function Function to apply to the property.
     * @param defaultValue Default value to return in case the property does not exist.
     * @return The requested property, mapped with the given function, or the given default value if it does not exist.
     * @param <T> Type of the requested property.
     */
    private static <T> T get(String key, Function<String, T> function, T defaultValue) {
        var value = getProperties().getProperty(key);
        if(value == null) {
            return defaultValue;
        }
        return function.apply(value);
    }

    /**
     * Private method used to get a property and map it to another type using the given function.
     * Logs an error if the requested property does not exist.
     *
     * @param key Key of the requested property.
     * @param function Function to apply to the property.
     * @param defaultValue Default value to return in case the property does not exist.
     * @return The requested property, mapped with the given function, or the given default value if it does not exist.
     * @param <T> Type of the requested property.
     */
    private static <T> T getOrLogError(String key, Function<String, T> function, T defaultValue) {
        var value = getProperties().getProperty(key);
        if(value == null) {
            System.err.println("There is no property with key " + key + " in application properties");
            return defaultValue;
        }
        return function.apply(value);
    }

    /**
     * Gets a string from the application properties.
     * Returns an empty string and logs an error if the requested property does not exist.
     *
     * @param key Key of the requested property.
     * @return The requested property.
     */
    public static String getString(String key) {
        return getOrLogError(key, Function.identity(), "");
    }

    /**
     * Private method used to convert a string to an integer and log an error in case the string is not a valid integer.
     *
     * @param key Key of the requested property. Only used in the error message.
     * @param value The string to parse.
     * @param defaultValue Default value to return in case the given string is not a valid integer.
     * @return The integer value represented by the given string.
     */
    private static int parseInt(String key, String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch(NumberFormatException e) {
            System.err.println("Property \"" + key + "\" with value \"" + value + "\" is not an integer");
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Gets an integer from the application properties.
     * Logs an error if the requested property is not an integer.
     *
     * @param key Key of the requested property.
     * @param defaultValue Default value to return in case the property does not exist, or it is not an integer.
     * @return The requested property.
     */
    public static int getInt(String key, int defaultValue) {
        return get(key, value -> parseInt(key, value, defaultValue), defaultValue);
    }

    /**
     * Gets an integer from the application properties.
     * Returns zero and logs an error if the requested property does not exist, or it is not an integer.
     *
     * @param key Key of the requested property.
     * @return The requested property.
     */
    public static int getInt(String key) {
        return getOrLogError(key, value -> parseInt(key, value, 0), 0);
    }

    /**
     * Private method used to convert a string to a float and log an error in case the string is not a valid float.
     *
     * @param key Key of the requested property. Only used in the error message.
     * @param value The string to parse.
     * @param defaultValue Default value to return in case the given string is not a valid float.
     * @return The float value represented by the given string.
     */
    private static float parseFloat(String key, String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch(NumberFormatException e) {
            System.err.println("Property \"" + key + "\" with value \"" + value + "\" is not a float");
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * Gets a float from the application properties.
     * Logs an error if the requested property is not a float.
     *
     * @param key Key of the requested property.
     * @param defaultValue Default value to return in case the property does not exist, or it is not a float.
     * @return The requested property.
     */
    public static float getFloat(String key, float defaultValue) {
        return get(key, value -> parseFloat(key, value, defaultValue), defaultValue);
    }

    /**
     * Gets a float from the application properties.
     * Returns zero and logs an error if the requested property does not exist, or it is not a float.
     *
     * @param key Key of the requested property.
     * @return The requested property.
     */
    public static float getFloat(String key) {
        return getOrLogError(key, value -> parseFloat(key, value, 0.0f), 0.0f);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return get(key, Boolean::parseBoolean, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getOrLogError(key, Boolean::parseBoolean, false);
    }
}
