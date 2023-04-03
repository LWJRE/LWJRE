package gamma.engine.scene;

import gamma.engine.input.InputEvent;
import gamma.engine.utils.YamlUtils;

public final class Scene {

	private static Entity root = new Entity();

	public static void changeSceneTo(String path) {
		root = YamlUtils.parseResource(path, Entity.class);
	}

	public static void changeSceneTo(Entity entity) {
		root = entity;
	}

	public static Entity getRoot() {
		return root;
	}

	private static long previousTime = System.nanoTime();

	public static void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		root.process(delta);
		previousTime = time;
	}

	public static void processInput(InputEvent event) {
		root.input(event);
	}

	public static void editorProcess() {
		root.editorProcess();
	}
}
