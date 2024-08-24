package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.input.InputEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all scene objects.
 * A tree of nodes represents a scene.
 * <p>
 *     Scene objects are creating by extending a node class and overriding the necessary methods.
 *     <ul>
 *         <li>The method {@link Node#onEnter()} is called when the node enters the scene tree.</li>
 *         <li>The method {@link Node#onUpdate(float)} is called every frame.</li>
 *         <li>The method {@link Node#onInput(InputEvent)} is called every time there is an input event.</li>
 *         <li>The method {@link Node#onExit()} is called when the node exits the scene tree.</li>
 *     </ul>
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
     * Reference to the {@link SceneTree} this node is in.
     * Passed to the {@link Node#enterTree(SceneTree)} function when the node enters the scene.
     */
    private SceneTree sceneTree = null;
    /**
     * The parent of this node.
     */
    private Node parent = null;

    /**
     * Called when this node enters the scene tree, after it has been called on all its children.
     * <p>
     *     When a node is added to the scene using {@link Node#setParent(Node)} and the parent is inside the scene tree,
     *     this method is first called on the children of the added node, then on the node itself.
     * </p>
     * <p>
     *     This method won't be called if the parent is not inside the scene tree,
     *     but it will be called once the parent is added to the scene.
     * </p>
     */
    protected void onEnter() {

    }

    /**
     * Called every frame while this node is inside the scene tree.
     *
     * @param delta The time elapsed since the previous frame.
     */
    public void onUpdate(float delta) {

    }

    /**
     * Called every time there is an input event while this node is inside the scene tree.
     *
     * @param event The input event.
     */
    public void onInput(InputEvent event) {

    }

    /**
     * Called when this node exits the scene tree, after it has been called on all its children.
     * <p>
     *     When a node is removed from the scene using {@link Node#setParent(Node)} or {@link Node#removeFromParent()} and the parent is inside the scene tree,
     *     this method is first called on the children of the removed node, then on the node itself.
     * </p>
     * <p>
     *     This method won't be called if this node is not inside the scene tree when it is removed from its parent.
     * </p>
     */
    protected void onExit() {

    }

    /**
     * Package-protected method called when a node enters the scene tree.
     * Iterates through the children of this node and calls {@code enterTree} on each of them.
     *
     * @param sceneTree The scene tree this node is in.
     */
    final void enterTree(SceneTree sceneTree) {
        for(var child : this.children) {
            child.enterTree(sceneTree);
        }
        this.sceneTree = sceneTree;
        this.onEnter();
    }

    /**
     * Package-protected method called when a node exits the scene tree.
     * Iterates through the children of this node and calls {@code exitTree} on each of them.
     */
    final void exitTree() {
        for(var child : this.children) {
            child.exitTree();
        }
        this.onExit();
        this.sceneTree = null;
    }

    /**
     * Checks if this node is inside the scene tree.
     *
     * @return True if this node is inside the scene tree, otherwise false.
     */
    public final boolean isInsideTree() {
        return this.sceneTree != null;
    }

    /**
     * Returns the scene tree this node is in.
     * <p>
     *     Returns {@code null} if this node is not inside the scene tree.
     *     Use {@link Node#isInsideTree()} to check if the node is inside the scene tree.
     * </p>
     *
     * @return The scene tree this node is in.
     */
    public final SceneTree getSceneTree() {
        return this.sceneTree;
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
     * Adds this node as a child of the given parent.
     * <p>
     *     If the given parent is inside the scene tree, this method will effectively add this node to the scene tree.
     * </p>
     * <p>
     *     If the given parent is null, this method will remove this node from its parent.
     * </p>
     *
     * @param parent This node's new parent or null to remove this node from its parent.
     *
     * @see Node#getParent()
     * @see Node#isInsideTree()
     */
    public final void setParent(Node parent) {
        if(this.parent != null) {
            this.parent.children.remove(this);
            if(this.isInsideTree()) {
                this.exitTree();
            }
        }
        this.parent = parent;
        if(this.parent != null) {
            this.parent.children.add(this);
            if(this.parent.isInsideTree()) {
                this.enterTree(this.parent.sceneTree);
            }
        }
    }

    /**
     * Removes this node from its parent.
     * <p>
     *     This method has the same effect as calling {@link Node#setParent(Node)} with a null parent.
     *     If this node is inside the scene tree, this method will effectively remove it from the scene.
     * </p>
     * <p>
     *     This method has no effect if this node has no parent.
     * </p>
     *
     * @see Node#getParent()
     * @see Node#isInsideTree()
     */
    public final void removeFromParent() {
        this.setParent(null);
    }

    /**
     * Private method used to check if a child is valid before adding it to this node.
     *
     * @param node The child to check.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node already has a parent.
     */
    private void checkValidChild(Node node) {
        Objects.requireNonNull(node, "Cannot add null as a child of " + this);
        if(node.parent != null) {
            throw new IllegalArgumentException("Node " + node + " already has parent " + node.parent);
        }
    }

    /**
     * Adds the given node as a child of this node.
     * <p>
     *     If this node is inside the scene tree, this method will call {@link Node#onEnter()} on the given node and all its children.
     * </p>
     * <p>
     *     If the given node already has a parent, this method will throw an exception.
     *     Use {@link Node#setParent(Node)} to change a node's parent.
     * </p>
     *
     * @param node The child to add.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node already has a parent.
     */
    public final void addChild(Node node) {
        this.checkValidChild(node);
        this.children.add(node);
        node.parent = this;
        if(this.isInsideTree()) {
            node.enterTree(this.sceneTree);
        }
    }

    /**
     * Adds the given node as a child of this node.
     * <p>
     *     If this node is inside the scene tree, this method will call {@link Node#onEnter()} on the given node and all its children.
     * </p>
     * <p>
     *     If the given node already has a parent, this method will throw an exception.
     *     Use {@link Node#removeFromParent()} before adding the node again.
     * </p>
     *
     * @param node The child to add.
     * @param index Index at which the given child is to be inserted. Negative indices start from the end.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node already has a parent.
     * @throws IndexOutOfBoundsException If the given index is out of range.
     */
    public final void addChild(Node node, int index) {
        this.checkValidChild(node);
        if(index < 0) {
            index = this.children.size() + index;
        }
        this.children.add(index, node);
        node.parent = this;
        if(this.isInsideTree()) {
            node.enterTree(this.sceneTree);
        }
    }

    /**
     * Returns a view of the children of this node.
     * If nodes are added or removed from the given list, they won't be removed from the scene or added to it.
     *
     * @return A view of the children of this node.
     */
    public final List<Node> getChildren() {
        return new ArrayList<>(this.children);
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
     * Returns the child at the given index.
     *
     * @param index The index of the child. Negative indices start from the end.
     * @return The child at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public final Node getChild(int index) {
        if(index < 0) {
            return this.children.get(this.children.size() + index);
        }
        return this.children.get(index);
    }

    /**
     * Returns the first child of this node that is assignable to the given type.
     * Returns null if there is no such child.
     *
     * @param type The type of node to return.
     * @return The first child of this node that is assignable to the given type or null if there is no such child.
     * @param <T> The type of node to return.
     * @throws NullPointerException If the given class type is null.
     */
    public final <T extends Node> T getChild(Class<T> type) {
        Objects.requireNonNull(type, "The given class type cannot be null");
        for(var child : this.children) {
            if(type.isAssignableFrom(child.getClass())) {
                return type.cast(child);
            }
        }
        return null;
    }

    /**
     * Returns all children of this node that are assignable to the given type.
     * Returns an empty list if there are no children assignable to the given type.
     *
     * @param type The type of node to return.
     * @return A list containing all children of this node that are assignable to the given type.
     * @param <T> The type of node to return.
     * @throws NullPointerException If the given class type is null.
     */
    public final <T extends Node> List<T> getChildren(Class<T> type) {
        Objects.requireNonNull(type, "The given class type cannot be null");
        var result = new ArrayList<T>();
        for(var child : this.children) {
            if(type.isAssignableFrom(child.getClass())) {
                result.add(type.cast(child));
            }
        }
        return result;
    }

    /**
     * Returns the first child of this node with the given name.
     * Returns null if this node does not have any children with that name.
     *
     * @param name Name of the child to return.
     * @return The first child of this node with the given name or null if there is no such child.
     * @throws NullPointerException If the given name is null.
     */
    public final Node getChild(String name) {
        Objects.requireNonNull(name, "The given name cannot be null");
        for(var child : this.children) {
            if(name.equals(child.name)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Returns all children of this node with the given name.
     * Returns an empty list if there are no children with that name.
     *
     * @param name Name of the children to return.
     * @return A list containing all children of this node with the given name.
     * @throws NullPointerException If the given name is null.
     */
    public final List<Node> getChildren(String name) {
        Objects.requireNonNull(name, "The given name cannot be null");
        var result = new ArrayList<Node>();
        for(var child : this.children) {
            if(name.equals(child.name)) {
                result.add(child);
            }
        }
        return result;
    }

    /**
     * Returns the first child of this node with the given name that is assignable to the given type.
     * Returns null if there is no such child.
     *
     * @param type The type of node to return.
     * @param name Name of the child to return.
     * @return The first child of this node with the given name that is assignable to the given type or null if there is no such child.
     * @param <T> The type of node to return.
     * @throws NullPointerException If either the given class type or the given name are null.
     */
    public final <T extends Node> T getChild(Class<T> type, String name) {
        Objects.requireNonNull(type, "The given class type cannot be null");
        Objects.requireNonNull(name, "The given name cannot be null");
        for(var child : this.children) {
            if(child.getClass().isAssignableFrom(type) && name.equals(child.name)) {
                return type.cast(child);
            }
        }
        return null;
    }

    /**
     * Returns all children of this node with the given name that are assignable to the given type.
     * Returns an empty list if there are no children with that name that are assignable to the given type.
     *
     * @param type The type of node to return.
     * @param name Name of the children to return.
     * @return A list containing all children of this node with the given name that are assignable to the given type.
     * @param <T> The type of node to return.
     * @throws NullPointerException If either the given class type or the given name are null.
     */
    public final <T extends Node> List<T> getChildren(Class<T> type, String name) {
        Objects.requireNonNull(type, "The given class type cannot be null");
        Objects.requireNonNull(name, "The given name cannot be null");
        var result = new ArrayList<T>();
        for(var child : this.children) {
            if(child.getClass().isAssignableFrom(type) && name.equals(child.name)) {
                result.add(type.cast(child));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return (this.name.isEmpty() ? this.getClass().getSimpleName() : this.name) + "@" + this.hashCode();
    }
}