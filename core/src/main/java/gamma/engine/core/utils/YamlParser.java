package gamma.engine.core.utils;

import gamma.engine.core.scene.Entity;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Utils class used to parse {@code .yaml} files.
 *
 * @author Nico
 */
public final class YamlParser extends Yaml {

	private static final YamlRepresenter REPRESENTER = new YamlRepresenter();
	private static final YamlConstructor CONSTRUCTOR = new YamlConstructor();
	private static final YamlParser PARSER = new YamlParser(CONSTRUCTOR, REPRESENTER, new DefaultDumperOptions());

	static {
		addSequenceRepresent(Vec2f.class, vec -> List.of(vec.x(), vec.y()));
		addSequenceRepresent(Vec3f.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		addSequenceRepresent(Vec4f.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		addClassTag(Entity.class, "!!gamma.engine.core.scene.Entity");
		addMappingRepresent(Entity.class, Entity::serialize);
		addMappingConstructor(Entity.class, Entity::deserialize);
	}

	public static <T> T loadResource(String path, Class<T> type) {
		return Resources.readAs(path, inputStream -> PARSER.loadAs(inputStream, type));
	}

	public static String serialize(Object object) {
		return PARSER.dump(object);
	}

	public static <T> void addScalarRepresent(Class<T> type, Function<T, String> represent) {
		REPRESENTER.addScalarRepresent(type, represent);
	}

	public static <T> void addSequenceRepresent(Class<T> type, Function<T, Iterable<?>> represent) {
		REPRESENTER.addSequenceRepresent(type, represent);
	}

	public static <T> void addMappingRepresent(Class<T> type, Function<T, Map<?, ?>> represent) {
		REPRESENTER.addMappingRepresent(type, represent);
	}

	public static void addMappingConstructor(Class<?> tag, Function<Map<Object, Object>, Object> construct) {
		CONSTRUCTOR.addMappingConstructor(tag, construct);
	}

	// TODO: Figure out when this is needed and add it automatically
	public static void addClassTag(Class<?> type, String tag) {
		REPRESENTER.addClassTag(type, new Tag(tag));
	}

	private static class YamlRepresenter extends Representer {

		private YamlRepresenter() {
			super(new DumperOptions());
		}

		private <T> void addScalarRepresent(Class<T> tag, Function<T, String> represent) {
			this.representers.put(tag, data -> this.representScalar(new Tag("!!" + tag.getName()), represent.apply(tag.cast(data))));
		}

		private <T> void addSequenceRepresent(Class<T> tag, Function<T, Iterable<?>> represent) {
			this.representers.put(tag, data -> this.representSequence(new Tag("!!" + tag.getName()), represent.apply(tag.cast(data)), DumperOptions.FlowStyle.FLOW));
		}

		private <T> void addMappingRepresent(Class<T> tag, Function<T, Map<?, ?>> represent) {
			this.representers.put(tag, data -> this.representMapping(new Tag("!!" + tag.getName()), represent.apply(tag.cast(data)), DumperOptions.FlowStyle.BLOCK));
		}
	}

	private static class YamlConstructor extends Constructor {

		private YamlConstructor() {
			super(new LoaderOptions());
		}

		private void addMappingConstructor(Class<?> tag, Function<Map<Object, Object>, Object> construct) {
			this.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
				@Override
				public Object construct(Node node) {
					return construct.apply(constructMapping((MappingNode) node));
				}
			});
		}
	}

	private static class DefaultDumperOptions extends DumperOptions {

		private DefaultDumperOptions() {
			this.setIndent(2);
			this.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
			this.setIndicatorIndent(1);
			this.setIndentWithIndicator(true);
		}
	}

	private YamlParser(BaseConstructor constructor, Representer representer, DumperOptions dumperOptions) {
		super(constructor, representer, dumperOptions);
		this.setBeanAccess(BeanAccess.FIELD);
	}
}
