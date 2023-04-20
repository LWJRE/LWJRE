package gamma.engine.resources;

import gamma.engine.rendering.Model;
import gamma.engine.rendering.Shader;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.*;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class YamlParser extends Constructor {

	private static final YamlParser INSTANCE;

	private static final Yaml YAML;

	static {
		LoaderOptions loaderOptions = new LoaderOptions();
		loaderOptions.setTagInspector(tag -> true);
		loaderOptions.setAllowDuplicateKeys(false);
		INSTANCE = new YamlParser(loaderOptions);
		YAML = new Yaml(INSTANCE);
		YAML.setBeanAccess(BeanAccess.FIELD);
		addScalar(Model.class, Model::getOrLoad);
		addScalar(Shader.class, Shader::getOrLoad);
	}

	/**
	 * Parses a {@code .yaml} file at the given path in the classpath.
	 *
	 * @see Yaml#loadAs(InputStream, Class)
	 *
	 * @param path Path to the {@code .yaml} file in the classpath
	 * @param type Type of the object to deserialize
	 * @return The deserialized {@code .yaml} file
	 * @param <T> Type of the object to return
	 */
	public static <T> T parseResource(String path, Class<T> type) {
		return FileUtils.readResource(path, inputStream -> YAML.loadAs(inputStream, type));
	}

	public static Object parseResource(String path) {
		return FileUtils.readResource(path, YAML::load);
	}

	/**
	 * Parses a {@code .yaml} file at the given path.
	 *
	 * @see Yaml#loadAs(Reader, Class)
	 *
	 * @param path Path to the {@code .yaml} file
	 * @param type Type of the object to deserialize
	 * @return The deserialized {@code .yaml} file
	 * @param <T> Type of the object to return
	 */
	public static <T> T parseFile(String path, Class<T> type) {
		return FileUtils.readFile(path, reader -> YAML.loadAs(reader, type));
	}

	public static Object parseFile(String path) {
		return FileUtils.readFile(path, YAML::load);
	}

	/**
	 * Adds a scalar construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param type Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a string and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addScalar(Class<T> type, Function<String, T> construct) {
		addScalar(new Tag(type), construct);
	}

	public static void addScalar(String tag, Function<String, ?> construct) {
		addScalar(new Tag(tag), construct);
	}

	private static void addScalar(Tag tag, Function<String, ?> construct) {
		INSTANCE.yamlConstructors.put(tag, new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(INSTANCE.constructScalar((ScalarNode) node));
			}
		});
	}

	/**
	 * Adds a sequence construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param type Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a list and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addSequence(Class<T> type, Function<List<?>, T> construct) {
		addSequence(new Tag(type), construct);
	}

	public static void addSequence(String tag, Function<List<?>, ?> construct) {
		addSequence(new Tag(tag), construct);
	}

	private static <T> void addSequence(Tag tag, Function<List<?>, T> construct) {
		INSTANCE.yamlConstructors.put(tag, new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(INSTANCE.constructSequence((SequenceNode) node));
			}
		});
	}

	/**
	 * Adds a mapping construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param type Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a map and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addMapping(Class<T> type, Function<Map<Object, Object>, T> construct) {
		addMapping(new Tag(type), construct);
	}

	public static void addMapping(String tag, Function<Map<Object, Object>, ?> construct) {
		addMapping(new Tag(tag), construct);
	}

	private static <T> void addMapping(Tag tag, Function<Map<Object, Object>, T> construct) {
		INSTANCE.yamlConstructors.put(tag, new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(INSTANCE.constructMapping((MappingNode) node));
			}
		});
	}

	private YamlParser(LoaderOptions loaderOptions) {
		super(loaderOptions);
	}
}
