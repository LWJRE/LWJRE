package gamma.engine.core.utils;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Utils class used to parse {@code .yaml} files.
 *
 * @author Nico
 */
public final class YamlParser {

	/** Yaml config object */
	private static final YamlConfig CONFIG = new YamlConfig();

	static {
		CONFIG.setPrivateFields(true);
		CONFIG.readConfig.setConstructorParameters(Vec2f.class, new Class[] {float.class, float.class}, new String[] {"x", "y"});
		CONFIG.readConfig.setConstructorParameters(Vec3f.class, new Class[] {float.class, float.class, float.class}, new String[] {"x", "y", "z"});
		CONFIG.readConfig.setConstructorParameters(Vec4f.class, new Class[] {float.class, float.class, float.class, float.class}, new String[] {"x", "y", "z", "w"});
	}

	/**
	 * Loads a {@code .yaml} file from the classpath.
	 *
	 * @param path Path to the file in the classpath
	 * @return The content of the {@code .yaml} file deserialized into an object. See {@link YamlReader#read()}.
	 */
	public static Object readResource(String path) {
		return Resources.readAs(path, inputStream -> {
			YamlReader reader = new YamlReader(new InputStreamReader(inputStream), CONFIG);
			Object result = reader.read();
			reader.close();
			return result;
		});
	}

	/**
	 * Loads a {@code .yaml} file from the classpath.
	 *
	 * @param path Path to the file in the classpath
	 * @return The content of the {@code .yaml} file deserialized into an object of the given type. See {@link YamlReader#read()}.
	 * @throws ClassCastException if the content of the {@code .yaml} file cannot be cast to the given type.
	 */
	public static <T> T readResource(String path, Class<T> type) {
		return Resources.readAs(path, inputStream -> {
			YamlReader reader = new YamlReader(new InputStreamReader(inputStream), CONFIG);
			T result = type.cast(reader.read());
			reader.close();
			return result;
		});
	}

	public static void writeToFile(Object object, String path) {
		try {
			YamlWriter writer = new YamlWriter(new FileWriter(path), CONFIG);
			writer.write(object);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	// TODO: Config options
}
