package gamma.engine.core.scene;

import gamma.engine.core.input.InputEvent;
import gamma.engine.core.utils.YamlUtils;

/**
 * Class that represents a scene. A scene is a tree data structure where every node is an entity.
 *
 * @author Nico
 */
public final class Scene {

	/** Current scene */
	private static Scene currentScene = new Scene();

	/**
	 * Gets the current scene.
	 *
	 * @return The current scene
	 */
	public static Scene getCurrent() {
		return currentScene;
	}

	/**
	 * Changes the current scene to the one at the given path in the classpath.
	 *
	 * @param path Path to the scene file in the classpath
	 */
	public static void changeScene(String path) {
		currentScene = YamlUtils.parseResource(path, Scene.class);
	}

	/**
	 * Changes the current scene to the given one.
	 *
	 * @param scene The new scene
	 */
	public static void changeScene(Scene scene) {
		currentScene = scene;
	}

	/** The scene's root */
	public final Entity root = new Entity();

	/** Used to compute the delta time */
	private transient long previousTime = System.nanoTime();

	/**
	 * Process the current scene by processing all entities in the scene.
	 * Called from the main application class.
	 */
	public void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		this.root.process(delta);
		previousTime = time;
	}

	/**
	 * Sends an input event to the root entity.
	 * The input is then propagated to all other entities.
	 *
	 * @param event The input event to send
	 */
	public void processInput(InputEvent event) {
		this.root.input(event);
	}
}
