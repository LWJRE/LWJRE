package gamma.engine;

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
		ServiceLoader<ApplicationListener> services = ServiceLoader.load(ApplicationListener.class);
		try {
			services.forEach(ApplicationListener::onInit);
			while(running) {
				services.forEach(ApplicationListener::onProcess);
			}
		} catch(Exception e) {
			System.err.println("Uncaught exception in main");
			e.printStackTrace();
		} finally {
			services.forEach(ApplicationListener::onTerminate);
		}
	}
}
