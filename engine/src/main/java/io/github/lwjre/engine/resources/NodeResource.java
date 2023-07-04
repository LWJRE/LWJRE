package io.github.lwjre.engine.resources;

import io.github.lwjre.engine.nodes.Node;
import io.github.lwjre.engine.utils.Reflection;
import io.github.lwjre.engine.utils.ReflectionException;
import io.github.lwjre.engine.utils.YamlSerializer;

import java.util.HashMap;
import java.util.Objects;

/**
 * Class that represents a {@link Node} as a resource.
 * A {@code NodeResource} only needs to be loaded once but can be instantiated many times to create copies of the same node.
 *
 * @author Nico
 */
public final class NodeResource {

	static {
		// Static initializer to add the serialization function to YamlSerializer
		YamlSerializer.mappingRepresent(NodeResource.class, nodeResource -> {
			HashMap<String, Object> values = new HashMap<>();
			if(nodeResource.override != null && !nodeResource.override.isEmpty()) {
				values.put("override", nodeResource.override);
			} else if(nodeResource.type != null && !nodeResource.type.isEmpty()) {
				values.put("type", nodeResource.type);
			}
			if(!nodeResource.properties.isEmpty()) {
				values.put("properties", nodeResource.properties);
			}
			if(!nodeResource.children.isEmpty()) {
				values.put("children", nodeResource.children);
			}
			return values;
		});
	}

	/**
	 * Loads a new {@code NodeResource} or returns the same instance if it was already loaded.
	 * Returns an empty {@link NodeResource#NodeResource()} if the given path is null or empty or if the resource at the given path is not a {@code NodeResource}.
	 *
	 * @param path Path to the {@code NodeResource} to load or to get
	 *
	 * @return The requested {@code NodeResource}
	 */
	public static NodeResource getOrLoad(String path) {
		if(path != null && !(path.isEmpty() || path.isBlank())) {
			Object resource = Resources.getOrLoad(path);
			if(resource instanceof NodeResource nodeResource) {
				return nodeResource;
			}
		}
		return new NodeResource();
	}

	/** Fully qualified class name of the node that this resource represents. By default {@code "io.github.lwjre.engine.nodes.Node"}. */
	public String type;
	/** Path to the {@code NodeResource} that this one overrides or an empty string if this node does not override anything */
	public String override = "";
	/** Map containing the node's properties to be set when it is instantiated. Only needs to contain the properties that differ from their default value. */
	public final HashMap<String, Object> properties = new HashMap<>();
	/** Map containing the node's children as {@code NodeResource}s */
	public final HashMap<String, NodeResource> children = new HashMap<>();

	/**
	 * Constructs an empty {@code NodeResource}, i.e., a node of type {@link Node} with no properties and no children.
	 */
	public NodeResource() {
		this(Node.class.getName());
	}

	/**
	 * Constructs a {@code NodeResource} for a node of the given type.
	 *
	 * @param type Fully qualified class name of the node that this {@code NodeResource} represents
	 */
	public NodeResource(String type) {
		this.type = type;
	}

	/**
	 * Copy constructor that creates a deep copy of the given {@link NodeResource}.
	 *
	 * @param copy The {@link NodeResource} to copy
	 */
	public NodeResource(NodeResource copy) {
		this(copy.type);
		this.override = copy.override;
		this.properties.putAll(copy.properties);
		copy.children.forEach((key, child) -> this.children.put(key, new NodeResource(child)));
	}

	/**
	 * Creates an instance of the node that this {@code NodeResource} represents.
	 * Instantiates a node of type {@link NodeResource#type} and sets its fields according the values stored in {@link NodeResource#properties}.
	 * An error is printed to the console if {@link NodeResource#properties} contains properties that are not fields in the node's class.
	 * If this {@code NodeResource} has an {@link NodeResource#override} the override resource is instantiated first.
	 * The node's children are instantiated recursively.
	 *
	 * @return A new instance of a node
	 *
	 * @throws RuntimeException If a {@link ReflectionException} occurs when instantiating the node
	 */
	public Node instantiate() {
		try {
			Node node;
			if(this.override != null && !this.override.isEmpty()) {
				node = getOrLoad(this.override).instantiate();
			} else if(this.type == null || this.type.isEmpty()) {
				node = Reflection.instantiate(Node.class);
			} else {
				node = (Node) Reflection.instantiate(this.type);
			}
			this.properties.forEach((name, value) -> {
				try {
					Reflection.setField(node, name, value);
				} catch (ReflectionException e) {
					e.printStackTrace();
				}
			});
			this.children.forEach((key, resource) -> node.addChild(key, resource.instantiate()));
			return node;
		} catch (ReflectionException e) {
			throw new RuntimeException("Could not instantiate " + this, e);
		}
	}

	@Override
	public boolean equals(Object object) {
		if(this == object) {
			return true;
		} else if(object == null || getClass() != object.getClass()) {
			return false;
		} else {
			NodeResource that = (NodeResource) object;
			return Objects.equals(this.type, that.type) &&
				Objects.equals(this.override, that.override) &&
				Objects.equals(this.properties, that.properties) &&
				Objects.equals(this.children, that.children);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.type, this.override, this.properties, this.children);
	}
}
