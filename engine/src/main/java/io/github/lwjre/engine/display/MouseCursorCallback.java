package io.github.lwjre.engine.display;

import org.lwjgl.glfw.GLFWCursorPosCallbackI;

public interface MouseCursorCallback extends GLFWCursorPosCallbackI {

	@Override
	default void invoke(long window, double xPos, double yPos) {
		this.onCursorMoved(xPos, yPos);
	}

	void onCursorMoved(double xPos, double yPos);
}
