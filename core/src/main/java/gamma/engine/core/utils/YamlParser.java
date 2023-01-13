package gamma.engine.core.utils;

import gamma.engine.core.resources.ShaderLoader;
import gamma.engine.core.tree.SubbranchLoader;
import org.lwjgl.opengl.GL20;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.Map;

/**
 * Utility class for parsing yaml files.
 *
 * @author Nico
 */
public final class YamlParser extends Constructor {

	/** Snakeyaml's Yaml object */
	private static final Yaml YAML = new Yaml(new YamlParser());

	static {
		YAML.setBeanAccess(BeanAccess.FIELD);
	}

	/**
	 * Loads a yaml file as the given type.
	 *
	 * @param file Path to the file to load in the classpath
	 * @param type Type of the result
	 * @return The loaded yaml file
	 * @param <T> Type of the result
	 */
	public static <T> T loadAs(String file, Class<T> type) {
		return FileUtils.inputStream(file, inputStream -> YAML.loadAs(inputStream, type));
	}

	/**
	 * Loads a yaml file to a {@link Map}.
	 *
	 * @param file Path to the file to load in the classpath
	 * @return A {@code Map} representing the yaml file
	 */
	public static Map<String, Object> loadAsMap(String file) {
		return FileUtils.inputStream(file, YAML::load);
	}

	/**
	 * Parses a yaml string to an object of the given type.
	 *
	 * @param yaml Yaml string
	 * @param type Type of the object to get
	 * @return The result of the parsing
	 * @param <T> Type of the object to get
	 */
	public static <T> T parseAs(String yaml, Class<T> type) {
		return YAML.loadAs(yaml, type);
	}

	/**
	 * Parses a yaml string to a {@link Map}.
	 *
	 * @param yaml Yaml string
	 * @return A {@code Map} representing the given yaml string
	 */
	public static Map<String, Object> parseAsMap(String yaml) {
		return YAML.load(yaml);
	}

	/**
	 * Adds constructors to the yaml constructor.
	 */
	private YamlParser() {
		super(new LoaderOptions());
		this.yamlConstructors.put(new Tag("!subbranch"), new SubbranchLoader());
		this.yamlConstructors.put(new Tag("!vertex"), new ShaderLoader(GL20.GL_VERTEX_SHADER));
		this.yamlConstructors.put(new Tag("!fragment"), new ShaderLoader(GL20.GL_FRAGMENT_SHADER));
	}
}
