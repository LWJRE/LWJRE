package gamma.engine.core.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Static class that provides helper function to read files.
 *
 * @author Nico
 */
public class FileUtils {

	/**
	 * Reads the file at the given path and applies a function to it.
	 * Creates a {@link FileReader} and applies the given function to it.
	 * The value returned is the result of the given function.
	 *
	 * @param path Path of the file to read
	 * @param readerFunction Function that takes a {@code FileReader} as input and returns the result of the loading
	 * @return The result of the given function
	 * @param <T> Type of the object returned
	 * @throws UncheckedIOException if an {@link IOException} occurs when reading the file
	 */
	public static <T> T readFile(String path, FileReaderFunction<T> readerFunction) {
		try(FileReader reader = new FileReader(path)) {
			return readerFunction.apply(reader);
		} catch (IOException e) {
			throw new UncheckedIOException("Error reading file " + path, e);
		}
	}

	/**
	 * Reads a resource from the classpath and applies a function to it.
	 * Creates an {@link InputStream} and applies the given function to it.
	 * The value returned is the result of the given function.
	 *
	 * @param path Path to the resource to read in the classpath
	 * @param inputStreamFunction Function that takes an {@code InputStream} as input and returns the result of the loading
	 * @return The result of the given function
	 * @param <T> Type of the object returned
	 * @throws UncheckedIOException if an {@link IOException} occurs when reading the file
	 */
	public static <T> T readResource(String path, InputStreamFunction<T> inputStreamFunction) {
		try(InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + path + " in classpath");
			return inputStreamFunction.apply(inputStream);
		} catch (IOException e) {
			throw new UncheckedIOException("Error reading resource " + path, e);
		}
	}

	/**
	 * Reads the content of the file at the given path to a string.
	 * Uses {@link BufferedReader#lines()} to read all the lines of the file and join them with a line separator.
	 *
	 * @param path Path to the file to read
	 * @return A string containing the whole content of the file
	 */
	public static String readFileAsString(String path) {
		return readFile(path, reader -> {
			BufferedReader bufferedReader = new BufferedReader(reader);
			String result = bufferedReader.lines().collect(Collectors.joining("\n"));
			bufferedReader.close();
			return result;
		});
	}

	/**
	 * Reads the content of the file at the given path in the classpath to a string.
	 * Uses {@link BufferedReader#lines()} to read all the lines of the file and join them with a line separator.
	 *
	 * @param path Path to the file to read
	 * @return A string containing the whole content of the file
	 */
	public static String readResourceAsString(String path) {
		return readResource(path, inputStream -> {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String result = bufferedReader.lines().collect(Collectors.joining("\n"));
			bufferedReader.close();
			return result;
		});
	}

	/**
	 * Reads a java {@code .properties} file at the given path.
	 *
	 * @param path Path to the file to read
	 * @return A {@link Properties} object with the content of the file
	 */
	public static Properties readPropertiesFile(String path) {
		return readFile(path, reader -> {
			Properties properties = new Properties();
			properties.load(reader);
			return properties;
		});
	}

	/**
	 * Reads a java {@code .properties} file in the classpath at the given path.
	 *
	 * @param path Path to the file to read in the classpath
	 * @return A {@link Properties} object with the content of the file
	 */
	public static Properties readPropertiesResource(String path) {
		return readResource(path, inputStream -> {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		});
	}

	/**
	 * Reads the content of a file to a byte array.
	 *
	 * @param path Path to the file to read
	 * @return A byte array containing the bytes read from the file
	 */
	public static byte[] readFileBytes(String path) {
		try {
			return Files.readAllBytes(Path.of(path));
		} catch (IOException e) {
			throw new UncheckedIOException("Error reading file " + path, e);
		}
	}

	/**
	 * Reads the content of a file in the classpath to a byte array.
	 *
	 * @param path Path to the file to read in the classpath
	 * @return A byte array containing the bytes read from the file
	 */
	public static byte[] readResourceBytes(String path) {
		return readResource(path, InputStream::readAllBytes);
	}

	/**
	 * Interface used to pass function to helper methods that may throw an {@link IOException}.
	 *
	 * @param <T> Return type of the function
	 */
	public interface InputStreamFunction<T> {

		/**
		 * Applies this function to the given {@code InputStream}.
		 *
		 * @param inputStream {@code InputStream} of the file to read
		 * @return The function result
		 * @throws IOException If an IO error occurs
		 */
		T apply(InputStream inputStream) throws IOException;
	}

	/**
	 * Interface used to pass function to helper methods that may throw an {@link IOException}.
	 *
	 * @param <T> Return type of the function
	 */
	public interface FileReaderFunction<T> {

		/**
		 * Applies this function to the given {@code FileReader}.
		 *
		 * @param reader {@code FileReader} of the file to read
		 * @return The function result
		 * @throws IOException If an IO error occurs
		 */
		T apply(FileReader reader) throws IOException;
	}
}
