package engine.core.window;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.system.MemoryUtil;

public final class Window {

	private final long handle;

	// TODO: Make sure window was not destroyed before using it

	private Window(WindowProperties properties) {
		properties.setWindowHints();
		this.handle = GLFW.glfwCreateWindow(properties.getWidth(), properties.getHeight(), properties.getTitle(), MemoryUtil.NULL, MemoryUtil.NULL); // TODO: Must be called from the main thread
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
	}

	public Window(String title, int width, int height) {
		this(new WindowProperties().setTitle(title).setSize(width, height));
	}

	public Window() {
		this(new WindowProperties("/window.properties"));
	}

	public void setKeyCallback(GLFWKeyCallbackI callback) {
		GLFW.glfwSetKeyCallback(this.handle, callback);
	}

	public void makeContextCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
	}

	// TODO: Get window size

	public void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(this.handle, x, y); // TODO: Must be called from the main thread
	}

	// TODO: Get window position

	public void show() {
		GLFW.glfwShowWindow(this.handle); // TODO: Must be called from the main thread
	}

	public void requestClose() {
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public void update() {
		GLFW.glfwSwapBuffers(this.handle);
	}

	public void destroy() {
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle); // TODO: Must be called from the main thread
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
