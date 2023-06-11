package io.github.lwjre.display;

import org.lwjgl.glfw.GLFWMouseButtonCallbackI;

public interface MouseButtonCallback extends GLFWMouseButtonCallbackI {

	@Override
	default void invoke(long window, int button, int action, int mods) {
		this.onButtonEvent(button, action, mods);
	}

	void onButtonEvent(int button, int action, int mods);
}
