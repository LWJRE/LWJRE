package gamma.engine.window;

import gamma.engine.ApplicationProperties;
import gamma.engine.input.Keyboard;
import gamma.engine.input.Mouse;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import vecmatlib.vector.Vec2i;

import java.nio.IntBuffer;

/**
 * Class that represents a GLFW window.
 *
 * @author Nico
 */
public class Window {

	/**
	 * Gets the window that is current in the calling thread.
	 *
	 * @see GLFW#glfwGetCurrentContext()
	 *
	 * @return The current window
	 * @throws IllegalStateException if there is no current window in the calling thread
	 */
	public static Window getCurrent() {
		long handle = GLFW.glfwGetCurrentContext();
		if(handle == MemoryUtil.NULL)
			throw new IllegalStateException("There is now window current in the calling thread");
		return new Window(handle);
	}

	/** Window handle */
	protected long handle;

	/**
	 * TODO: Write docs
	 *
	 * @param title Title of the window
	 * @param width Window width
	 * @param height Window height
	 */
	public Window(String title, int width, int height) {
		GLFWErrorCallback.createPrint(System.err).set(); // TODO: Move this somewhere else
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
		GLFW.glfwShowWindow(this.handle);
	}

	/**
	 * Creates a window with the title and size specified in {@link ApplicationProperties}.
	 * Windows must be created from the main thread.
	 */
	public Window() {
		this(ApplicationProperties.get("window/title", "untitled"), ApplicationProperties.get("window/size/width", 400), ApplicationProperties.get("window/size/height", 300));
	}

	/**
	 * Creates a window with the given handle. Only used in {@link Window#getCurrent()}.
	 *
	 * @param handle Window handle
	 */
	private Window(long handle) {
		this.handle = handle;
	}

	/**
	 * TODO: Change this
	 */
	private void checkState() {
		if(this.handle == MemoryUtil.NULL)
			throw new IllegalStateException("Window was already destroyed");
	}

	/**
	 * Sets up window callbacks.
	 *
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void setupCallbacks() {
		this.checkState();
		GLFW.glfwSetWindowSizeCallback(this.handle, (window, width, height) -> GL11.glViewport(0, 0, width, height));
		GLFW.glfwSetKeyCallback(this.handle, (window, key, scancode, action, mods) -> Keyboard.keyCallback(key, scancode, action, mods));
		GLFW.glfwSetMouseButtonCallback(this.handle, (window, button, action, mods) -> Mouse.buttonCallback(button, action, mods));
		GLFW.glfwSetCursorPosCallback(this.handle, (window, x, y) -> Mouse.cursorPosCallback(x, y));
		GLFW.glfwSetScrollCallback(this.handle, (window, x, y) -> Mouse.scrollCallback(x, y));
	}

	/**
	 * TODO: Write docs
	 */
	public void makeContextCurrent() {
		this.checkState();
		GLFW.glfwMakeContextCurrent(this.handle);
		GL.createCapabilities();
	}

	/**
	 * Sets the size of the window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwSetWindowSize(long, int, int)
	 *
	 * @param width New width of the window
	 * @param height New height of the window
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void setSize(int width, int height) {
		this.checkState();
		GLFW.glfwSetWindowSize(this.handle, width, height);
	}

	/**
	 * Sets the size of the window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwSetWindowSize(long, int, int)
	 *
	 * @param size New size of the window
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void setSize(Vec2i size) {
		this.setSize(size.x(), size.y());
	}

	/**
	 * Gets the size of this window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwGetWindowSize(long, IntBuffer, IntBuffer)
	 *
	 * @return A {@link Vec2i} containing the size of the window
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public Vec2i getSize() {
		this.checkState();
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(handle, w, h);
			return new Vec2i(w.get(), h.get());
		}
	}

	/**
	 * Sets the position of this window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwSetWindowPos(long, int, int)
	 *
	 * @param x New x position of the window from the upper-left corner of the screen
	 * @param y New y position of the window from the upper-left corner of the screen
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void setPosition(int x, int y) {
		this.checkState();
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	/**
	 * Sets the position of this window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwSetWindowPos(long, int, int)
	 *
	 * @param position New position of the window
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void setPosition(Vec2i position) {
		this.setPosition(position.x(), position.y());
	}

	/**
	 * Gets the position of this window.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwGetWindowPos(long, IntBuffer, IntBuffer)
	 *
	 * @return A {@link Vec2i} containing the position of the window from the upper-left corner of the screen
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public Vec2i getPosition() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer x = stack.mallocInt(1);
			IntBuffer y = stack.mallocInt(1);
			GLFW.glfwGetWindowPos(this.handle, x, y);
			return new Vec2i(x.get(), y.get());
		}
	}

	/**
	 * Returns true if the user has attempted to close the window, otherwise false.
	 * This method may be called from any thread.
	 *
	 * @see GLFW#glfwWindowShouldClose(long)
	 *
	 * @return True if the user has attempted to close the window, otherwise false
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public boolean isCloseRequested() {
		this.checkState();
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	/**
	 * Requests this window to be closed.
	 * This method may be called from any thread.
	 *
	 * @see GLFW#glfwSetWindowShouldClose(long, boolean)
	 *
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void requestClose() {
		this.checkState();
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	/**
	 * Updates the window.
	 * Called every frame from the main application class.
	 *
	 * @see GLFW#glfwSwapBuffers(long)
	 *
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void update() {
		this.checkState();
		GLFW.glfwSwapBuffers(this.handle);
	}

	/**
	 * Destroys this window and frees all callbacks.
	 * This method must be called from the main thread.
	 *
	 * @see GLFW#glfwDestroyWindow(long)
	 *
	 * @throws IllegalStateException if the window was already destroyed
	 */
	public void destroy() {
		this.checkState();
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
		this.handle = MemoryUtil.NULL;
	}

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
