package gamma.engine.core;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

/**
 * Class that represents a GLFW Window.
 *
 * @author Nico
 */
public final class Window {

	/** The window handle */
	private final long handle;

	// TODO: Make sure window was not destroyed before using it

	/**
	 * Creates a window with the given title. Uses window hints from the {@link ApplicationSettings}.
	 * Note: Windows must be created from the main thread.
	 *
	 * @param title Window title
	 * @param width Window width
	 * @param height Window height
	 */
	public Window(String title, int width, int height) {
		if(!Application.isMainThread())
			throw new RuntimeException("Windows can only be created from the main thread");
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationSettings.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationSettings.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationSettings.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationSettings.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationSettings.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
	}

	/**
	 * Creates a window with the properties from {@link ApplicationSettings}.
	 * Note: Windows must be created from the main thread.
	 */
	public Window() {
		this(ApplicationSettings.get("window/title", "untitled"), ApplicationSettings.get("window/size/width", 400), ApplicationSettings.get("window/size/height", 300));
	}

	/**
	 * Makes the context of this window current in the calling thread. See {@link GLFW#glfwMakeContextCurrent(long)}.
	 */
	public void makeContextCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
	}

	// TODO: Get window size

	/**
	 * Sets the position of the window. See {@link GLFW#glfwSetWindowPos(long, int, int)}.
	 * This function must be called from the main thread.
	 *
	 * @param x Position x
	 * @param y Position y
	 */
	public void setPosition(int x, int y) {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	// TODO: Get window position

	/**
	 * Makes this window visible after its creation. See {@link GLFW#glfwShowWindow(long)}.
	 * This function must be called from the main thread.
	 */
	public void show() {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		GLFW.glfwShowWindow(this.handle);
	}

	/**
	 * Closes the window. See {@link GLFW#glfwSetWindowShouldClose(long, boolean)}.
	 */
	public void requestClose() {
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	/**
	 * Returns true if the user has attempted to close the window, otherwise false. See {@link GLFW#glfwWindowShouldClose(long)}.
	 *
	 * @return True if the user has attempted to close the window, otherwise false
	 */
	public boolean isCloseRequested() {
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	/**
	 * Updates the window. See {@link GLFW#glfwSwapBuffers(long)}.
	 */
	public void update() {
		GLFW.glfwSwapBuffers(this.handle);
	}

	/**
	 * Destroys this window and frees all callbacks. See {@link GLFW#glfwDestroyWindow(long)} and {@link Callbacks#glfwFreeCallbacks(long)}.
	 * This function must be called from the main thread.
	 */
	public void destroy() {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
	}

	/**
	 * Enables or disables vSync.
	 *
	 * @param vSync True to enable vSync, false to disable it
	 */
	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
