package gamma.engine.core.utils;

import gamma.engine.core.resources.Model;
import gamma.engine.core.resources.Resources;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Entity;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;
import vecmatlib.color.Color3f;
import vecmatlib.color.Color4f;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Static class that provides helper functions for serialization and parsing of {@code .yaml} files.
 *
 * @author Nico
 */
public class YamlUtils {

	/** Needed to add represents */
	private static final YamlSerializer SERIALIZER = new YamlSerializer();
	/** Needed to add constructs */
	private static final YamlParser PARSER = new YamlParser();
	/** Yaml object */
	private static final Yaml YAML;

	static {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setIndent(2);
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setIndicatorIndent(2);
		dumperOptions.setIndentWithIndicator(true);
		LoaderOptions loaderOptions = new LoaderOptions();
		loaderOptions.setTagInspector(tag -> true);
		YAML = new Yaml(PARSER, SERIALIZER, dumperOptions, loaderOptions);
		YAML.setBeanAccess(BeanAccess.FIELD);
		addSequenceRepresent(Vec2f.class, vec -> List.of(vec.x(), vec.y()));
		addSequenceRepresent(Vec3f.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		addSequenceRepresent(Vec4f.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		addSequenceRepresent(Color3f.class, color -> List.of(color.r(), color.g(), color.b()));
		addSequenceRepresent(Color4f.class, color -> List.of(color.r(), color.g(), color.b(), color.a()));
		addMappingRepresent(Entity.class, Entity::serialize);
		addMappingConstruct(Entity.class, Entity::deserialize);
		addScalarConstruct(Shader.class, Shader::getOrLoad);
		addScalarConstruct(Model.class, Model::getOrLoad);
		addScalarRepresent(Shader.class, Resources::pathOf);
		addScalarRepresent(Model.class, Resources::pathOf);
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

	/**
	 * Serializes the given object to a yaml string.
	 *
	 * @param object The object to serialize
	 * @return A yaml string.
	 */
	public static String serialize(Object object) {
		return YAML.dump(object);
	}

	/**
	 * Adds a scalar represent. Determines how objects of the given class will be serialized.
	 *
	 * @param tag Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as a string
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addScalarRepresent(Class<T> tag, Function<T, String> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representScalar(new Tag(tag), represent.apply(tag.cast(data))));
	}

	/**
	 * Adds a scalar construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param tag Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a string and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addScalarConstruct(Class<T> tag, Function<String, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructScalar((ScalarNode) node));
			}
		});
	}

	/**
	 * Adds a sequence represent. Determines how objects of the given class will be serialized.
	 *
	 * @param tag Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as an {@link Iterable}
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addSequenceRepresent(Class<T> tag, Function<T, Iterable<?>> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representSequence(new Tag(tag), represent.apply(tag.cast(data))));
	}

	/**
	 * Adds a sequence construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param tag Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a list and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addSequenceConstruct(Class<T> tag, Function<List<?>, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructSequence((SequenceNode) node));
			}
		});
	}

	/**
	 * Adds a mapping represent. Determines how objects of the given class will be serialized.
	 *
	 * @param tag Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as a {@link Map}
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addMappingRepresent(Class<T> tag, Function<T, Map<?, ?>> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representMapping(new Tag(tag), represent.apply(tag.cast(data))));
	}

	/**
	 * Adds a mapping construct. Determines how objects of the given class will be deserialized.
	 *
	 * @param tag Type of the object to deserialize
	 * @param construct A {@link Function} that takes in a map and returns the deserialized object
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addMappingConstruct(Class<T> tag, Function<Map<Object, Object>, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructMapping((MappingNode) node));
			}
		});
	}

	/**
	 * Extends {@link Constructor} to make protected functions visible from within this class.
	 */
	private static class YamlParser extends Constructor {

		/**
		 * Constructs a yaml parser.
		 */
		private YamlParser() {
			super(new LoaderOptions());
		}

		/**
		 * Gets {@link Constructor#yamlConstructors}.
		 *
		 * @return {@link Constructor#yamlConstructors}
		 */
		public Map<Tag, Construct> getConstructors() {
			return this.yamlConstructors;
		}

		@Override
		public String constructScalar(ScalarNode node) {
			return super.constructScalar(node);
		}

		@Override
		public List<?> constructSequence(SequenceNode node) {
			return super.constructSequence(node);
		}

		@Override
		public Map<Object, Object> constructMapping(MappingNode node) {
			return super.constructMapping(node);
		}
	}

	/**
	 * Extends {@link Representer} to make protected functions visible from within this class.
	 */
	private static class YamlSerializer extends Representer {

		/**
		 * Constructs a yaml serializer.
		 */
		private YamlSerializer() {
			super(new DumperOptions());
		}

		/**
		 * Gets {@link Representer#representers}.
		 *
		 * @return {@link Representer#representers}
		 */
		public Map<Class<?>, Represent> getRepresents() {
			return this.representers;
		}

//		@Override
//		protected Set<Property> getProperties(Class<?> type) {
//			return super.getProperties(type);
//		}

		@Override
		public Node representScalar(Tag tag, String value) {
			return super.representScalar(tag, value);
		}

		public Node representSequence(Tag tag, Iterable<?> sequence) {
			return super.representSequence(tag, sequence, DumperOptions.FlowStyle.FLOW);
		}

		public Node representMapping(Tag tag, Map<?, ?> mapping) {
			return super.representMapping(tag, mapping, DumperOptions.FlowStyle.BLOCK);
		}
	}
}
