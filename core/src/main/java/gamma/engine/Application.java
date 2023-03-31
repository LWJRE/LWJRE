package gamma.engine;

import gamma.engine.rendering.RenderingSystem;
import gamma.engine.resources.DeletableResource;
import gamma.engine.scene.Scene;
import gamma.engine.window.Window;
import org.lwjgl.glfw.GLFW;

/**
 * Main application class.
 *
 * @author Nico
 */
public final class Application {

	public static void main(String[] args) {
		if(GLFW.glfwInit()) {
			try {
				System.out.println("Application started");
				Window window = new Window();
				window.setupCallbacks();
				window.makeContextCurrent();
				Scene.changeScene(ApplicationProperties.getString("startScene"));
				while(!window.isCloseRequested()) {
					Scene.getCurrent().process();
					RenderingSystem.render();
					window.update();
					GLFW.glfwPollEvents();
				}
				window.destroy();
			} catch (Exception e) {
				System.err.println("Uncaught exception in main");
				e.printStackTrace();
			} finally {
				System.out.println("Terminating");
				DeletableResource.deleteAll();
				GLFW.glfwTerminate();
			}
		} else {
			System.err.println("Unable to initialize GLFW");
		}
	}
}
