package io.github.view.graphics;

import io.github.view.resources.PropertiesFile;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class Window {

	private final long handle;

	// TODO: Make sure window was not destroyed before using it

	public Window(PropertiesFile properties) {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, properties.get("visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, properties.get("resizeable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, properties.get("decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, properties.get("focused", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, properties.get("maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		String title = properties.get("title", "untitled");
		int width = properties.get("width", 300);
		int height = properties.get("height", 300);
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL); // TODO: Must be called from the main thread
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
	}

	public Window(String title, int width, int height) {
		this(new PropertiesFile().set("title", title).set("width", width).set("height", height));
	}

	public Window() {
		this(new PropertiesFile());
	}

	public final void setKeyCallback(GLFWKeyCallbackI callback) {
		GLFW.glfwSetKeyCallback(this.handle, callback);
	}

	public final void makeContextCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
		GL.createCapabilities(); // TODO: Context switching?
	}

	// TODO: Get window size

	public final void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(this.handle, x, y); // TODO: Must be called from the main thread
	}

	// TODO: Get window position

	public final void show() {
		GLFW.glfwShowWindow(this.handle); // TODO: Must be called from the main thread
	}

	public final void requestClose() {
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	public final boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public final void update() {
		GLFW.glfwSwapBuffers(this.handle);
	}

	public final void destroy() {
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle); // TODO: Must be called from the main thread
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
