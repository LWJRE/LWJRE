package gamma.engine.core.utils;

import gamma.engine.core.resources.Resource;
import gamma.engine.core.resources.Resources;
import gamma.engine.core.scene.EntityResource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import vecmatlib.color.Color3f;
import vecmatlib.color.Color4f;
import vecmatlib.vector.*;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility class used to serialize {@code .yaml} files.
 *
 * @author Nico
 */
public final class YamlSerializer extends Representer {

	// TODO: Write tests

	/**
	 * Serializes the given object into a yaml string.
	 *
	 * @param data The object to serialize
	 * @return A yaml representation of the given object
	 */
	public static String serialize(Object data) {
		DumperOptions options = new DumperOptions();
		options.setSplitLines(false);
		options.setIndentWithIndicator(true);
		options.setIndicatorIndent(2);
		return new Yaml(new YamlSerializer(), options).dump(data);
	}

	/**
	 * Constructs yaml serializer.
	 */
	private YamlSerializer() {
		super(new DumperOptions());
		this.sequenceRepresent(Vec2f.class, vec -> List.of(vec.x(), vec.y()));
		this.sequenceRepresent(Vec3f.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		this.sequenceRepresent(Vec4f.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		this.sequenceRepresent(Vec2i.class, vec -> List.of(vec.x(), vec.y()));
		this.sequenceRepresent(Vec3i.class, vec -> List.of(vec.x(), vec.y(), vec.z()));
		this.sequenceRepresent(Vec4i.class, vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		this.sequenceRepresent(Color3f.class, col -> List.of(col.r(), col.g(), col.b()));
		this.sequenceRepresent(Color4f.class, col -> List.of(col.r(), col.g(), col.b(), col.a()));
		this.scalarRepresent(Resource.class, Resources::pathOf); // TODO: This does not work
		this.mappingRepresent(EntityResource.class, EntityResource::toMap);
	}

	/**
	 * Adds a scalar represent.
	 *
	 * @param type Class to represent
	 * @param function Represent function
	 * @param <T> Type of object to represent
	 */
	private <T> void scalarRepresent(Class<T> type, Function<T, String> function) {
		this.representers.put(type, data -> this.representScalar(new Tag(type), function.apply(type.cast(data))));
	}

	/**
	 * Adds a sequence represent.
	 *
	 * @param type Class to represent
	 * @param function Represent function
	 * @param <T> Type of object to represent
	 */
	private <T> void sequenceRepresent(Class<T> type, Function<T, Iterable<?>> function) {
		this.representers.put(type, data -> this.representSequence(new Tag(type), function.apply(type.cast(data)), DumperOptions.FlowStyle.FLOW));
	}

	/**
	 * Adds a mapping represent.
	 *
	 * @param type Class to represent
	 * @param function Represent function
	 * @param <T> Type of object to represent
	 */
	private <T> void mappingRepresent(Class<T> type, Function<T, Map<?, ?>> function) {
		this.representers.put(type, data -> this.representMapping(new Tag(type), function.apply(type.cast(data)), DumperOptions.FlowStyle.BLOCK));
	}
}
