package gamma.engine.core.tree;

import gamma.engine.core.utils.Reflection;
import gamma.engine.core.utils.YamlParser;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;

/**
 * Constructor used by snakeyaml to parse nodes tagged with {@code !subbranch}.
 * Used in {@link YamlParser}.
 */
public class SubbranchLoader extends AbstractConstruct {

	static {
		YamlParser.addConstructor("!subbranch", new SubbranchLoader());
	}

	/**
	 * Loads a subbranch file and returns the root node.
	 *
	 * @param file The file to load
	 * @return The root node of the loaded branch
	 */
	public static gamma.engine.core.tree.Node load(String file) {
		return YamlParser.loadAs(file, gamma.engine.core.tree.Node.class);
	}

	@Override
	public Object construct(Node node) {
		Object subbranchRoot = null;
		for(NodeTuple tuple : ((MappingNode) node).getValue()) {
			String key = ((ScalarNode) tuple.getKeyNode()).getValue();
			if(key.equals("file")) {
				subbranchRoot = YamlParser.loadAs(((ScalarNode) tuple.getValueNode()).getValue(), tuple.getValueNode().getType());
			} else if(key.equals("overrides") && subbranchRoot != null) {
				Object finalSubbranchRoot = subbranchRoot;
				YamlParser.parseAsMap(((ScalarNode) tuple.getValueNode()).getValue()).forEach((variable, value) -> Reflection.setField(finalSubbranchRoot, variable, value));
			}
			// TODO: Children of subbranch
		}
		return subbranchRoot;
	}
}
