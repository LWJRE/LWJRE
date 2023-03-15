package gamma.engine.core.utils;

import gamma.engine.core.scene.Entity;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Construct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class YamlUtils {

	private static final YamlSerializer SERIALIZER = new YamlSerializer();
	private static final YamlParser PARSER = new YamlParser();
	private static final Yaml YAML;

	static {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setIndent(2);
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setIndicatorIndent(1);
		dumperOptions.setIndentWithIndicator(true);
		YAML = new Yaml(PARSER, SERIALIZER, dumperOptions);
		YAML.setBeanAccess(BeanAccess.FIELD);
		addSequenceRepresent(Vec2f.class, vec -> List.of(vec.x(), vec.y()));
		addSequenceRepresent(Vec3f.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		addSequenceRepresent(Vec4f.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		addMappingRepresent(Entity.class, Entity::serialize);
		addMappingConstruct(Entity.class, Entity::deserialize);
	}

	public static <T> T parseResource(String path, Class<T> type) {
		return FileUtils.readResource(path, inputStream -> YAML.loadAs(inputStream, type));
	}

	public static <T> T parseFile(String path, Class<T> type) {
		return FileUtils.readFile(path, reader -> YAML.loadAs(reader, type));
	}

	public static String serialize(Object object) {
		return YAML.dump(object);
	}

	public static <T> void addScalarRepresent(Class<T> tag, Function<T, String> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representScalar(new Tag(tag), represent.apply(tag.cast(data))));
	}

	public static <T> void addScalarConstruct(Class<T> tag, Function<String, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructScalar((ScalarNode) node));
			}
		});
	}

	public static <T> void addSequenceRepresent(Class<T> tag, Function<T, Iterable<?>> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representSequence(new Tag(tag), represent.apply(tag.cast(data))));
	}

	public static <T> void addSequenceConstruct(Class<T> tag, Function<List<?>, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructSequence((SequenceNode) node));
			}
		});
	}

	public static <T> void addMappingRepresent(Class<T> tag, Function<T, Map<?, ?>> represent) {
		SERIALIZER.getRepresents().put(tag, data -> SERIALIZER.representMapping(new Tag(tag), represent.apply(tag.cast(data))));
	}

	public static <T> void addMappingConstruct(Class<T> tag, Function<Map<Object, Object>, T> construct) {
		PARSER.getConstructors().put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return construct.apply(PARSER.constructMapping((MappingNode) node));
			}
		});
	}

	private static class YamlParser extends Constructor {

		private YamlParser() {
			super(new LoaderOptions());
		}

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

	private static class YamlSerializer extends Representer {

		private YamlSerializer() {
			super(new DumperOptions());
		}

		public Map<Class<?>, Represent> getRepresents() {
			return this.representers;
		}

		@Override
		protected Set<Property> getProperties(Class<?> type) {
			// TODO: Implement this
			return super.getProperties(type);
		}

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
