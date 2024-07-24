package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.resources.SceneResource;

import java.util.Objects;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree {

    /** The root of the current scene. */
    private Node root = null;
    /** The current 2D camera. */
    private Camera2D camera2D = null;
    /** The current 3D camera. */
    private Camera3D camera3D = null;

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

    /**
     * Returns the currently active 2D camera.
     * Returns null if there is no active camera.
     *
     * @return The currently active 2D camera.
     *
     * @see Camera2D#makeCurrent()
     */
    public Camera2D getCamera2D() {
        return this.camera2D;
    }

    /**
     * Package-protected method used to set the current 2D camera.
     * <p>
     *     This method is internal and should not be called from outside the {@link Camera2D} class.
     *     Use {@link Camera2D#makeCurrent()} instead.
     * </p>
     *
     * @param camera The camera.
     */
    void setCamera2D(Camera2D camera) {
        this.camera2D = camera;
    }

    /**
     * Returns the currently active 3D camera.
     * Returns null if there is no active camera.
     *
     * @return The currently active 3D camera.
     *
     * @see Camera3D#makeCurrent()
     */
    public Camera3D getCamera3D() {
        return this.camera3D;
    }

    /**
     * Package-protected method used to set the current 3D camera.
     * <p>
     *     This method is internal and should not be called from outside the {@link Camera3D} class.
     *     Use {@link Camera3D#makeCurrent()} instead.
     * </p>
     *
     * @param camera The camera.
     */
    void setCamera3D(Camera3D camera) {
        this.camera3D = camera;
    }
}