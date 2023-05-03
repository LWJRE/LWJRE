package gamma.engine;

import gamma.engine.physics.PhysicsSystem;
import gamma.engine.rendering.RenderingSystem;
import gamma.engine.scene.Scene;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	private static boolean running = true;

	public static void quit() {
		running = false;
	}

	public static void main(String[] args) {
		try {
			Window.init();
			RenderingSystem.init();
			Scene.changeSceneTo(ApplicationProperties.getString("startScene"));
			while(running) {
				Scene.process();
				Window.update();
				PhysicsSystem.update();
				RenderingSystem.render();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			RenderingSystem.cleanUp();
			Window.terminate();
		}
	}
}
