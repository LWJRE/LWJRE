package io.github.hexagonnico.core.scene;

import io.github.hexagonnico.core.resources.SceneResource;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree {

    /**
     * The root of the current scene.
     */
    private Node root = null;

    public void changeScene(SceneResource sceneResource) {
        if(this.root != null) {
            this.root.exitTree();
        }
        this.root = sceneResource.instantiate();
        if(this.root != null) {
            this.root.enterTree(this);
        }
    }

    public void changeScene(String file) {
        var scene = SceneResource.getOrLoad(file);
        if(scene != null) {
            this.changeScene(scene);
        }
    }

    /**
     * Returns the root of the current scene or null if no scene is running.
     *
     * @return The root of the current scene or null if no scene is running.
     */
    public Node getRoot() {
        return root;
    }
}
