package gamma.engine.core.scene;

import gamma.engine.core.input.InputEvent;
import gamma.engine.core.utils.YamlUtils;

public final class Scene {

	private static Scene currentScene = new Scene();

	public static Scene getCurrent() {
		return currentScene;
	}

	public static void changeScene(String path) {
		// TODO: On exit and on enter?
		currentScene = YamlUtils.parseResource(path, Scene.class);
	}

	public static void changeScene(Scene scene) {
		currentScene = scene;
	}

	public final Entity root = new Entity();

	private transient long previousTime = System.nanoTime();

	public void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		this.root.process(delta);
		previousTime = time;
	}

	public void processInput(InputEvent event) {
		this.root.input(event);
	}
}
