package gamma.engine.core.scene;

public final class Scene {

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

	private static long previousTime = System.nanoTime();

	public static void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		root.process(delta);
		if(next != null) {
			root = next;
			next = null;
		}
		previousTime = time;
	}

//	public void onEditorProcess() {
//		root.editorProcess();
//		if(next != null) {
//			root = next;
//			next = null;
//		}
//	}
}
