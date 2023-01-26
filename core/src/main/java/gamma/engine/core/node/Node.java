package gamma.engine.core.node;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Base class that represents a node in the scene tree.
 *
 * @author Nico
 */
public class Node {

	/** HashMap of child nodes */
	private final HashMap<String, Node> children = new HashMap<>();
	/** Reference to the parent node */
	private Node parent;

	/**
	 * Adds a child to this node with the next available key.
	 *
	 * @param node The child to add
	 * @throws IllegalStateException if the given child already has a parent
	 */
	public final void addChild(Node node) {
		if(node.parent == null) {
			String key = node.getClass().getSimpleName();
			int index = 0;
			while(this.children.containsKey(key))
				key += index++;
			this.addChild(key, node);
		} else {
			throw new IllegalStateException("Cannot add node " + node + " as a child of " + this + " because it already has parent " + node.parent);
		}
	}

	/**
	 * Adds a child to this node with the given key.
	 *
	 * @param key The child's key
	 * @param node The child to add
	 * @throws IllegalStateException if the given child already has a parent or the given key already exists in this node
	 */
	public final void addChild(String key, Node node) {
		if(node.parent == null) {
			if(!this.children.containsKey(key)) {
				this.children.put(key, node);
				node.parent = this;
				node.enterTree();
			} else {
				throw new IllegalStateException("Node " + this + " already has a child with key " + key);
			}
		} else {
			throw new IllegalStateException("Cannot add node " + node + " as a child of " + this + " because it already has parent " + node.parent);
		}
	}

	/**
	 * Called from {@link Node#addChild(Node)} when this node enters the scene tree.
	 */
	private void enterTree() {
		this.children.forEach((key, node) -> {
			if(node.parent == null)
				node.parent = this;
			node.enterTree();
		});
		this.onEnterTree();
	}

	/**
	 * Called when this node is added as a child of another node.
	 */
	protected void onEnterTree() {

	}

	/**
	 * Called every frame from {@link SceneTree#process()}. Updates this node and all of its children.
	 *
	 * @param delta Time since the previous update in seconds
	 */
	public final void process(float delta) {
		this.children.forEach((key, node) -> node.process(delta));
		this.onUpdate(delta);
	}

	/**
	 * Updates this node and all of its children while in the editor.
	 */
	public final void editorProcess() {
		this.children.forEach((key, node) -> node.editorProcess());
		this.onUpdateInEditor();
	}

	/**
	 * Called every frame.
	 *
	 * @param delta Time since the previous update in seconds
	 */
	protected void onUpdate(float delta) {

	}

	/**
	 * Called every update frame while in the editor.
	 */
	protected void onUpdateInEditor() {

	}

	/**
	 * Checks if this node has a child with the given key.
	 *
	 * @param key The child's key
	 * @return True if this node has a child with the given key, otherwise false.
	 */
	public final boolean hasChild(String key) {
		return this.children.containsKey(key);
	}

	/**
	 * Removes the child with the given key from the scene tree.
	 *
	 * @param key The child's key
	 * @return The removed node
	 * @throws IllegalStateException if this node does not have a child with the given key
	 */
	public final Node removeChild(String key) {
		if(this.children.containsKey(key)) {
			Node node = this.children.remove(key);
			node.exitTree();
			node.parent = null;
			return node;
		} else {
			throw new IllegalStateException("Node " + this + " does not have a child with key " + key);
		}
	}

	/**
	 * Removes the given child from this node.
	 *
	 * @param node The child to remove
	 * @throws IllegalStateException if the given node is not a child of this node
	 */
	public final void removeChild(Node node) {
		if(this.children.values().remove(node)) {
			node.exitTree();
			node.parent = null;
		} else {
			throw new IllegalStateException("Node " + node + " is not a child of " + this);
		}
	}

	/**
	 * Called from {@link Node#removeChild(Node)} and {@link Node#removeChild(String)} when this node exits the scene tree.
	 */
	private void exitTree() {
		this.children.forEach((key, node) -> node.exitTree());
		this.onExitTree();
	}

	/**
	 * Called when this node is removed as a child of its parent.
	 */
	protected void onExitTree() {

	}

	/**
	 * Gets this node's parent or null if this is the root node or an orphan node.
	 *
	 * @return This node's parent or null if this is the root node or an orphan node
	 */
	public final Node getParent() {
		return this.parent;
	}

	/**
	 * Returns an {@link Optional} describing the child with the given key or an empty {@code Optional} if this node does not have a child with the given key.
	 *
	 * @param key The child's key
	 * @return An {@link Optional} describing the child with the given key or an empty {@code Optional} if this node does not have a child with the given key
	 */
	public final Optional<Node> getChild(String key) {
		return Optional.ofNullable(this.children.get(key));
	}

	/**
	 * Returns an {@link Optional} describing the first child of the given type or an empty {@code Optional} if this node does not have a child of the given type.
	 *
	 * @param type The type of the child to get
	 * @return An {@link Optional} describing the first child of the given type or an empty {@code Optional} if this node does not have a child of the given type
	 * @param <T> Type of the child to get
	 */
	public final <T extends Node> Optional<T> getChildOfType(Class<T> type) {
		return this.children.values().stream().filter(node -> type.isAssignableFrom(node.getClass())).findFirst().map(type::cast);
	}

	/**
	 * Returns a {@link Stream} of this node's children.
	 *
	 * @return A {@link Stream} of this node's children.
	 */
	public final Stream<Node> getChildren() {
		return this.children.values().stream();
	}

	/**
	 * Returns a {@link Stream} of all the children of this node of the given type.
	 *
	 * @param type The type of the children to get
	 * @return A {@link Stream} of all the children of this node of the given type
	 * @param <T> Type of the children to get
	 */
	public final <T extends Node> Stream<T> getChildrenOfType(Class<T> type) {
		return this.children.values().stream().filter(node -> type.isAssignableFrom(node.getClass())).map(type::cast);
	}

	// TODO: Make sure this is usable
	public final void removeFromTree() {
		if(this.parent != null)
			this.parent.removeChild(this);
		else
			throw new IllegalStateException("The root node cannot be removed from the tree");
	}
}
