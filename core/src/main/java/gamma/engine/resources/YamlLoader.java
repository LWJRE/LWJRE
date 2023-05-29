package gamma.engine.resources;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.vector.*;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.List;
import java.util.function.Function;

/**
 * Implementation of a {@link ResourceLoader} to load {@code .yaml} files.
 *
 * @author Nico
 */
public final class YamlLoader extends Constructor implements ResourceLoader {

	// TODO: Allow adding things from static methods

	/**
	 * Constructs a new loader.
	 */
	public YamlLoader() {
		super(new Options());
		this.scalarConstruct("!getOrLoad", Resources::getOrLoad);
		this.floatSequenceConstruct("!Vec2f", list -> new Vec2f(list.get(0), list.get(1)));
		this.floatSequenceConstruct("!Vec3f", list -> new Vec3f(list.get(0), list.get(1), list.get(2)));
		this.floatSequenceConstruct("!Vec4f", list -> new Vec4f(list.get(0), list.get(1), list.get(2), list.get(3)));
		this.intSequenceConstruct("!Vec2i", list -> new Vec2i(list.get(0), list.get(1)));
		this.intSequenceConstruct("!Vec3i", list -> new Vec3i(list.get(0), list.get(1), list.get(2)));
		this.intSequenceConstruct("!Vec4i", list -> new Vec4i(list.get(0), list.get(1), list.get(2), list.get(3)));
		this.floatSequenceConstruct("!Color3f", list -> new Color3f(list.get(0), list.get(1), list.get(2)));
		this.floatSequenceConstruct("!Color4f", list -> new Color4f(list.get(0), list.get(1), list.get(2), list.get(3)));
	}

	private void scalarConstruct(String tag, Function<String, Object> function) {
		this.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(((ScalarNode) node).getValue());
			}
		});
	}

	private void floatSequenceConstruct(String tag, Function<List<Float>, Object> function) {
		this.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(((SequenceNode) node).getValue().stream().map(node1 -> Float.valueOf(((ScalarNode) node1).getValue())).toList());
			}
		});
	}

	private void intSequenceConstruct(String tag, Function<List<Integer>, Object> function) {
		this.yamlConstructors.put(new Tag(tag), new AbstractConstruct() {
			@Override
			public Object construct(Node node) {
				return function.apply(((SequenceNode) node).getValue().stream().map(node1 -> Integer.valueOf(((ScalarNode) node1).getValue())).toList());
			}
		});
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
}
