package gamma.engine.core;

import gamma.engine.core.tree.SceneTree;
import gamma.engine.core.window.Window;
import org.lwjgl.glfw.GLFW;

import java.util.ServiceLoader;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	/** Application singleton instance */
	private static Application application;

	/**
	 * Gets the application's main window.
	 *
	 * @return The application's main window
	 */
	public static Window window() {
		return application.window;
	}

	/** Application window */
	private final Window window;
	/** Loaded modules */
	private final ServiceLoader<Module> modules;

	/**
	 * Starts the application.
	 */
	private Application() {
		if(!GLFW.glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		this.window = new Window();
		this.modules = ServiceLoader.load(Module.class);
	}

	/**
	 * Runs the application.
	 */
	private void run() {
		try {
			this.window.makeContextCurrent();
			this.window.show();
			this.modules.forEach(Module::onStart);
			SceneTree.loadScene("/branches/test_dynamics.yaml");
			while(!this.window.isCloseRequested()) {
				SceneTree.process();
				this.modules.forEach(Module::onUpdate);
				this.window.update();
				GLFW.glfwPollEvents();
			}
			this.window.destroy();
		} catch(Exception e) {
			System.err.println("Uncaught exception");
			e.printStackTrace();
		} finally {
			this.modules.forEach(Module::onTerminate);
			GLFW.glfwTerminate();
		}
	}

	public static void main(String[] args) {
		application = new Application();
		application.run();
	}
}
