package gamma.engine.utils;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class YamlSerializer extends Representer {

	private static final YamlSerializer INSTANCE = new YamlSerializer();

	public static String serialize(Object data) {
		Yaml yaml = new Yaml(INSTANCE);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return yaml.dump(data);
	}

	public static void writeToFile(Object data, String path) {
		try {
			Yaml yaml = new Yaml(INSTANCE);
			yaml.setBeanAccess(BeanAccess.FIELD);
			yaml.dump(data, new FileWriter(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private YamlSerializer() {
		super(new DumperOptions());
		this.sequenceRepresent(Vec2f.class, "!Vec2f", vec -> List.of(vec.x(), vec.y()));
		this.sequenceRepresent(Vec3f.class, "!Vec3f", vec -> List.of(vec.x(), vec.y(), vec.z()));
		this.sequenceRepresent(Vec4f.class, "!Vec4f", vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		this.sequenceRepresent(Vec2i.class, "!Vec2i", vec -> List.of(vec.x(), vec.y()));
		this.sequenceRepresent(Vec3i.class, "!Vec3i", vec -> List.of(vec.x(), vec.y(), vec.z()));
		this.sequenceRepresent(Vec4i.class, "!Vec4i", vec -> List.of(vec.x(), vec.y(), vec.z(), vec.w()));
		this.sequenceRepresent(Color3f.class, "!Color3f", col -> List.of(col.r(), col.g(), col.b()));
		this.sequenceRepresent(Color4f.class, "!Color4f", col -> List.of(col.r(), col.g(), col.b(), col.a()));
	}

	private <T> void sequenceRepresent(Class<T> type, String tag, Function<T, Iterable<?>> function) {
		this.representers.put(type, data -> this.representSequence(new Tag(tag), function.apply(type.cast(data)), DumperOptions.FlowStyle.FLOW));
	}
}
