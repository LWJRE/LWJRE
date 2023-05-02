package gamma.engine.core.resources;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.function.Function;

public final class YamlLoader extends Constructor implements ResourceLoader {

	/**
	 * Constructs a new loader.
	 */
	public YamlLoader() {
		super(new Options());
		this.yamlConstructors.put(new Tag(Resource.class), new Construct(node -> Resources.getOrLoad(this.constructScalar((ScalarNode) node))));
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

	private static class Construct extends AbstractConstruct {

		private final Function<Node, Object> function;

		private Construct(Function<Node, Object> function) {
			this.function = function;
		}

		@Override
		public Object construct(Node node) {
			return this.function.apply(node);
		}
	}
}
