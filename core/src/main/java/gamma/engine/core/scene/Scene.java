package gamma.engine.core.scene;

public final class Scene {

	public Entity root;
	private transient long previousTime = System.nanoTime();

	public void process() {
		long time = System.nanoTime();
		float delta = (time - previousTime) / 1_000_000_000.0f;
		this.root.process(delta);
		previousTime = time;
	}
}
