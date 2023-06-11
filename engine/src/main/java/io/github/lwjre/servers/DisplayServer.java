package io.github.lwjre.servers;

import io.github.lwjre.Application;
import io.github.lwjre.ApplicationProperties;
import io.github.lwjre.display.Window;
import io.github.lwjre.display.WindowOptions;
import io.github.lwjre.input.Keyboard;
import io.github.lwjre.input.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class DisplayServer implements EngineServer {

	private Window mainWindow;

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(GLFW.glfwInit()) {
			this.mainWindow = new Window(new WindowOptions().title(ApplicationProperties.get("window.title", "Untitled"))
					.width(ApplicationProperties.get("window.viewport.width", 400))
					.height(ApplicationProperties.get("window.viewport.height", 300))
					.visible(ApplicationProperties.get("window.hints.visible", false))
					.resizable(ApplicationProperties.get("window.hints.resizable", true))
					.decorated(ApplicationProperties.get("window.hints.decorated", true))
					.focused(ApplicationProperties.get("window.hints.focused", true))
					.maximized(ApplicationProperties.get("window.hints.maximized", false)));
			this.mainWindow.setSizeCallback(RenderingServer::resizeViewport);
			this.mainWindow.setKeyCallback(new Keyboard.Callback());
			this.mainWindow.setMouseButtonCallback(new Mouse.ButtonCallback());
			this.mainWindow.setCursorPosCallback(new Mouse.CursorCallback());
			this.mainWindow.setMouseScrollCallback(new Mouse.ScrollCallback());
			this.mainWindow.makeCurrent();
			this.mainWindow.setVisible(true);
		} else {
			throw new RuntimeException("Unable to initialize GLFW");
		}
	}

	@Override
	public void update() {
		if(this.mainWindow.shouldClose()) {
			Application.quit();
		} else {
			this.mainWindow.update();
			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void terminate() {
		this.mainWindow.destroy();
		GLFW.glfwTerminate();
		GLFWErrorCallback errorCallback = GLFW.glfwSetErrorCallback(null);
		if(errorCallback != null) {
			errorCallback.close();
		}
	}

	@Override
	public int priority() {
		return 1;
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
