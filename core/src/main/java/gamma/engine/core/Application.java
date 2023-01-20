package gamma.engine.core;

import gamma.engine.core.tree.SceneTree;
import org.lwjgl.glfw.GLFW;

import java.util.ServiceLoader;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	/** Main thread object used in {@link Application#isMainThread()} */
	private static Thread mainThread;

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

	/**
	 * Checks if the calling thread is the main thread.
	 *
	 * @return True if the current thread is the main thread, otherwise false
	 */
	public static boolean isMainThread() {
		return Thread.currentThread().equals(mainThread);
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
			// TODO: Default empty scene
			SceneTree.loadScene(ApplicationSettings.get("startScene", ""));
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
		mainThread = Thread.currentThread();
		application = new Application();
		application.run();
	}
}
