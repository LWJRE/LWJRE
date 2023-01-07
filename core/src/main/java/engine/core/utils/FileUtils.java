package engine.core.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {

	public static void readFile(String file, IOConsumer ioConsumer, Consumer<IOException> exceptionConsumer) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			ioConsumer.accept(inputStream);
		} catch (IOException exception) {
			exceptionConsumer.accept(exception);
		}
	}

	public static void readFile(String file, IOConsumer ioConsumer) {
		readFile(file, ioConsumer, exception -> {
			System.err.println("Error loading file " + file);
			exception.printStackTrace();
		});
	}

	public static <T> T readFile(String file, IOFunction<T> ioFunction, Function<IOException, T> exceptionFunction) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			return ioFunction.apply(inputStream);
		} catch (IOException exception) {
			return exceptionFunction.apply(exception);
		}
	}

	public static void streamLines(String file, Consumer<Stream<String>> streamConsumer, Consumer<IOException> exceptionConsumer) {
		readFile(file, inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			streamConsumer.accept(reader.lines());
			reader.close();
		}, exceptionConsumer);
	}

	public static void streamLines(String file, Consumer<Stream<String>> streamConsumer) {
		streamLines(file, streamConsumer, exception -> {
			System.err.println("Error loading file " + file);
			exception.printStackTrace();
		});
	}

	public static <T> T streamLines(String file, Function<Stream<String>, T> streamFunction, Function<IOException, T> exceptionFunction) {
		return readFile(file, inputStream -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			T result = streamFunction.apply(reader.lines());
			reader.close();
			return result;
		}, exceptionFunction);
	}

	public static String readString(String file, Function<IOException, String> exceptionFunction) {
		return streamLines(file, stringStream -> stringStream.collect(Collectors.joining("\n")), exceptionFunction);
	}

	public static void readImage(String file, Consumer<BufferedImage> imageConsumer, Consumer<IOException> exceptionConsumer) {
		readFile(file, inputStream -> imageConsumer.accept(ImageIO.read(inputStream)), exceptionConsumer);
	}

	public static void readImage(String file, Consumer<BufferedImage> imageConsumer) {
		readImage(file, imageConsumer, exception -> {
			System.err.println("Error loading image file " + file);
			exception.printStackTrace();
		});
	}

	public static <T> T readImage(String file, Function<BufferedImage, T> imageFunction, Function<IOException, T> exceptionFunction) {
		return readFile(file, inputStream -> imageFunction.apply(ImageIO.read(inputStream)), exceptionFunction);
	}

	// TODO: Upgrade this
	public static <T> T parseYaml(String file) {
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		return readFile(file, yaml::load, exception -> null);
	}

	@FunctionalInterface
	public interface IOConsumer {

		void accept(InputStream inputStream) throws IOException;
	}

	@FunctionalInterface
	public interface IOFunction<R> {

		R apply(InputStream inputStream) throws IOException;
	}
}
