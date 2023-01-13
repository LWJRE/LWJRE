package gamma.engine.core.utils;

import gamma.engine.core.resources.ShaderLoader;
import org.lwjgl.opengl.GL20;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Tag;

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
	 * Adds constructors to the yaml constructor.
	 */
	private YamlParser() {
		super(new LoaderOptions());
		this.yamlConstructors.put(new Tag("!vertex"), new ShaderLoader(GL20.GL_VERTEX_SHADER));
		this.yamlConstructors.put(new Tag("!fragment"), new ShaderLoader(GL20.GL_FRAGMENT_SHADER));
	}
}
