package io.github.view.utils;

import org.yaml.snakeyaml.Yaml;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {

	public static void readFile(String file, IOConsumer ioConsumer) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			ioConsumer.accept(inputStream);
		} catch(IOException e) {
			System.err.println("Error loading file " + file);
			e.printStackTrace();
		}
	}

	public static <T> T readFile(String file, IOFunction<T> action) throws IOException {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			return action.apply(inputStream);
		}
	}

	public static void streamLines(String file, Consumer<Stream<String>> streamConsumer) {
		readFile(file, inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			streamConsumer.accept(reader.lines());
			reader.close();
		});
	}

	public static <T> T streamLines(String file, Function<Stream<String>, T> streamFunction) throws IOException {
		return readFile(file, inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			T result = streamFunction.apply(reader.lines());
			reader.close();
			return result;
		});
	}

	public static String readString(String file) throws IOException {
		return streamLines(file, stringStream -> {
			return stringStream.collect(Collectors.joining("\n"));
		});
	}

	public static BufferedImage readImage(String file) throws IOException {
		return readFile(file, inputStream -> {
			return ImageIO.read(inputStream);
		});
	}

	public static PropertiesFile readProperties(String file) {
		try {
			return readFile(file, inputStream -> {
				PropertiesFile propertiesFile = new PropertiesFile();
				propertiesFile.load(inputStream);
				return propertiesFile;
			});
		} catch (IOException e) {
			System.err.println("Error loading file " + file);
			e.printStackTrace();
			return new PropertiesFile();
		}
	}

	public static <T> T parseYaml(String file) throws IOException {
		return readFile(file, inputStream -> {
			return new Yaml().load(inputStream);
		});
	}

	@FunctionalInterface
	private interface IOConsumer {

		void accept(InputStream inputStream) throws IOException;
	}

	@FunctionalInterface
	private interface IOFunction<R> {

		R apply(InputStream inputStream) throws IOException;
	}
}
