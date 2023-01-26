package gamma.engine.core.node;

/**
 * Static class that contains the scene tree.
 *
 * @author Nico
 */
public final class SceneTree {

	/** The tree's root always stays the same */
	private static final Node ROOT = new Node();

	/** Used to compute elapsed time since the previous update */
	private static long previousTime = System.nanoTime();

	/**
	 * Called from the main loop to process the scene tree.
	 */
	public static void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		ROOT.process(delta);
		previousTime = time;
	}

	/**
	 * Changes the current scene to the one on the given file.
	 * If an error occurs while loading the scene, the scene will be empty and the error will be printed to the console.
	 *
	 * @param file File path to the scene to change to
	 */
	public static void loadScene(String file) {
		try {
			if(ROOT.hasChild(""))
				ROOT.removeChild("");
			Node node = SubbranchLoader.load(file);
			ROOT.addChild("", node);
		} catch(RuntimeException exception) {
			System.err.println("Error loading scene " + file);
			exception.printStackTrace();
		}
	}
}
