package gamma.engine.core.tree;

import gamma.engine.core.Application;

/**
 * Static class that holds the current scene.
 *
 * @author Nico
 */
public final class SceneTree {

	/** Root of the current scene */
	private static Node root = null;
	/** Root of the next scene */
	private static Node next = null;

	/**
	 * Changes scene to the {@link NodeResource} at the given path in the classpath.
	 * The scene is not changed immediately, but only before the next {@link SceneTree#process()} call.
	 *
	 * @param file Path to the {@code NodeResource} file of the scene in the classpath
	 */
	public static void changeScene(String file) {
		changeScene(NodeResource.getOrLoad(file));
	}

	/**
	 * Changes scene to the given {@link NodeResource}.
	 * The scene is not changed immediately, but only before the next {@link SceneTree#process()} call.
	 *
	 * @param resource {@code NodeResource} of the scene to change to
	 */
	public static void changeScene(NodeResource resource) {
		changeScene(resource.instantiate());
	}

	/**
	 * Changes scene to the one with the given root.
	 * The scene is not changed immediately, but only before the next {@link SceneTree#process()} call.
	 *
	 * @param nextScene Root of the scene to change to.
	 */
	public static void changeScene(Node nextScene) {
		next = nextScene;
	}

	/** Time elapsed since the previous frame in nanoseconds */
	private static long previousTime = System.nanoTime();

	/**
	 * Processes the current scene.
	 * Called from the {@link Application} main loop.
	 */
	public static void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		if(next != null) {
			if(root != null) {
				root.exit();
			}
			root = next;
			root.enter();
			next = null;
		}
		if(root != null) {
			root.process(delta);
		}
		previousTime = time;
	}

	/**
	 * Gets the current scene's root or null if no scene is current.
	 *
	 * @return The current scene's root or null if no scene is current
	 */
	public static Node getRoot() {
		return root;
	}
}
