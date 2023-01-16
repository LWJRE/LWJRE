package gamma.engine.core.utils;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
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

	/** Snakeyaml's constructor object */
	private static final YamlParser PARSER = new YamlParser();
	/** Snakeyaml's Yaml object */
	private static final Yaml YAML = new Yaml(PARSER);

	static {
		YAML.setBeanAccess(BeanAccess.FIELD);
	}

	/**
	 * Adds a constructor for objects with the given tag.
	 * The constructor will be run whenever a yaml file contains said tag.
	 *
	 * @param tag The yaml tag in the format "!tag"
	 * @param construct The constructor to add
	 */
	public static void addConstructor(String tag, AbstractConstruct construct) {
		PARSER.yamlConstructors.put(new Tag(tag), construct);
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
	 * Constructs the constructor (don't ask).
	 */
	private YamlParser() {
		super(new LoaderOptions());
	}
}
