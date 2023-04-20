package gamma.engine.resources;

import gamma.engine.rendering.Model;
import gamma.engine.rendering.Shader;
import gamma.engine.scene.EntityResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import vecmatlib.color.Color3f;
import vecmatlib.color.Color4f;
import vecmatlib.vector.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class YamlSerializer extends Representer {

	private static final YamlSerializer INSTANCE;

	private static final Yaml YAML;

	static {
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setIndent(2);
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		dumperOptions.setIndicatorIndent(2);
		dumperOptions.setIndentWithIndicator(true);
		INSTANCE = new YamlSerializer(dumperOptions);
		YAML = new Yaml(INSTANCE, dumperOptions);
		YAML.setBeanAccess(BeanAccess.FIELD);
		addSequence(Vec2f.class, vec -> List.of(vec.x(), vec.y()));
		addSequence(Vec3f.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		addSequence(Vec4f.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		addSequence(Vec2i.class, vec -> List.of(vec.x(), vec.y()));
		addSequence(Vec3i.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		addSequence(Vec4i.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		addSequence(Color3f.class, color -> List.of(color.r(), color.g(), color.b()));
		addSequence(Color4f.class, color -> List.of(color.r(), color.g(), color.b(), color.a()));
		addScalar(Model.class, Resources::pathOf);
		addScalar(Shader.class, Resources::pathOf);
		addMapping(EntityResource.class, EntityResource::serialize);
	}

	public static String serialize(Object data) {
		return YAML.dump(data);
	}

	/**
	 * Adds a scalar represent. Determines how objects of the given class will be serialized.
	 *
	 * @param type Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as a string
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addScalar(Class<T> type, Function<T, String> represent) {
		addScalar(type, new Tag(type), represent);
	}

	public static <T> void addScalar(Class<T> type, String tag, Function<T, String> represent) {
		addScalar(type, new Tag(tag), represent);
	}

	private static <T> void addScalar(Class<T> type, Tag tag, Function<T, String> represent) {
		INSTANCE.representers.put(type, data -> INSTANCE.representScalar(tag, represent.apply(type.cast(data))));
	}

	/**
	 * Adds a sequence represent. Determines how objects of the given class will be serialized.
	 *
	 * @param type Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as an {@link Iterable}
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addSequence(Class<T> type, Function<T, Iterable<?>> represent) {
		addSequence(type, new Tag(type), represent);
	}

	public static <T> void addSequence(Class<T> type, String tag, Function<T, Iterable<?>> represent) {
		addSequence(type, new Tag(tag), represent);
	}

	private static <T> void addSequence(Class<T> type, Tag tag, Function<T, Iterable<?>> represent) {
		INSTANCE.representers.put(type, data -> INSTANCE.representSequence(tag, represent.apply(type.cast(data)), DumperOptions.FlowStyle.FLOW));
	}

	/**
	 * Adds a mapping represent. Determines how objects of the given class will be serialized.
	 *
	 * @param type Type of the object to serialize
	 * @param represent A {@link Function} that returns the serialization of the object as a {@link Map}
	 * @param <T> Type of the object to serialize
	 */
	public static <T> void addMapping(Class<T> type, Function<T, Map<?, ?>> represent) {
		addMapping(type, new Tag(type), represent);
	}

	public static <T> void addMapping(Class<T> type, String tag, Function<T, Map<?, ?>> represent) {
		addMapping(type, new Tag(tag), represent);
	}

	private static <T> void addMapping(Class<T> type, Tag tag, Function<T, Map<?, ?>> represent) {
		INSTANCE.representers.put(type, data -> INSTANCE.representMapping(tag, represent.apply(type.cast(data)), DumperOptions.FlowStyle.BLOCK));
	}

	private YamlSerializer(DumperOptions dumperOptions) {
		super(dumperOptions);
	}
}
