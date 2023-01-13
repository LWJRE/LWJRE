package gamma.engine.core.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FileUtils {

	/**
	 * Gets an input stream from a file in the classpath and applies the given function.
	 *
	 * @param file Path to the file in the classpath
	 * @param function Function to apply to the input stream
	 * @return The result of the given function
	 * @param <T> Return type of the given function
	 */
	public static <T> T inputStream(String file, IOFunction<T> function) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			return function.apply(inputStream);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Reads the content of a file as a string.
	 *
	 * @param file Path to the file to load
	 * @return The whole content of the file as a string
	 */
	public static String readAsString(String file) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String result = reader.lines().collect(Collectors.joining("\n"));
			reader.close();
			return result;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Reads an image file as a {@link BufferedImage}.
	 *
	 * @param file Path to the image file
	 * @return The image as a {@code BufferedImage}
	 */
	public static BufferedImage readImage(String file) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			return ImageIO.read(inputStream);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Returns an {@link Optional} representing an image file as a {@link BufferedImage} or an empty {@code Optional} if an exception occurs while loading the image.
	 *
	 * @param file Path to the image file
	 * @return An {@code Optional} representing an image file as a {@code BufferedImage} or an empty {@code Optional} if an exception occurs while loading the image
	 */
	public static Optional<BufferedImage> readImageOptional(String file) {
		try(InputStream inputStream = FileUtils.class.getResourceAsStream(file)) {
			if(inputStream == null)
				throw new FileNotFoundException("Could not find file " + file);
			return Optional.of(ImageIO.read(inputStream));
		} catch (IOException exception) {
			exception.printStackTrace();
			return Optional.empty();
		}
	}

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

	@FunctionalInterface
	public interface IOConsumer {

		void accept(InputStream inputStream) throws IOException;
	}

	@FunctionalInterface
	public interface IOFunction<R> {

		R apply(InputStream inputStream) throws IOException;
	}
}
