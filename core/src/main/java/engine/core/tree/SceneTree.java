package engine.core.tree;

import engine.core.utils.FileUtils;

/**
 * Static class that contains the scene tree.
 *
 * @author Nico
 */
public final class SceneTree {

	/** The tree's root always stays the same */
	private static final Node root = new Node();

	/** Used to compute elapsed time since the previous update */
	private static long previousTime = System.nanoTime();

	/**
	 * Called from the main loop to process the scene tree.
	 */
	public static void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		root.process(delta);
		previousTime = time;
	}

	/**
	 * Changes the current scene to the one on the given file.
	 *
	 * @param file File path to the scene to change to
	 */
	public static void loadScene(String file) {
		Node node = FileUtils.parseYaml(file, Node.class);
		if(root.hasChild("")) root.removeChild("");
		root.addChild("", node);
	}
}
