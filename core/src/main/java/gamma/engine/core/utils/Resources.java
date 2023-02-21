package gamma.engine.core.utils;

import java.io.*;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Utils class to read resources from the classpath.
 *
 * @author Nico
 */
public final class Resources {

	/**
	 * Reads any resource with the given function.
	 *
	 * @param path Path to the classpath resource
	 * @param function Function to apply when loading the resource
	 * @return The result of the function
	 * @param <T> Type of the result
	 * @throws UncheckedIOException if an IO error occurs
	 */
	public static <T> T readAs(String path, IOFunction<T> function) {
		try(InputStream inputStream = Resources.class.getResourceAsStream(path)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find resource " + path + " in classpath");
			return function.apply(inputStream);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Reads the content of a text file in the classpath to a string.
	 *
	 * @param path Path to the text file in the classpath
	 * @return A string with the content of the whole text file
	 */
	public static String readAsString(String path) {
		return readAs(path, inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			return result;
		});
	}

	/**
	 * Reads a {@code .properties} file from the classpath.
	 *
	 * @param path Path to the properties file in the classpath
	 * @return A {@link Properties} object with the properties read from the file
	 * @throws UncheckedIOException if an IO error occurs
	 */
	public static Properties readProperties(String path) {
		return readAs(path, inputStream -> {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		});
	}

	/**
	 * Functional interface that can be used as a lambda in {@link Resources#readAs(String, IOFunction)}.
	 *
	 * @param <T> Return type of the function
	 */
	public interface IOFunction<T> {

		/**
		 * Applies the function.
		 *
		 * @param inputStream Classpath resource input stream
		 * @return The result of the function
		 * @throws IOException If an IO error occurs
		 */
		T apply(InputStream inputStream) throws IOException;
	}
}
