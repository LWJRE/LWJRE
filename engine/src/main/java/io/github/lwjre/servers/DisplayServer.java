package io.github.lwjre.servers;

import io.github.lwjre.Application;
import io.github.lwjre.ApplicationProperties;
import io.github.lwjre.display.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class DisplayServer implements EngineServer {

	private static Window mainWindow;

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(GLFW.glfwInit()) {
			// TODO: Window hints
			String windowTitle = ApplicationProperties.get("window.title", "Untitled");
			int windowWidth = ApplicationProperties.get("window.viewport.width", 400);
			int windowHeight = ApplicationProperties.get("window.viewport.height", 300);
			mainWindow = new Window(windowTitle, windowWidth, windowHeight);
			mainWindow.makeCurrent();
			mainWindow.setVisible(true);
		} else {
			throw new RuntimeException("Unable to initialize GLFW");
		}
	}

	@Override
	public void update() {
		if(mainWindow.shouldClose()) {
			Application.quit();
		} else {
			mainWindow.update();
			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void terminate() {
		mainWindow.destroy();
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	@Override
	public int priority() {
		return 1;
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
