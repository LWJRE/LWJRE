package io.github.lwjre.engine.display;

import org.lwjgl.glfw.GLFWKeyCallbackI;

public interface KeyCallback extends GLFWKeyCallbackI {

	@Override
	default void invoke(long window, int key, int scancode, int action, int mods) {
		this.onKeyEvent(key, scancode, action, mods);
	}

	void onKeyEvent(int key, int scancode, int action, int mods);
}
