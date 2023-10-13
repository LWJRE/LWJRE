package io.github.lwjre.engine.servers;

import io.github.hexagonnico.vecmatlib.vector.Vec2i;
import io.github.lwjre.engine.Application;
import io.github.lwjre.engine.ApplicationSettings;
import io.github.lwjre.engine.display.Window;
import io.github.lwjre.engine.display.WindowOptions;
import io.github.lwjre.engine.input.Keyboard;
import io.github.lwjre.engine.input.Mouse;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class DisplayServer implements EngineServer {

	private static Window mainWindow;

	public static Window window() {
		return mainWindow;
	}

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(GLFW.glfwInit()) {
			mainWindow = new Window(new WindowOptions()
				.title(ApplicationSettings.get("window.title", "Untitled"))
				.width(ApplicationSettings.get("window.viewport", new Vec2i(400, 0)).x())
				.height(ApplicationSettings.get("window.viewport", new Vec2i(0, 300)).y())
				.visible(ApplicationSettings.get("window.visible", false))
				.resizable(ApplicationSettings.get("window.resizable", true))
				.decorated(ApplicationSettings.get("window.decorated", true))
				.focused(ApplicationSettings.get("window.focused", true))
				.maximized(ApplicationSettings.get("window.maximized", false)));
			mainWindow.setSizeCallback(RenderingServer::resizeViewport);
			mainWindow.setKeyCallback(new Keyboard.Callback());
			mainWindow.setMouseButtonCallback(new Mouse.ButtonCallback());
			mainWindow.setCursorPosCallback(new Mouse.CursorCallback());
			mainWindow.setMouseScrollCallback(new Mouse.ScrollCallback());
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
