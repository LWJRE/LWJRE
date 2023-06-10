package gamma.engine.core.nodes;

import gamma.engine.core.Application;
import gamma.engine.core.ApplicationProperties;
import gamma.engine.core.resources.NodeResource;

public class SceneTree {

	/** Root of the current scene */
	private static Node root = null;
	/** Root of the next scene */
	private static Node next = null;

	public static void changeScene(String file) {
		changeScene(NodeResource.getOrLoad(file));
	}

	public static void changeScene(NodeResource resource) {
		changeScene(resource.instantiate());
	}

	public static void changeScene(Node nextScene) {
		next = nextScene;
	}

	/**
	 * Gets the current scene's root or null if no scene is current.
	 *
	 * @return The current scene's root or null if no scene is current
	 */
	public static Node getRoot() {
		return root;
	}

	/** Time elapsed since the previous frame in nanoseconds */
	private static long previousTime = System.nanoTime();

	public static void start() {
		changeScene(ApplicationProperties.getString("startScene"));
	}

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
		// TODO: Call Application.quit() if root is deleted
	}

	public static void exit() {
		root.exit();
		Application.quit();
	}
}
