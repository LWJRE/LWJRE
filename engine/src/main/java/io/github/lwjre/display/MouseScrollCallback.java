package io.github.lwjre.display;

import org.lwjgl.glfw.GLFWScrollCallbackI;

public interface MouseScrollCallback extends GLFWScrollCallbackI {

	@Override
	default void invoke(long window, double xOffset, double yOffset) {
		this.onMouseScrolled(xOffset, yOffset);
	}

	void onMouseScrolled(double xOffset, double yOffset);
}
