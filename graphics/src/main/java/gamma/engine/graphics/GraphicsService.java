package gamma.engine.graphics;

import gamma.engine.core.Application;
import gamma.engine.core.ApplicationProperties;
import gamma.engine.core.EngineService;
import gamma.engine.graphics.resources.DeletableResource;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

public class GraphicsService implements EngineService {

	private Window window;

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(GLFW.glfwInit()) {
			// TODO: Window hints
			String windowTitle = ApplicationProperties.get("window.title", "Untitled");
			int windowWidth = ApplicationProperties.get("window.viewport.width", 400);
			int windowHeight = ApplicationProperties.get("window.viewport.height", 300);
			this.window = new Window(windowTitle, windowWidth, windowHeight);
			this.window.makeCurrent();
			this.window.setVisible(true);
			GL.createCapabilities(); // TODO: Should this be in another class?
			// TODO: Window callbacks
		} else {
			throw new RuntimeException("Unable to initialize GLFW");
		}
	}

	@Override
	public void update() {
		if(this.window.shouldClose()) {
			Application.quit();
		} else {
			this.window.update();
			RenderingSystem.render();
			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void terminate() {
		DeletableResource.deleteAll();
		this.window.destroy();
	}
}
