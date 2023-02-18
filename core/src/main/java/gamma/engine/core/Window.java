package gamma.engine.core;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import vecmatlib.vector.Vec2i;

import java.nio.IntBuffer;
import java.util.ServiceLoader;

/**
 * Class that represents a GLFW Window.
 *
 * @author Nico
 */
public final class Window {

	/** The window handle */
	private final long handle;

	/** Loaded window listeners */
	private final ServiceLoader<WindowListener> listeners = ServiceLoader.load(WindowListener.class);

	// TODO: Make sure window was not destroyed before using it

	// TODO: Application properties

	/**
	 * Creates a window with the given title. Uses window hints from the {@link ApplicationProperties}.
	 * Note: Windows must be created from the main thread.
	 *
	 * @param title Window title
	 * @param width Window width
	 * @param height Window height
	 */
	public Window(String title, int width, int height) {
		if(!Application.isMainThread())
			throw new RuntimeException("Windows can only be created from the main thread");
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwSetWindowSizeCallback(this.handle, (l, w, h) -> this.listeners.forEach(listener -> listener.onResize(w, h)));
	}

	/**
	 * Creates a window with the properties from {@link ApplicationProperties}.
	 * Note: Windows must be created from the main thread.
	 */
	public Window() {
		this(ApplicationProperties.get("window/title", "untitled"), ApplicationProperties.get("window/size/width", 400), ApplicationProperties.get("window/size/height", 300));
	}

	/**
	 * Makes the context of this window current in the calling thread. See {@link GLFW#glfwMakeContextCurrent(long)}.
	 */
	public void makeContextCurrent() {
		GLFW.glfwMakeContextCurrent(this.handle);
	}

	/**
	 * Sets the size of the window. See {@link GLFW#glfwSetWindowSize(long, int, int)}.
	 * This method must be called from the main thread.
	 *
	 * @param width Width of the window in pixels
	 * @param height Height of the window in pixels
	 */
	public void setSize(int width, int height) {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		GLFW.glfwSetWindowSize(this.handle, width, height);
	}

	/**
	 * Sets the size of the window. See {@link GLFW#glfwSetWindowSize(long, int, int)}.
	 * This method must be called from the main thread.
	 *
	 * @param size Size of the window in pixels
	 */
	public void setSize(Vec2i size) {
		this.setSize(size.x(), size.y());
	}

	/**
	 * Gets the size of the window. See {@link GLFW#glfwGetWindowSize(long, IntBuffer, IntBuffer)}.
	 * This method must be called from the main thread.
	 *
	 * @return A 2d integer vector containing the size of the window in pixels
	 */
	public Vec2i getSize() {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(this.handle, w, h);
			return new Vec2i(w.get(), h.get());
		}
	}

	/**
	 * Sets the position of the window. See {@link GLFW#glfwSetWindowPos(long, int, int)}.
	 * This method must be called from the main thread.
	 *
	 * @param x Position x in pixels
	 * @param y Position y in pixels
	 */
	public void setPosition(int x, int y) {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	/**
	 * Sets the position of the window. See {@link GLFW#glfwSetWindowPos(long, int, int)}.
	 * This method must be called from the main thread.
	 *
	 * @param position Position of the window in pixels on the screen
	 */
	public void setPosition(Vec2i position) {
		this.setPosition(position.x(), position.y());
	}

	/**
	 * Gets the position of the window. See {@link GLFW#glfwGetWindowPos(long, IntBuffer, IntBuffer)}.
	 * This method must be called from the main thread.
	 *
	 * @return The position of the window in pixels on the screen
	 */
	public Vec2i getPosition() {
		if(!Application.isMainThread())
			throw new RuntimeException("This function must be called from the main thread");
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer x = stack.mallocInt(1);
			IntBuffer y = stack.mallocInt(1);
			GLFW.glfwGetWindowPos(this.handle, x, y);
			return new Vec2i(x.get(), y.get());
		}
	}

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
