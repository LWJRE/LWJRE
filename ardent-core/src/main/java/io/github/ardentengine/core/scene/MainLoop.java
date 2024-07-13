package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.input.InputEvent;
import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.input.Input;

import java.util.function.Consumer;

/**
 * Engine system that represents the main loop.
 * Contains the currently running {@link SceneTree}.
 * <p>
 *     This engine system has the lowest priority.
 *     It always runs last to ensure the rendering system and the display system are initialized before it.
 * </p>
 */
public final class MainLoop implements EngineSystem {

    /**
     * The main scene tree.
     */
    private final SceneTree sceneTree = new SceneTree();

    /**
     * Previous time obtained using {@link System#nanoTime()} and used to compute {@code delta}.
     */
    private long previousTime = 0;

    @Override
    public void initialize() {
        Input.setEventDispatchFunction(this::input);
        var mainScene = ApplicationProperties.getString("application.run.mainScene");
        if(!mainScene.isEmpty()) {
            this.sceneTree.changeScene(mainScene);
        }
    }

    /**
     * Recursive method used to process the scene tree.
     * Children are processed first.
     *
     * @param node The node to process.
     * @param delta Delta time.
     */
    private void process(Node node, float delta) {
        for(var child : node.getChildren()) {
            this.process(child, delta);
        }
        node.onUpdate(delta);
    }

    @Override
    public void process() {
        if(this.previousTime == 0) {
            this.previousTime = System.nanoTime();
        }
        var time = System.nanoTime();
        var delta = (time - this.previousTime) / 1_000_000_000.0f;
        if(this.sceneTree.getRoot() != null) {
            this.process(this.sceneTree.getRoot(), delta);
        }
        this.previousTime = time;
    }

    /**
     * Method used for {@link Input#setEventDispatchFunction(Consumer)}.
     * Sends input events to the scene tree.
     *
     * @param event The input event.
     */
    private void input(InputEvent event) {
        if(this.sceneTree.getRoot() != null) {
            this.input(this.sceneTree.getRoot(), event);
        }
    }

    /**
     * Recursive method used to send input events to the scene tree.
     * Input events are sent to children first.
     *
     * @param node The node to process.
     * @param event The input event.
     */
    private void input(Node node, InputEvent event) {
        for(var child : node.getChildren()) {
            this.input(child, event);
        }
        node.onInput(event);
    }

    @Override
    public void terminate() {
        if(this.sceneTree.getRoot() != null) {
            this.sceneTree.getRoot().exitTree();
        }
    }

    @Override
    public int priority() {
        return Integer.MAX_VALUE;
    }
}
