package gamma.engine.tree;

import gamma.engine.resources.Resources;
import gamma.engine.utils.Reflection;

import java.util.HashMap;

public final class NodeResource {

	public static NodeResource getOrLoad(String path) {
		if(path != null && !(path.isEmpty() || path.isBlank())) {
			Object resource = Resources.getOrLoad(path);
			if(resource instanceof NodeResource nodeResource) {
				return nodeResource;
			}
		}
		return new NodeResource();
	}

	public String type = "gamma.engine.tree.Node";
	public String override = "";
	public final HashMap<String, Object> properties = new HashMap<>();
	public final HashMap<String, NodeResource> children = new HashMap<>();

	public Node instantiate() {
		Node node;
		if(this.type == null || this.type.isEmpty()) {
			this.type = "gamma.engine.tree.Node";
		}
		if(this.override != null && !this.override.isEmpty()) {
			node = getOrLoad(this.override).instantiate();
		} else {
			node = (Node) Reflection.instantiate(this.type);
		}
		this.properties.forEach((name, value) -> Reflection.setField(node, name, value));
		this.children.forEach((key, resource) -> node.addChild(key, resource.instantiate()));
		return node;
	}
}
