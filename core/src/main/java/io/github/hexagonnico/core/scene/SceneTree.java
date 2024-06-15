package io.github.hexagonnico.core.scene;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree {

    /**
     * The root of the current scene.
     */
    private Node root = null;

    public void changeScene() {
        if(this.root != null) {
            this.root.exitTree();
        }
        // TODO: Implement scene change
        this.root = new Node();
        this.root.addChild(new Sprite2D());
        this.root.enterTree(this);
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
