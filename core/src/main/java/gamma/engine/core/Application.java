package gamma.engine.core;

import gamma.engine.core.scene.Scene;
import gamma.engine.core.window.Window;
import org.lwjgl.glfw.GLFW;

import java.util.ServiceLoader;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	public static void main(String[] args) {
		if(GLFW.glfwInit()) {
			ServiceLoader<ApplicationListener> listeners = ServiceLoader.load(ApplicationListener.class);
			try {
				System.out.println("Application started");
				Window window = new Window();
				window.makeContextCurrent();
				window.show();
				listeners.forEach(ApplicationListener::onStart);
				Scene.changeScene(ApplicationProperties.getString("startScene"));
				while(!window.isCloseRequested()) {
					listeners.forEach(ApplicationListener::onUpdate);
					Scene.getCurrent().process();
					window.update();
					GLFW.glfwPollEvents();
				}
				window.destroy();
			} catch (Exception e) {
				System.err.println("Uncaught exception in main");
				e.printStackTrace();
			} finally {
				System.out.println("Terminating");
				listeners.forEach(ApplicationListener::onTerminate);
				GLFW.glfwTerminate();
			}
		} else {
			System.err.println("Unable to initialize GLFW");
		}
	}
}
