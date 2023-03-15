package gamma.engine.core.utils;

import java.io.*;
import java.util.Properties;
import java.util.stream.Collectors;

public class FileUtils {

//	public static <T> T readAs(String path, IOFunction<T> function) {
//		try(FileReader reader = new FileReader(path)) {
//			return function.apply(reader);
//		} catch (IOException e) {
//			throw new UncheckedIOException(e);
//		}
//	}

	public static <T> T readFile(String path, FileReaderFunction<T> readerFunction) {
		try(FileReader reader = new FileReader(path)) {
			return readerFunction.apply(reader);
		} catch (IOException e) {
			throw new UncheckedIOException("Error reading file " + path, e);
		}
	}

	public static <T> T readResource(String path, InputStreamFunction<T> inputStreamFunction) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(path)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + path + " in classpath");
			return inputStreamFunction.apply(inputStream);
		} catch (IOException e) {
			throw new UncheckedIOException("Error reading resource " + path, e);
		}
	}

	public static String readFileAsString(String path) {
		return readFile(path, reader -> {
			BufferedReader bufferedReader = new BufferedReader(reader);
			String result = bufferedReader.lines().collect(Collectors.joining("\n"));
			bufferedReader.close();
			return result;
		});
	}

	public static String readResourceAsString(String path) {
		return readResource(path, inputStream -> {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String result = bufferedReader.lines().collect(Collectors.joining("\n"));
			bufferedReader.close();
			return result;
		});
	}

	public static Properties readPropertiesFile(String path) {
		return readFile(path, reader -> {
			Properties properties = new Properties();
			properties.load(reader);
			return properties;
		});
	}

	public static Properties readPropertiesResource(String path) {
		return readResource(path, inputStream -> {
			Properties properties = new Properties();
			properties.load(inputStream);
			return properties;
		});
	}

	public interface InputStreamFunction<T> {

		T apply(InputStream inputStream) throws IOException;
	}

	public interface FileReaderFunction<T> {

		T apply(FileReader reader) throws IOException;
	}
}
