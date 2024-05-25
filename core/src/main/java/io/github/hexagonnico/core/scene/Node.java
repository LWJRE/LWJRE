package io.github.hexagonnico.core.scene;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base class for all scene objects.
 * A tree of nodes represents a scene.
 * <p>
 *     Scene objects are creating by extending a node class and overriding the necessary methods.
 *     The method {@link Node#onEnter()} is called when the node enters the scene tree.
 *     The method {@link Node#onUpdate(float)} is called every frame.
 *     The method {@link Node#onExit()} is called when the node exits the scene tree.
 * </p>
 */
public class Node {

    /**
     * The name of this node.
     */
    public String name = "";

    /**
     * List of child nodes.
     */
    private final ArrayList<Node> children = new ArrayList<>();
    /**
     * Keeps track of whether this node is inside the scene tree or not.
     */
    private boolean insideTree = false;
    /**
     * The parent of this node.
     */
    private Node parent = null;

    /**
     * Called when this node enters the scene tree, after it has been called on all its children.
     * <p>
     *     When a node is added to the scene using {@link Node#addChild(Node)} and the parent is inside the scene tree,
     *     this method is first called on the children of the added node, then on the node itself.
     * </p>
     * <p>
     *     This method won't be called if the parent is not inside the scene tree,
     *     but it will be called once the parent is added to the scene.
     * </p>
     *
     * @see Node#isInsideTree()
     */
    protected void onEnter() {

    }

    /**
     * Called every frame while this node is inside the scene tree.
     *
     * @param delta The time elapsed since the previous frame.
     */
    protected void onUpdate(float delta) {

    }

    // TODO: onInput

    /**
     * Called when this node exits the scene tree, after it has been called on all its children.
     * <p>
     *     When a node is removed from the scene using {@link Node#removeChild(Node)} or {@link Node#removeFromScene()} and the parent is inside the scene tree,
     *     this method is first called on the children of the removed node, then on the node itself.
     * </p>
     * <p>
     *     This method won't be called if this node is not inside the scene tree when it is removed from its parent.
     * </p>
     *
     * @see Node#isInsideTree()
     */
    protected void onExit() {

    }

    /**
     * Package-protected method called when a node enters the scene tree.
     * Iterates through the children of this node and calls {@code enterTree} on each of them.
     */
    final void enterTree() {
        for(Node child : this.children) {
            child.enterTree();
        }
        this.insideTree = true;
        this.onEnter();
    }

    /**
     * Package-protected method called when a node exits the scene tree.
     * Iterates through the children of this node and calls {@code exitTree} on each of them.
     */
    final void exitTree() {
        for(Node child : this.children) {
            child.exitTree();
        }
        this.onExit();
        this.insideTree = false;
    }

    /**
     * Adds the given node as a child of this node.
     * <p>
     *     Calls {@link Node#onEnter()} on the given node and all its children if this node is inside the scene tree.
     * </p>
     * <p>
     *     An error will be logged if the given node is null or if it already has a parent.
     * </p>
     *
     * @param node The child node to add. Must not be null and must not already have a parent.
     */
    public final void addChild(Node node) {
        if(node == null) {
            System.err.println("Cannot add null as a child of " + this);
        } else if(node.parent != null) {
            System.err.println("Node " + node + " already has parent " + node.parent);
        } else {
            this.children.add(node);
            node.parent = this;
            if(this.isInsideTree()) {
                node.enterTree();
            }
        }
    }

    /**
     * Adds the given node as a child of this node at the given index.
     * <p>
     *     Calls {@link Node#onEnter()} on the given node and all its children if this node is inside the scene tree.
     * </p>
     * <p>
     *     An error will be logged if the given node is null or if it already has a parent or if the given index is out of bounds.
     * </p>
     *
     * @param node The child node to add. Must not be null and must not already have a parent.
     * @param index Index of the child to add. Must be greater than zero and smaller than {@link Node#getChildCount()}.
     */
    public final void addChild(Node node, int index) {
        if(node.parent != null) {
            System.err.println("Node " + node + " already has parent " + node.parent);
        } else if(index < 0 || index >= this.getChildCount()) {
            System.err.println("Index " + index + " is out of bounds [0, " + this.children.size() + "]");
        } else {
            this.children.add(index, node);
            node.parent = this;
            if(this.isInsideTree()) {
                node.enterTree();
            }
        }
    }

    /**
     * Returns an immutable view of the children of this node.
     * Nodes may not be added or removed from the given list.
     *
     * @return An immutable view of the children of this node.
     *
     * @see List#copyOf(Collection)
     */
    public final List<Node> getChildren() {
        return List.copyOf(this.children);
    }

    /**
     * Returns the number of children of this node.
     *
     * @return The number of children of this node.
     */
    public final int getChildCount() {
        return this.children.size();
    }

    /**
     * Returns the child at the given index or null if the given index is out of bounds.
     * <p>
     *     This method works the same as {@link Node#getChild(int)}, but does not log an error if the given index is out of bounds.
     * </p>
     *
     * @param index The index of the child.
     * @return The child at the given index or null if the given index is out of bounds.
     */
    public final Node getChildOrNull(int index) {
        if(index >= 0 && index < this.children.size()) {
            return this.children.get(index);
        }
        return null;
    }

    /**
     * Returns the child at the given index.
     * Logs an error and returns null if the given index is out of bounds.
     *
     * @param index The index of the child.
     * @return The child at the given index or null if the given index is out of bounds.
     *
     * @see Node#getChildOrNull(int)
     */
    public final Node getChild(int index) {
        Node child = this.getChildOrNull(index);
        if(child == null) {
            System.err.println("Index " + index + " is out of bounds [0, " + this.children.size() + "]");
        }
        return child;
    }

    /**
     * Returns the first child of this node of the given type or null if this node has no children of the given type.
     * <p>
     *     This method works the same as {@link Node#getChild(Class)}, but does not log an error if this node has no children of the given type.
     * </p>
     *
     * @param type The type of node to return.
     * @return The first child of this node of the given type or null if this node has no children of the given type.
     * @param <T> The type of node to return.
     */
    public final <T extends Node> T getChildOrNull(Class<T> type) {
        for(Node child : this.children) {
            if(child.getClass().isAssignableFrom(type)) {
                return type.cast(child);
            }
        }
        return null;
    }

    /**
     * Returns the first child of this node of the given type.
     * Logs an error and returns null if this node has no children of the given type.
     *
     * @param type The type of node to return.
     * @return The first child of this node of the given type or null if this node has no children of the given type.
     * @param <T> The type of node to return.
     *
     * @see Node#getChildOrNull(Class)
     */
    public final <T extends Node> T getChild(Class<T> type) {
        T child = this.getChildOrNull(type);
        if(child == null) {
            System.err.println("Node " + this + " has no children of type " + type.getName());
        }
        return child;
    }

    /**
     * Returns the first child of this node with the given name or null if this node has no children with the given name.
     * <p>
     *     This method works the same as {@link Node#getChild(String)}, but does not log an error if this node has no children with the given name.
     * </p>
     *
     * @param name The name of the child to look for.
     * @return The first child of this node with the given name or null if this node has no children with the given name.
     */
    public final Node getChildOrNull(String name) {
        for(Node child : this.children) {
            if(child.name.equals(name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Returns the first child of this node with the given name.
     * Logs an error and returns null if this node has no children with the given name.
     *
     * @param name The name of the child to look for.
     * @return The first child of this node with the given name or null if this node has no children with the given name.
     *
     * @see Node#getChildOrNull(String)
     */
    public final Node getChild(String name) {
        Node child = this.getChildOrNull(name);
        if(child == null) {
            System.err.println("Node " + this + " has no children with name " + name);
        }
        return child;
    }

    /**
     * Returns the first child of this node of the given type with the given name or null if there is no such node.
     * <p>
     *     This method works the same as {@link Node#getChild(Class, String)}, but does not log an error if the node cannot be found.
     * </p>
     *
     * @param type The type of node to return.
     * @param name The name of the child to look for.
     * @return The first child of this node of the given type with the given name or null if there is no such node.
     * @param <T> The type of node to return.
     */
    public final <T extends Node> T getChildOrNull(Class<T> type, String name) {
        for(Node child : this.children) {
            if(child.getClass().isAssignableFrom(type) && child.name.equals(name)) {
                return type.cast(child);
            }
        }
        return null;
    }

    /**
     * Returns the first child of this node of the given type with the given name.
     * Logs an error and returns null if there is no such node.
     *
     * @param type The type of node to return.
     * @param name The name of the child to look for.
     * @return The first child of this node of the given type with the given name or null if there is no such node.
     * @param <T> The type of node to return.
     */
    public final <T extends Node> T getChild(Class<T> type, String name) {
        T child = this.getChildOrNull(type, name);
        if(child == null) {
            System.err.println("Node " + this + " has no children of type " + type.getName() + " with name " + name);
        }
        return child;
    }

    /**
     * Removes the given child from this node.
     * Logs an error if the given node is not a child of this node.
     *
     * @param node The child to remove.
     */
    public final void removeChild(Node node) {
        if(node.parent == this && this.children.remove(node)) {
            if(this.isInsideTree()) {
                node.exitTree();
            }
            node.parent = null;
        } else {
            System.err.println("Node " + node + " is not a child of " + this);
        }
    }

    /**
     * Removes the child at the given index from this node.
     * Logs an error if the given index is out of bounds.
     *
     * @param index The index of the child.
     */
    public final void removeChild(int index) {
        if(index < 0 || index >= this.children.size()) {
            System.err.println("Index " + index + " is out of bounds [0, " + this.children.size() + "]");
        } else {
            Node node = this.children.remove(index);
            if(this.isInsideTree()) {
                node.exitTree();
            }
            node.parent = null;
        }
    }

    // TODO: Remove child with name

    /**
     * Removes this node from the scene.
     */
    public final void removeFromScene() {
        if(this.isInsideTree()) {
            this.getParent().removeChild(this);
        }
    }

    /**
     * Returns this node's parent or null if this node does not have a parent.
     *
     * @return This node's parent or null if this node does not have a parent.
     */
    public final Node getParent() {
        return this.parent;
    }

    /**
     * Checks if this node is inside the scene tree.
     *
     * @return True if this node is inside the scene tree, otherwise false.
     */
    public final boolean isInsideTree() {
        return this.insideTree;
    }

    @Override
    public String toString() {
        return (this.name.isEmpty() ? this.getClass().getSimpleName() : this.name) + "@" + this.hashCode();
    }
}
