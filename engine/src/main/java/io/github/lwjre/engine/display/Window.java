package io.github.lwjre.engine.display;

import io.github.hexagonnico.vecmatlib.vector.Vec2i;
import org.lwjgl.glfw.*;
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

	public Window(WindowOptions options) {
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, options.visible() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, options.resizable() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, options.decorated() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FOCUSED, options.focused() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, options.maximized() ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
		this.handle = GLFW.glfwCreateWindow(options.width(), options.height(), options.title(), MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create window");
		}
	}

	public Window(String title, int width, int height) {
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL) {
			throw new RuntimeException("Failed to create window");
		}
	}

	public Window() {
		this("untitled", 400, 300);
	}

	private Window(long handle) {
		this.handle = handle;
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

	public void setSizeCallback(WindowResizeCallback windowResizeCallback) {
		GLFWWindowSizeCallback callback = GLFW.glfwSetWindowSizeCallback(this.handle, windowResizeCallback);
		if(callback != null) {
			callback.close();
		}
	}

	public void setKeyCallback(KeyCallback keyCallback) {
		GLFWKeyCallback callback = GLFW.glfwSetKeyCallback(this.handle, keyCallback);
		if(callback != null) {
			callback.close();
		}
	}

	public void setMouseButtonCallback(MouseButtonCallback mouseButtonCallback) {
		GLFWMouseButtonCallback previousCallback = GLFW.glfwSetMouseButtonCallback(this.handle, mouseButtonCallback);
		if(previousCallback != null) {
			previousCallback.close();
		}
	}

	public void setCursorPosCallback(MouseCursorCallback cursorPosCallback) {
		GLFWCursorPosCallback previousCallback = GLFW.glfwSetCursorPosCallback(this.handle, cursorPosCallback);
		if(previousCallback != null) {
			previousCallback.close();
		}
	}

	public void setMouseScrollCallback(MouseScrollCallback mouseScrollCallback) {
		GLFWScrollCallback previousCallback = GLFW.glfwSetScrollCallback(this.handle, mouseScrollCallback);
		if(previousCallback != null) {
			previousCallback.close();
		}
	}

	public boolean shouldClose() {
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public int width() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(this.handle, width, null);
			return width.get();
		}
	}

	public int height() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(this.handle, null, height);
			return height.get();
		}
	}

	public Vec2i size() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			GLFW.glfwGetWindowSize(this.handle, width, height);
			return new Vec2i(width.get(), height.get());
		}
	}

	public float aspectRatio() {
		return (float) this.width() / this.height();
	}

	public void setSize(int width, int height) {
		GLFW.glfwSetWindowSize(this.handle, width, height);
	}

	public void setSize(Vec2i size) {
		this.setSize(size.x(), size.y());
	}

	public void setPosition(int x, int y) {
		GLFW.glfwSetWindowPos(this.handle, x, y);
	}

	public void setPosition(Vec2i position) {
		this.setPosition(position.x(), position.y());
	}

	public Vec2i position() {
		try(MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer xPos = stack.mallocInt(1);
			IntBuffer yPos = stack.mallocInt(1);
			GLFW.glfwGetWindowPos(this.handle, xPos, yPos);
			return new Vec2i(xPos.get(), yPos.get());
		}
	}

	// TODO: Set fullscreen, set borderless

	public void requestClose() {
		GLFW.glfwSetWindowShouldClose(this.handle, true);
	}

	public void destroy() {
		Callbacks.glfwFreeCallbacks(this.handle);
		GLFW.glfwDestroyWindow(this.handle);
	}
}
