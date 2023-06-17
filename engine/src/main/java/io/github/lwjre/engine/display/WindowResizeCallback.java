package io.github.lwjre.engine.display;

import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

public interface WindowResizeCallback extends GLFWWindowSizeCallbackI {

	@Override
	default void invoke(long window, int width, int height) {
		this.onResize(width, height);
	}

	void onResize(int width, int height);
}
