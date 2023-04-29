package gamma.engine.scene;

import gamma.engine.ApplicationListener;
import gamma.engine.ApplicationProperties;
import gamma.engine.EditorListener;
import gamma.engine.input.InputEvent;
import gamma.engine.window.WindowListener;

public final class Scene implements ApplicationListener, EditorListener, WindowListener {

	private static Entity root = new Entity();
	private static Entity next = null;

	public static void changeSceneTo(String path) {
		changeSceneTo(EntityResource.getOrLoad(path));
	}

	public static void changeSceneTo(EntityResource resource) {
		changeSceneTo(resource.instance());
	}

	public static void changeSceneTo(Entity entity) {
		root.removeFromScene();
		next = entity;
	}

	public static Entity getRoot() {
		return root;
	}

	private long previousTime = System.nanoTime();

	@Override
	public void onInit() {
		changeSceneTo(ApplicationProperties.getString("startScene"));
	}

	@Override
	public void onWindowInput(InputEvent event) {
		root.input(event);
	}

	@Override
	public void onProcess() {
		long time = System.nanoTime();
		float delta = (time - this.previousTime) / 1_000_000_000.0f;
		root.process(delta);
		if(next != null) {
			root = next;
			next = null;
		}
		this.previousTime = time;
	}

	@Override
	public void onEditorProcess() {
		root.editorProcess();
		if(next != null) {
			root = next;
			next = null;
		}
	}
}
