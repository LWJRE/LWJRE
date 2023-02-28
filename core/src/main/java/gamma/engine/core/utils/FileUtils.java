package gamma.engine.core.utils;

import java.io.*;

public class FileUtils {

	public static <T> T readAs(String path, IOFunction<T> function) {
		try(FileReader reader = new FileReader(path)) {
			return function.apply(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public interface IOFunction<T> {

		T apply(Reader reader) throws IOException;
	}
}
