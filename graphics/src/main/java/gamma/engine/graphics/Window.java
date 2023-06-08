package gamma.engine.graphics;

import gamma.engine.core.Application;
import gamma.engine.core.ApplicationProperties;
import gamma.engine.core.input.Keyboard;
import gamma.engine.core.input.Mouse;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

/**
 * Class that represents a GLFW window.
 *
 * @author Nico
 */
public final class Window {

	private final long handle;

	public Window(String title, int width, int height) {
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create window");
		}
	}

	public Window() {
		this("untitled", 400, 300);
	}

	public void setVisible(boolean visible) {
		if(visible) {
			GLFW.glfwShowWindow(this.handle);
		} else {
			GLFW.glfwHideWindow(this.handle);
		}
	}

	public void makeCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
	}

	public void update() {
		GLFW.glfwSwapBuffers(this.handle);
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public void destroy() {
//		Callbacks.glfwFreeCallbacks(window);
		GLFW.glfwDestroyWindow(this.handle);
	}

//	private static long window;
//
//	public static void init() {
//		GLFWErrorCallback.createPrint(System.err).set();
//		if(!GLFW.glfwInit()) {
//			throw new RuntimeException("Unable to initialize GLFW");
//		}
//		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		window = GLFW.glfwCreateWindow(ApplicationProperties.get("window/viewport/width", 400), ApplicationProperties.get("window/viewport/height", 300), ApplicationProperties.get("window/title", "untitled"), MemoryUtil.NULL, MemoryUtil.NULL);
//		if(window == MemoryUtil.NULL) {
//			throw new RuntimeException("Failed to create the GLFW window");
//		}
//		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> RenderingSystem.resizeViewport(width, height));
//		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> Keyboard.keyCallback(key, scancode, action, mods));
//		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> Mouse.buttonCallback(button, action, mods));
//		GLFW.glfwSetCursorPosCallback(window, (window, x, y) -> Mouse.cursorPosCallback(x, y));
//		GLFW.glfwSetScrollCallback(window, (window, x, y) -> Mouse.scrollCallback(x, y));
//		GLFW.glfwMakeContextCurrent(window);
//		GLFW.glfwShowWindow(window);
//	}
//
//	public static void update() {
//		if(GLFW.glfwWindowShouldClose(window)) {
//			Application.quit();
//		} else {
//			GLFW.glfwSwapBuffers(window);
//			GLFW.glfwPollEvents();
//		}
//	}
//
//	public static void setSize(int width, int height) {
//		GLFW.glfwSetWindowSize(window, width, height);
//	}
//
//	// TODO: Get size
//
//	public void setPosition(int x, int y) {
//		GLFW.glfwSetWindowPos(window, x, y);
//	}
//
//	// TODO: Get position
//
//	public void requestClose() {
//		GLFW.glfwSetWindowShouldClose(window, true);
//	}
//
//	public static void setVSync(boolean vSync) {
//		GLFW.glfwSwapInterval(vSync ? 1 : 0);
//	}
//
//	public static void terminate() {
//		Callbacks.glfwFreeCallbacks(window);
//		GLFW.glfwDestroyWindow(window);
//		GLFW.glfwTerminate();
//		GLFW.glfwSetErrorCallback(null).free();
//	}
}
