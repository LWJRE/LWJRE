package gamma.engine;

import gamma.engine.window.Window;
import org.lwjgl.glfw.GLFW;

import java.util.ServiceLoader;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	public static void main(String[] args) {
		ServiceLoader<ApplicationListener> services = ServiceLoader.load(ApplicationListener.class);
		if(GLFW.glfwInit()) {
			try {
				System.out.println("Application started");
				Window window = new Window();
				window.setupCallbacks();
				window.makeContextCurrent();
				services.forEach(ApplicationListener::onInit);
				while(!window.isCloseRequested()) {
					services.forEach(ApplicationListener::onProcess);
					window.update();
					GLFW.glfwPollEvents();
				}
				window.destroy();
			} catch (Exception e) {
				System.err.println("Uncaught exception in main");
				e.printStackTrace();
			} finally {
				System.out.println("Terminating");
				services.forEach(ApplicationListener::onTerminate);
				GLFW.glfwTerminate();
			}
		} else {
			System.err.println("Unable to initialize GLFW");
		}
	}
}
