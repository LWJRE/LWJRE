package io.github.hexagonnico.core.scene;

/**
 * Class representing the currently running scene.
 */
public final class SceneTree {

    /**
     * The root of the current scene.
     */
    private Node root = null;

    /**
     * Previous time obtained using {@link System#nanoTime()} and used to compute {@code delta}.
     */
    private long previousTime = 0;

    public void changeScene() {
        if(this.root != null) {
            this.root.exitTree();
        }
        this.root = new Node(); // TODO: Implement change scene
        this.root.enterTree();
    }

    /**
     * Processes the scene tree.
     * Called every frame from the main {@link io.github.hexagonnico.core.Application} class.
     */
    public void process() {
        if(previousTime == 0) {
            previousTime = System.nanoTime();
        }
        long time = System.nanoTime();
        float delta = (time - previousTime) / 1_000_000_000.0f;
        if(this.root != null) {
            this.process(this.root, delta);
        }
        previousTime = time;
    }

    /**
     * Recursive function used to process the scene tree.
     * Children are processed first.
     *
     * @param node The node to process.
     * @param delta Delta time.
     */
    private void process(Node node, float delta) {
        for(Node child : node.getChildren()) {
            this.process(child, delta);
        }
        node.onUpdate(delta);
    }

    // TODO: Call exit tree when terminating
}
