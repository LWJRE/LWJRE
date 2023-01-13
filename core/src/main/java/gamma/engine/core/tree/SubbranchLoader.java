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
