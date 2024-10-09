package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.resources.SceneResource;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree extends EngineSystem {

    /** The root of the current scene. */
    private Node root = null;
    /** Time elapsed since the previous update in nanoseconds. */
    private long previousTime = 0L;

    /** Scene to change to when {@link SceneTree#changeScene(SceneResource)} is called. */
    private SceneResource nextScene = null;

    // TODO: Tags/groups system

    /**
     * Requests the current scene to be changed to the given one.
     * <p>
     *     The scene is only changed at the start of the next frame to ensure two scenes are not running at the same time.
     * </p>
     * <p>
     *     This method causes {@link Node#onExit()} to be called at the end of the current frame.
     *     The {@link Node#onEnter()} method is called at the beginning of the next frame.
     * </p>
     * <p>
     *     If this method is called multiple times per frame, only the last given scene will be taken into consideration.
     *     This method has no effect if the given scene is null.
     * </p>
     *
     * @param sceneResource The scene resource.
     */
    public void changeScene(SceneResource sceneResource) {
        this.nextScene = sceneResource;
    }

    /**
     * Requests the current scene to be changed to the one contained in the {@link SceneResource} file at the given path.
     * <p>
     *     The scene is only changed at the start of the next frame to ensure two scenes are not running at the same time.
     * </p>
     * <p>
     *     This method causes {@link Node#onExit()} to be called at the end of the current frame.
     *     The {@link Node#onEnter()} method is called at the beginning of the next frame.
     * </p>
     * <p>
     *     If this method is called multiple times per frame, only the last given scene will be taken into consideration.
     * </p>
     *
     * @param file Path to the {@code SceneResource} file in the classpath.
     */
    public void changeScene(String file) {
        this.changeScene(SceneResource.getOrLoad(file));
    }

    @Override
    protected void initialize() {
        Input.setEventDispatchFunction(this::input);
        var mainScene = ApplicationProperties.getString("application.run.mainScene");
        if(!mainScene.isEmpty()) {
            this.changeScene(mainScene);
        }
        this.previousTime = System.nanoTime();
    }

    @Override
    protected void process() {
        var time = System.nanoTime();
        // Change scene if it was requested to change during the previous frame
        if(this.nextScene != null) {
            this.root = this.nextScene.instantiate();
            this.nextScene = null;
            if(this.root != null) {
                this.root.enterTree(this);
            }
        }
        if(this.root != null && this.root.isInsideTree()) {
            // Update the current scene
            this.root.update((time - this.previousTime) / 1_000_000_000.0f);
            // Exit the current scene if it was requested to change
            if(this.nextScene != null) {
                this.root.exitTree();
            }
        } else {
            // Quit if the root was removed from the scene
            Application.quit();
        }
        this.previousTime = time;
    }

    /**
     * Private method used for the input event dispatch function.
     *
     * @param event The input event.
     */
    private void input(InputEvent event) {
        if(this.root != null) {
            this.root.input(event);
        }
    }

    @Override
    protected void terminate() {
        if(this.root != null) {
            this.root.exitTree();
        }
    }

    /**
     * Returns the root of the current scene or null if no scene is running.
     *
     * @return The root of the current scene or null if no scene is running.
     */
    public Node root() {
        return this.root;
    }
}