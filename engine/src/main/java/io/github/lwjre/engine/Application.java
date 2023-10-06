package io.github.lwjre.engine;

import io.github.lwjre.engine.nodes.SceneTree;
import io.github.lwjre.engine.servers.EngineServer;

import java.util.ArrayList;
import java.util.Collections;
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
		ArrayList<EngineServer> servers = new ArrayList<>();
		ServiceLoader.load(EngineServer.class).forEach(servers::add);
		Collections.sort(servers);
		try {
			servers.forEach(EngineServer::init);
			SceneTree.start();
			while(running) {
				SceneTree.process();
				servers.forEach(EngineServer::update);
			}
		} catch(Exception e) {
			System.err.println("Uncaught exception in main");
			e.printStackTrace();
		} finally {
			SceneTree.exit();
			Collections.reverse(servers);
			servers.forEach(EngineServer::terminate);
		}
	}
}
