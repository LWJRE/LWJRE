package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.ApplicationProperties;
import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.input.Input;
import io.github.ardentengine.core.input.InputEvent;

/**
 * Class representing the currently running scene.
 */
public final class SceneSystem extends EngineSystem {

    /** Instance of the scene tree. */
    private final SceneTree sceneTree = new SceneTree();
    /** Time elapsed since the previous update in nanoseconds. */
    private long previousTime = 0L;

    @Override
    protected void initialize() {
        Input.setEventDispatchFunction(this::input);
        var mainScene = ApplicationProperties.getString("application.run.mainScene");
        if(!mainScene.isEmpty()) {
            this.sceneTree.changeScene(mainScene);
        }
        this.previousTime = System.nanoTime();
    }

    @Override
    protected void process() {
        var time = System.nanoTime();
        if(this.sceneTree.root() != null) {
            this.sceneTree.root().update((time - this.previousTime) / 1_000_000_000.0f);
        }
        this.previousTime = time;
    }

    private void input(InputEvent event) {
        if(this.sceneTree.root() != null) {
            this.sceneTree.root().input(event);
        }
    }

    @Override
    protected void terminate() {
        if(this.sceneTree.root() != null) {
            this.sceneTree.root().exitTree();
        }
    }
}