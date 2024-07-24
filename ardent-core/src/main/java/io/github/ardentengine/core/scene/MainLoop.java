package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;
import io.github.scalamath.vecmatlib.Mat2x3f;
import io.github.scalamath.vecmatlib.Mat3x4f;

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

    /** The main scene tree. */
    private final SceneTree sceneTree = new SceneTree();

    /** Previous time obtained using {@link System#nanoTime()} and used to compute {@code delta}. */
    private long previousTime = 0;

    @Override
    public void initialize() {
        Input.setEventDispatchFunction(this::input);
        var mainScene = ApplicationProperties.getString("application.run.mainScene");
        if(!mainScene.isEmpty()) {
            this.sceneTree.changeScene(mainScene);
        }
        this.previousTime = System.nanoTime();
    }

    @Override
    public void process() {
        var time = System.nanoTime();
        var delta = (time - this.previousTime) / 1_000_000_000.0f;
        if(this.sceneTree.getRoot() != null) {
            this.process(this.sceneTree.getRoot(), delta);
        }
        this.previousTime = time;
    }

    /**
     * Recursive method used to process the scene tree.
     * Children are processed first.
     *
     * @param node The node to process.
     * @param delta Delta time.
     */
    private void process(Node node, float delta) {
        if(node instanceof Node2D node2D) {
            this.process(node2D, delta, node2D.localTransform(), node2D.zIndex);
        } else if(node instanceof Node3D node3D) {
            this.process(node3D, delta, node3D.localTransform());
        } else {
            for(var child : node.getChildren()) {
                this.process(child, delta);
            }
        }
        node.onUpdate(delta);
    }

    /**
     * Recursive method used to process 2D nodes in the scene tree.
     * Keeps the transform and the z index of the given node for rendering.
     *
     * @param node The node to process.
     * @param delta Delta time.
     * @param transform The given node's global transform.
     * @param zIndex The given node's effective z index.
     */
    private void process(Node2D node, float delta, Mat2x3f transform, int zIndex) {
        // Parent nodes are rendered first
        if(node instanceof VisualInstance2D visualInstance) {
            visualInstance.render(transform, zIndex);
        }
        // Child nodes are processed first
        for(var child : node.getChildren()) {
            if(child instanceof Node2D child2D) {
                this.process(child2D, delta, transform.multiply(child2D.localTransform(), 0.0f, 0.0f, 1.0f), zIndex + child2D.zIndex);
            } else if(child instanceof Node3D child3D) {
                this.process(child3D, delta, child3D.localTransform());
            } else {
                this.process(child, delta);
            }
        }
        // Processing happens after rendering
        node.onUpdate(delta);
    }

    /**
     * Recursive method used to process 3D nodes in the scene tree.
     * Keeps the transform of the given node for rendering.
     *
     * @param node The node to process.
     * @param delta Delta time.
     * @param transform The given node's global transform.
     */
    private void process(Node3D node, float delta, Mat3x4f transform) {
        // Parent nodes are rendered first
        if(node instanceof VisualInstance3D visualInstance) {
            visualInstance.render(transform);
        }
        // Child nodes are processed first
        for(var child : node.getChildren()) {
            if(child instanceof Node2D child2D) {
                this.process(child2D, delta, child2D.localTransform(), child2D.zIndex);
            } else if(child instanceof Node3D child3D) {
                this.process(child3D, delta, transform.multiply(child3D.localTransform(), 0.0f, 0.0f, 0.0f, 1.0f));
            } else {
                this.process(child, delta);
            }
        }
        // Processing happens after rendering
        node.onUpdate(delta);
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