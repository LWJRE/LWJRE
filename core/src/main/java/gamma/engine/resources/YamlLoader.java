package gamma.engine.resources;

import gamma.engine.rendering.Model;
import gamma.engine.rendering.Shader;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.function.Function;

/**
 * Implementation of a {@link ResourceLoader} to load {@code .yaml} files.
 *
 * @author Nico
 */
public final class YamlLoader extends Constructor implements ResourceLoader {

	/**
	 * Constructs a new loader.
	 */
	public YamlLoader() {
		super(new Options());
		this.yamlConstructors.put(new Tag(Model.class), new ConstructScalar(node -> Model.getOrLoad(this.constructScalar(node))));
		this.yamlConstructors.put(new Tag(Shader.class), new ConstructScalar(node -> Shader.getOrLoad(this.constructScalar(node))));
	}

	@Override
	public Object load(String path) {
		Yaml yaml = new Yaml(this);
		yaml.setBeanAccess(BeanAccess.FIELD);
		return FileUtils.readResource(path, yaml::load);
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".yaml", ".yml"};
	}

	/**
	 * Needed because {@link LoaderOptions} need to be passed in the constructor of {@link Constructor}.
	 */
	private static class Options extends LoaderOptions {

		/**
		 * Set default options.
		 */
		private Options() {
			this.setTagInspector(tag -> true);
			this.setAllowDuplicateKeys(false);
		}
	}

	/**
	 * Used to construct scalar properties.
	 */
	private static class ConstructScalar extends AbstractConstruct {

		/** Construct function */
		private final Function<ScalarNode, Object> function;

		/**
		 * Constructs a construct (I know...).
		 *
		 * @param function Construct function
		 */
		private ConstructScalar(Function<ScalarNode, Object> function) {
			this.function = function;
		}

		@Override
		public Object construct(Node node) {
			return this.function.apply((ScalarNode) node);
		}
	}

	// TODO: Add other constructs
}
