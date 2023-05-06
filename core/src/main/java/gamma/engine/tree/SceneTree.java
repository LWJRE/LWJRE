package gamma.engine.tree;

public final class SceneTree {

	private static Node root = null;
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

	private static long previousTime = System.nanoTime();

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

	public static Node getRoot() {
		return root;
	}
}
