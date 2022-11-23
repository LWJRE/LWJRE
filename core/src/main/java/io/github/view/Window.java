package io.github.view;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

public class Window {

	private long handle;

	public Window(String title, int width, int height) {
		if(!Application.isMainThread())
			throw new RuntimeException("Windows can only be created from the main thread");
		this.handle = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.handle == MemoryUtil.NULL)
			throw new RuntimeException("Failed to create the GLFW window");
	}

	public final void makeContextCurrent() {
		if(this.wasDestroyed())
			throw new RuntimeException("Window was already destroyed");
		GLFW.glfwMakeContextCurrent(this.handle);
		GL.createCapabilities();
	}

	public final void makeContextNonCurrent() {
		GLFW.glfwMakeContextCurrent(MemoryUtil.NULL);
	}

	public final boolean isContextCurrent() {
		if(this.wasDestroyed())
			throw new RuntimeException("Window was already destroyed");
		return GLFW.glfwGetCurrentContext() == this.handle;
	}

	public final void update() {
		if(this.wasDestroyed())
			throw new RuntimeException("Window was already destroyed");
		if(!this.isContextCurrent())
			throw new RuntimeException("Context is not current on the calling thread");
		GLFW.glfwSwapBuffers(this.handle);
	}

	public final boolean shouldClose() {
		if(this.wasDestroyed())
			throw new RuntimeException("Window was already destroyed");
		return GLFW.glfwWindowShouldClose(this.handle);
	}

	public final void destroy() {
		if(!Application.isMainThread())
			throw new RuntimeException("Windows can only be destroyed from the main thread");
		if(this.wasDestroyed())
			throw new RuntimeException("Window was already destroyed");
		GLFW.glfwDestroyWindow(this.handle);
		this.handle = MemoryUtil.NULL;
	}

	public final boolean wasDestroyed() {
		return this.handle == MemoryUtil.NULL;
	}
}
