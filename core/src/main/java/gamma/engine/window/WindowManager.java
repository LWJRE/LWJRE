package gamma.engine.window;

import gamma.engine.Application;
import gamma.engine.ApplicationListener;
import gamma.engine.ApplicationProperties;
import gamma.engine.input.KeyInputEvent;
import gamma.engine.input.MouseButtonInputEvent;
import gamma.engine.input.MouseCursorInputEvent;
import gamma.engine.input.MouseScrollInputEvent;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.system.MemoryUtil;

import java.util.ServiceLoader;

/**
 * Class that represents a GLFW window.
 *
 * @author Nico
 */
public class WindowManager implements ApplicationListener {

	private long handle;

	@Override
	public void onInit() {
		GLFWErrorCallback.createPrint(System.err).set();
		ServiceLoader<WindowListener> services = ServiceLoader.load(WindowListener.class);
		if(GLFW.glfwInit()) {
			GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
			GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
			this.handle = GLFW.glfwCreateWindow(ApplicationProperties.get("window/viewport/width", 400), ApplicationProperties.get("window/viewport/height", 300), ApplicationProperties.get("window/title", "untitled"), MemoryUtil.NULL, MemoryUtil.NULL);
			if(this.handle == MemoryUtil.NULL) {
				throw new RuntimeException("Failed to create the GLFW window");
			}
			GLFW.glfwSetWindowSizeCallback(this.handle, (window, width, height) -> services.forEach(service -> service.onWindowResized(width, height)));
			GLFW.glfwSetKeyCallback(this.handle, (window, key, scancode, action, mods) -> services.forEach(service -> service.onWindowInput(new KeyInputEvent(key, scancode, action, mods))));
			GLFW.glfwSetMouseButtonCallback(this.handle, (window, button, action, mods) -> services.forEach(service -> service.onWindowInput(new MouseButtonInputEvent(button, action, mods))));
			GLFW.glfwSetCursorPosCallback(this.handle, (window, x, y) -> services.forEach(service -> service.onWindowInput(new MouseCursorInputEvent((int) x, (int) y))));
			GLFW.glfwSetScrollCallback(this.handle, (window, x, y) -> services.forEach(service -> service.onWindowInput(new MouseScrollInputEvent((float) x, (float) y))));
			GLFW.glfwMakeContextCurrent(this.handle);
			GLFW.glfwShowWindow(this.handle);
		} else {
			throw new RuntimeException("Unable to initialize GLFW");
		}
	}

	@Override
	public void onProcess() {
		if(GLFW.glfwWindowShouldClose(this.handle)) {
			Application.quit();
		} else {
			GLFW.glfwSwapBuffers(this.handle);
			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void onTerminate() {
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

	// TODO: Reimplement the following

//	/**
//	 * Gets the window that is current in the calling thread.
//	 *
//	 * @see GLFW#glfwGetCurrentContext()
//	 *
//	 * @return The current window
//	 * @throws IllegalStateException if there is no current window in the calling thread
//	 */
//	public static WindowManager getCurrent() {
//		long handle = GLFW.glfwGetCurrentContext();
//		if(handle == MemoryUtil.NULL)
//			throw new IllegalStateException("There is now window current in the calling thread");
//		return new WindowManager(handle);
//	}
//
//	/**
//	 * Sets the size of the window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwSetWindowSize(long, int, int)
//	 *
//	 * @param width New width of the window
//	 * @param height New height of the window
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public void setSize(int width, int height) {
//		this.checkState();
//		GLFW.glfwSetWindowSize(this.handle, width, height);
//	}
//
//	/**
//	 * Sets the size of the window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwSetWindowSize(long, int, int)
//	 *
//	 * @param size New size of the window
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public void setSize(Vec2i size) {
//		this.setSize(size.x(), size.y());
//	}
//
//	/**
//	 * Gets the size of this window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwGetWindowSize(long, IntBuffer, IntBuffer)
//	 *
//	 * @return A {@link Vec2i} containing the size of the window
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public Vec2i getSize() {
//		this.checkState();
//		try(MemoryStack stack = MemoryStack.stackPush()) {
//			IntBuffer w = stack.mallocInt(1);
//			IntBuffer h = stack.mallocInt(1);
//			GLFW.glfwGetWindowSize(handle, w, h);
//			return new Vec2i(w.get(), h.get());
//		}
//	}
//
//	/**
//	 * Sets the position of this window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwSetWindowPos(long, int, int)
//	 *
//	 * @param x New x position of the window from the upper-left corner of the screen
//	 * @param y New y position of the window from the upper-left corner of the screen
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public void setPosition(int x, int y) {
//		this.checkState();
//		GLFW.glfwSetWindowPos(this.handle, x, y);
//	}
//
//	/**
//	 * Sets the position of this window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwSetWindowPos(long, int, int)
//	 *
//	 * @param position New position of the window
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public void setPosition(Vec2i position) {
//		this.setPosition(position.x(), position.y());
//	}
//
//	/**
//	 * Gets the position of this window.
//	 * This method must be called from the main thread.
//	 *
//	 * @see GLFW#glfwGetWindowPos(long, IntBuffer, IntBuffer)
//	 *
//	 * @return A {@link Vec2i} containing the position of the window from the upper-left corner of the screen
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public Vec2i getPosition() {
//		try(MemoryStack stack = MemoryStack.stackPush()) {
//			IntBuffer x = stack.mallocInt(1);
//			IntBuffer y = stack.mallocInt(1);
//			GLFW.glfwGetWindowPos(this.handle, x, y);
//			return new Vec2i(x.get(), y.get());
//		}
//	}
//
//	/**
//	 * Requests this window to be closed.
//	 * This method may be called from any thread.
//	 *
//	 * @see GLFW#glfwSetWindowShouldClose(long, boolean)
//	 *
//	 * @throws IllegalStateException if the window was already destroyed
//	 */
//	public void requestClose() {
//		this.checkState();
//		GLFW.glfwSetWindowShouldClose(this.handle, true);
//	}

	/**
	 * Enables or disables V-Sync.
	 * This method may be called from any thread.
	 *
	 * @see GLFW#glfwSwapInterval(int)
	 *
	 * @param vSync True to enable V-Sync, false to disable it
	 */
	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
