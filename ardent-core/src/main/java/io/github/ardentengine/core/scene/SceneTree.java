package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.resources.SceneResource;

import java.util.Objects;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree {

    /** The root of the current scene. */
    private Node root = null;

    /**
     * Changes the current scene to the given one.
     * <p>
     *     Calling this method causes {@link Node#exitTree()} to be called immediately on all the nodes in the current scene.
     *     After the new scene has been instantiated, {@link Node#enterTree(SceneTree)} will be called on all the nodes in the new scene.
     *     The {@link Node#onUpdate(float)} method will start being called from the next frame.
     * </p>
     * @param sceneResource The new scene.
     * @throws NullPointerException If the given scene is null.
     */
    public void changeScene(SceneResource sceneResource) {
        Objects.requireNonNull(sceneResource, "The new scene cannot be null");
        if(this.root != null) {
            this.root.exitTree();
        }
        this.root = sceneResource.instantiate();
        if(this.root != null) {
            this.root.enterTree(this);
        }
    }

    /**
     * Changes the current scene to the one contained in the {@link SceneResource} file at the given path.
     * <p>
     *     Calling this method causes {@link Node#exitTree()} to be called immediately on all the nodes in the current scene.
     *     After the new scene has been instantiated, {@link Node#enterTree(SceneTree)} will be called on all the nodes in the new scene.
     *     The {@link Node#onUpdate(float)} method will start being called from the next frame.
     * </p>
     *
     * @param file Path to the {@code SceneResource} file in the classpath.
     * @throws IllegalArgumentException If the given string is not a valid file path, or it does not point to a scene resource.
     */
    public void changeScene(String file) {
        var scene = SceneResource.getOrLoad(file);
        if(scene == null) {
            throw new IllegalArgumentException("Cannot change scene to " + file);
        }
        this.changeScene(scene);
    }

    /**
     * Returns the root of the current scene or null if no scene is running.
     *
     * @return The root of the current scene or null if no scene is running.
     */
    public Node getRoot() {
        return this.root;
    }
}