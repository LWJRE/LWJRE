package gamma.engine.core;

import gamma.engine.core.nodes.SceneTree;
import gamma.engine.core.servers.EngineServer;

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
		ServiceLoader<EngineServer> systems = ServiceLoader.load(EngineServer.class);
		try {
			systems.forEach(EngineServer::init);
			SceneTree.start();
			while(running) {
				SceneTree.process();
				systems.forEach(EngineServer::update);
			}
		} catch(Exception e) {
			System.err.println("Uncaught exception in main");
			e.printStackTrace();
		} finally {
			SceneTree.exit();
			systems.forEach(EngineServer::terminate);
		}
	}
}
