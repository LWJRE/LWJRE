package gamma.engine.core;

import gamma.engine.core.tree.SceneTree;

import java.util.ServiceLoader;

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
		ServiceLoader<EngineService> systems = ServiceLoader.load(EngineService.class);
		try {
			systems.forEach(EngineService::init);
			SceneTree.changeScene(args.length > 0 ? args[0] : ApplicationProperties.getString("startScene"));
			while(running) {
				SceneTree.process();
				systems.forEach(EngineService::update);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			systems.forEach(EngineService::terminate);
		}
	}
}
