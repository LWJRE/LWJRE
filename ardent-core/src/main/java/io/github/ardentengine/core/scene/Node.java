package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.input.InputEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Base class for all scene objects.
 * A tree of nodes represents a scene.
 * <p>
 *     Scene objects are creating by extending a node class and overriding the necessary methods.
 * </p>
 * <ul>
 *     <li>The method {@link Node#onEnter()} is called when the node enters the scene tree.</li>
 *     <li>The method {@link Node#onUpdate(float)} is called every frame.</li>
 *     <li>The method {@link Node#onInput(InputEvent)} is called every time there is an input event.</li>
 *     <li>The method {@link Node#onExit()} is called when the node exits the scene tree.</li>
 * </ul>
 */
public class Node {

    /** The name of this node. */
    private String name = "";

    /** List of child nodes. */
    private final ArrayList<Node> children = new ArrayList<>();
    /** Reference to the {@link SceneTree} this node is in. */
    private SceneTree sceneTree = null;
    /** The parent of this node. */
    private Node parent = null;

    // TODO: Add pause mode and the ability to pause the scene

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
     */
    protected void onEnter() {

    }

    /**
     * Called when a node enters the scene tree.
     * Iterates through the children of this node and calls {@code enterTree} on each of them.
     * <p>
     *     This method is package-protected to ensure users cannot replace a node's functionality when overriding {@link Node#onEnter()}.
     * </p>
     */
    void enterTree(SceneTree sceneTree) {
        this.sceneTree = sceneTree;
        for(var child : this.children) {
            child.enterTree(sceneTree);
        }
        this.onEnter();
    }

    /**
     * Called every frame while this node is inside the scene tree.
     *
     * @param deltaTime The time elapsed since the previous frame.
     */
    protected void onUpdate(float deltaTime) {

    }

    /**
     * Called every frame while this node is inside the scene tree.
     * Iterates through the children of this node and calls {@code update} on each of them.
     * <p>
     *     This method is package-protected to ensure users cannot replace a node's functionality when overriding {@link Node#onUpdate(float)}.
     * </p>
     *
     * @param deltaTime The time elapsed since the previous frame.
     */
    void update(float deltaTime) {
        for(var child : this.children) {
            child.update(deltaTime);
        }
        this.onUpdate(deltaTime);
    }

    /**
     * Called every time there is an input event while this node is inside the scene tree.
     *
     * @param event The input event.
     */
    protected void onInput(InputEvent event) {

    }

    /**
     * Called every time there is an input event while this node is inside the scene tree.
     * Iterates through the children of this node and calls {@code input} on each of them.
     * <p>
     *     This method is package-protected to ensure users cannot replace a node's functionality when overriding {@link Node#onInput(InputEvent)}.
     * </p>
     *
     * @param event The input event.
     */
    void input(InputEvent event) {
        for(var child : this.children) {
            child.input(event);
        }
        this.onInput(event);
    }

    /**
     * Called when this node exits the scene tree, after it has been called on all its children.
     * <p>
     *     When a node is removed from the scene using {@link Node#removeFromTree()} and the parent is inside the scene tree,
     *     this method is first called on the children of the removed node, then on the node itself.
     * </p>
     * <p>
     *     This method won't be called if this node is not inside the scene tree when it is removed from its parent.
     * </p>
     */
    protected void onExit() {

    }

    /**
     * Called when a node exits the scene tree.
     * Iterates through the children of this node and calls {@code exitTree} on each of them.
     * <p>
     *     This method is package-protected to ensure users cannot replace a node's functionality when overriding {@link Node#onExit()}.
     * </p>
     */
    void exitTree() {
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
     *     Returns null if this node is not inside the scene tree.
     *     Use {@link Node#isInsideTree()} to check if the node is inside the scene tree.
     * </p>
     *
     * @return The scene tree this node is in.
     */
    public final SceneTree sceneTree() {
        return this.sceneTree;
    }

    /**
     * Returns the parent of this node or null if this node does not have a parent.
     *
     * @return The parent of this node or null if this node does not have a parent.
     */
    public final Node parent() {
        return this.parent;
    }

    /**
     * Private method used to check if a child is valid before adding it to this node.
     *
     * @param node The child to check.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node cannot be added as a child of this node.
     */
    private void checkValidChild(Node node) {
        Objects.requireNonNull(node, "Cannot add null as a child of " + this);
        if(node == this) {
            // The child cannot be the same node
            throw new IllegalArgumentException("Cannot add " + this + " as a child of itself");
        } else if(node.parent != null) {
            // A child node cannot be added if it already has a parent
            throw new IllegalArgumentException("Cannot add " + node + " as a child of " + this + " because it already has parent " + node.parent);
        } else if(node.isInsideTree()) {
            // The only node inside the scene tree to not have a parent is the root
            throw new IllegalArgumentException("Cannot add the root node as a child of " + this);
        } else if(!this.isInsideTree()) {
            // The given child might be an ancestor of this node if they are not inside the scene tree
            // If this node is inside the scene tree its ancestors already have a parent
            var parent = this.parent;
            while(parent != null) {
                if(parent == node) {
                    throw new IllegalArgumentException("Node " + node + " is an ancestor of " + this);
                }
                parent = parent.parent;
            }
        }
    }

    /**
     * Adds the given node as a child of this node.
     * This method can be used to add a node to the scene tree.
     * <p>
     *     If this node is inside the scene tree, this method will cause {@link Node#onEnter()} to be called.
     * </p>
     * <p>
     *     This method fails if the given node already has a parent.
     *     Use {@link Node#removeFromTree()} to remove a node from its parent.
     * </p>
     *
     * @param node The child to add.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node cannot be added as a child of this node.
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
     * Returns a view of the children of this node.
     * If nodes are added or removed from the returned list, they won't be removed from the scene or added to it.
     *
     * @return A view of the children of this node.
     */
    public final List<Node> children() {
        return new ArrayList<>(this.children);
    }

    /**
     * Returns the number of children of this node.
     *
     * @return The number of children of this node.
     */
    public final int childCount() {
        return this.children.size();
    }

    /**
     * Adds the given node as a child of this node.
     * This method can be used to add a node to the scene tree.
     * <p>
     *     If this node is inside the scene tree, this method will cause {@link Node#onEnter()} to be called.
     * </p>
     * <p>
     *     This method fails if the given node already has a parent.
     *     Use {@link Node#removeFromTree()} to remove a node from its parent.
     * </p>
     *
     * @param node The child to add.
     * @param index Sibling index of the child to add. Negative indices start from the end.
     * @throws NullPointerException If the given node is null.
     * @throws IllegalArgumentException If the given node cannot be added as a child of this node.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public final void addChild(Node node, int index) {
        this.checkValidChild(node);
        if(index > this.childCount() || index < -this.childCount() - 1) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + this.childCount());
        }
        if(index < 0) {
            this.children.add(this.childCount() + index + 1, node);
        } else {
            this.children.add(index, node);
        }
        node.parent = this;
        if(this.isInsideTree()) {
            node.enterTree(this.sceneTree);
        }
    }

    /**
     * Removes this node from its parent and from the scene tree.
     * Can be used to remove a child from its parent or to remove a node from the scene when it is no longer needed.
     * <p>
     *     This method will cause {@link Node#onExit()} to be called if it is inside the scene tree.
     * </p>
     */
    public final void removeFromTree() {
        // Remove this node from the scene tree
        if(this.isInsideTree()) {
            this.exitTree();
        }
        // Remove this node from its parent
        if(this.parent != null) {
            this.parent.children.remove(this);
            this.parent = null;
        }
    }

    /**
     * Sets the parent of this node.
     * <p>
     *     This method is equivalent to removing the node from the scene and adding it again to the given parent.
     *     Calls {@link Node#removeFromTree()} and {@link Node#addChild(Node)} in this order.
     * </p>
     *
     * @param parent The new parent of this node.
     * @throws NullPointerException If the given parent is null.
     * @throws IllegalArgumentException If this node cannot be added as a child of the given node.
     */
    public final void setParent(Node parent) {
        this.removeFromTree();
        parent.addChild(this);
    }

    /**
     * Returns the child at the given index.
     *
     * @param index The index of the child. Negative indices start from the end.
     * @return The child at the given index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public final Node getChild(int index) {
        if(index >= this.childCount() || index < -this.childCount()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + this.childCount());
        }
        if(index < 0) {
            return this.children.get(this.childCount() + index);
        }
        return this.children.get(index);
    }

    /**
     * Returns the first child of this node that is assignable to the given type or null if there is no such child.
     * <p>
     *     This method works the same as {@link Node#getChild(Class)}, but does not throw an exception if the child is not found.
     * </p>
     *
     * @param type Type of child to return.
     * @return The first child of the requested type.
     * @param <T> Type of the child to return.
     * @throws NullPointerException If the given type is null.
     */
    public final <T extends Node> T getChildOrNull(Class<T> type) {
        Objects.requireNonNull(type, "The given type cannot be null");
        for(var child : this.children) {
            if(type.isAssignableFrom(child.getClass())) {
                return type.cast(child);
            }
        }
        return null;
    }

    /**
     * Returns the first child of this node that is assignable to the given type.
     *
     * @param type Type of child to return.
     * @return The first child of the requested type.
     * @param <T> Type of the child to return.
     * @throws NullPointerException If the given type is null.
     * @throws NoSuchElementException If this node has no child that is assignable to the given type.
     */
    public final <T extends Node> T getChild(Class<T> type) {
        var child = this.getChildOrNull(type);
        if(child == null) {
            throw new NoSuchElementException("Node " + this + " has no children of type " + type.getName());
        }
        return child;
    }

    /**
     * Returns all children of this node that are assignable to the given type.
     *
     * @param type Type of children to return.
     * @return A list containing the children of this node that are assignable to the given type.
     * @param <T> Type of children to return.
     * @throws NullPointerException If the given type is null.
     */
    public final <T extends Node> List<T> getChildren(Class<T> type) {
        Objects.requireNonNull(type, "The given type cannot be null");
        var result = new ArrayList<T>();
        for(var child : this.children) {
            if(type.isAssignableFrom(child.getClass())) {
                result.add(type.cast(child));
            }
        }
        return result;
    }

    /**
     * Setter method for {@link Node#name}.
     * <p>
     *     The name of the node affects how it appears in the editor and the result of {@link Node#toString()}.
     * </p>
     * <p>
     *     If the name of the node is an empty string, the editor and the {@code toString()} method will show {@link Class#getSimpleName()}.
     * </p>
     * <p>
     *     The name of a node is not required to be unique among its siblings.
     * </p>
     *
     * @param name The name of the node. Cannot be null. Can be an empty string.
     */
    public final void setName(String name) {
        this.name = Objects.requireNonNull(name, "The given name cannot be null");
    }

    /**
     * Getter method for {@link Node#name}.
     * Can be an empty string.
     *
     * @return The name of this node.
     */
    public final String name() {
        return this.name;
    }

    /**
     * Returns the first child of this node that satisfies the given predicate or null if there is no such child.
     *
     * @param predicate A predicate that returns true for the child to be returned.
     * @return The first child of this node that satisfies the given predicate or null if there is no such child.
     * @throws NullPointerException If the given predicate is null.
     */
    public final Node findChild(Predicate<Node> predicate) {
        Objects.requireNonNull(predicate, "The given predicate cannot be null");
        for(var child : this.children) {
            if(predicate.test(child)) {
                return child;
            }
        }
        return null;
    }

    /**
     * Returns all children of this node that satisfy the given predicate.
     *
     * @param predicate A predicate that returns true for the children to be returned.
     * @return A list containing the children of this node that satisfy the given predicate.
     * @throws NullPointerException If the given predicate is null.
     */
    public final List<Node> findChildren(Predicate<Node> predicate) {
        Objects.requireNonNull(predicate, "The given predicate cannot be null");
        var result = new ArrayList<Node>();
        for(var child : this.children) {
            if(predicate.test(child)) {
                result.add(child);
            }
        }
        return result;
    }

    /**
     * Returns the sibling index of this node.
     * <p>
     *     Nodes are siblings if they share the same direct parent.
     *     The sibling index represents the order in which siblings appear in the tree and the order in which they are processed.
     * </p>
     * <p>
     *     Returns -1 if this node has no parent.
     * </p>
     *
     * @return The sibling index of this node or -1 if this node has no parent.
     */
    public final int siblingIndex() {
        if(this.parent != null) {
            return this.parent.children.indexOf(this);
        }
        return -1;
    }

    /**
     * Sets the sibling index of this node.
     * <p>
     *     Nodes are siblings if they share the same direct parent.
     *     The sibling index represents the order in which siblings appear in the tree and the order in which they are processed.
     * </p>
     * <p>
     *     This method has no effect if this node has no parent.
     * </p>
     *
     * @param index Index of this node among its siblings. Negative indices start from the end.
     * @return True if the sibling index of this node was updated correctly, false if this node has no parent.
     * @throws IndexOutOfBoundsException If the given index is out of range.
     */
    public final boolean setSiblingIndex(int index) {
        if(this.parent != null) {
            if(index >= this.parent.childCount() || index < -this.parent.childCount()) {
                throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + this.parent.childCount());
            }
            this.parent.children.remove(this);
            if(index < 0) {
                this.parent.children.add(this.parent.childCount() + 1 + index, this);
            } else {
                this.parent.children.add(index, this);
            }
            return true;
        }
        return false;
    }

    /**
     * Returns the first descendant of this node that satisfies the given predicate or null if there is no such node.
     * This method performs a recursive descend through the tree.
     *
     * @param predicate A predicate that returns true for the node to be returned.
     * @return The first descendant of this node that satisfies the given predicate or null if there is no such node.
     * @throws NullPointerException If the given predicate is null.
     */
    public final Node findNode(Predicate<Node> predicate) {
        Objects.requireNonNull(predicate, "The given predicate cannot be null");
        for(var child : this.children) {
            if(predicate.test(child)) {
                return child;
            }
        }
        for(var child : this.children) {
            var node = child.findNode(predicate);
            if(node != null) {
                return node;
            }
        }
        return null;
    }

    /**
     * Private method used to recursively descend through the tree when using {@link Node#findNodes(Predicate)}.
     *
     * @param nodes Resulting list.
     * @param predicate A predicate that returns true for the nodes to be returned.
     */
    private void findNodes(List<Node> nodes, Predicate<Node> predicate) {
        for(var child : this.children) {
            if(predicate.test(child)) {
                nodes.add(child);
            }
            child.findNodes(nodes, predicate);
        }
    }

    /**
     * Returns all descendants of this node that satisfy the given predicate.
     * This method performs a recursive descend through the tree.
     *
     * @param predicate A predicate that returns true for the nodes to be returned.
     * @return A list containing all descendants of this node that satisfy the given predicate.
     * @throws NullPointerException If the given predicate is null.
     */
    public final List<Node> findNodes(Predicate<Node> predicate) {
        Objects.requireNonNull(predicate, "The given predicate cannot be null");
        var result = new ArrayList<Node>();
        this.findNodes(result, predicate);
        return result;
    }

    @Override
    public String toString() {
        return (this.name.isEmpty() ? this.getClass().getSimpleName() : this.name) + "@" + Integer.toHexString(this.hashCode());
    }
}