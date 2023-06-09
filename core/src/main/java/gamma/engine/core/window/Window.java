package gamma.engine.core.window;

import gamma.engine.core.input.Keyboard;
import gamma.engine.core.input.Mouse;
import gamma.engine.core.servers.RenderingServer;
import io.github.hexagonnico.vecmatlib.vector.Vec2i;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;

/**
 * Class that represents a GLFW window.
 *
 * @author Nico
 */
public final class Window {

	public static Window current() {
		long handle = GLFW.glfwGetCurrentContext();
		if(handle != MemoryUtil.NULL) {
			return new Window(handle);
		} else {
			throw new IllegalStateException("There is no window whose context is current in the calling thread");
		}
	}

	private final long handle;
	private Vec2i size;

	// TODO: Window hints
//		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, ApplicationProperties.get("window/hints/visible", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, ApplicationProperties.get("window/hints/resizable", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, ApplicationProperties.get("window/hints/decorated", true) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, ApplicationProperties.get("window/hints/focused", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
//		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, ApplicationProperties.get("window/hints/maximized", false) ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

	public Window(String title, int width, int height) {
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create window");
		}
		this.size = new Vec2i(width, height);
		GLFW.glfwSetWindowSizeCallback(this.handle, (window, newWidth, newHeight) -> this.onResize(newWidth, newHeight));
		// TODO: Better callbacks
		GLFW.glfwSetKeyCallback(this.handle, (window, key, scancode, action, mods) -> Keyboard.keyCallback(key, scancode, action, mods));
		GLFW.glfwSetMouseButtonCallback(this.handle, (window, button, action, mods) -> Mouse.buttonCallback(button, action, mods));
		GLFW.glfwSetCursorPosCallback(this.handle, (window, x, y) -> Mouse.cursorPosCallback(x, y));
		GLFW.glfwSetScrollCallback(this.handle, (window, x, y) -> Mouse.scrollCallback(x, y));
	}

	public Window() {
		this("untitled", 400, 300);
	}

	private Window(long handle) {
		this.handle = handle;
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(this.handle, width, height);
			this.size = new Vec2i(width.get(), height.get());
		}
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

	private void onResize(int width, int height) {
		// TODO: Give options for viewport scaling
		this.size = new Vec2i(width, height);
		float aspectWidth = this.size.x();
		float aspectHeight = aspectWidth / (16.0f / 9.0f);
		if(aspectHeight > this.size.y()) {
			aspectHeight = this.size.y();
			aspectWidth = aspectHeight * (16.0f / 9.0f);
		}
		float viewportX = (this.size.x() / 2.0f) - (aspectWidth / 2.0f);
		float viewportY = (this.size.y() / 2.0f) - (aspectHeight / 2.0f);
		RenderingServer.setViewport((int) viewportX, (int) viewportY, (int) aspectWidth, (int) aspectHeight);
	}

	public int width() {
		return this.size.x();
	}

	public int height() {
		return this.size.y();
	}

	public Vec2i size() {
		return this.size;
	}

	public float aspectRatio() {
		return (float) this.width() / this.height();
	}

	public void setSize(int width, int height) {
		GLFW.glfwSetWindowSize(this.handle, width, height);
		// TODO: Check if this calls 'onResize'
	}

	public void setSize(Vec2i size) {
		this.setSize(size.x(), size.y());
	}

	public void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	public void setPosition(Vec2i position) {
		this.setSize(position.x(), position.y());
	}

	// TODO: Get position

	public void requestClose() {
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	public void destroy() {
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
	}

	public static void setVSync(boolean vSync) {
		GLFW.glfwSwapInterval(vSync ? 1 : 0);
	}
}
