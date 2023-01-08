package engine.core.input;

import org.lwjgl.glfw.GLFW;

public final class Mouse {

	/** Left moues button code */
	public static final int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_1;
	/** Right mouse button code */
	public static final int BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_2;
	/** Middle mouse button code */
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_3;
	/** Thumb mouse button 4 code */
	public static final int BUTTON_4 = GLFW.GLFW_MOUSE_BUTTON_4;
	/** Thumb mouse button 5 code */
	public static final int BUTTON_5 = GLFW.GLFW_MOUSE_BUTTON_5;
	/** Thumb mouse button 6 code */
	public static final int BUTTON_6 = GLFW.GLFW_MOUSE_BUTTON_6;
	/** Thumb mouse button 7 code */
	public static final int BUTTON_7 = GLFW.GLFW_MOUSE_BUTTON_7;
	/** Thumb mouse button 8 code */
	public static final int BUTTON_8 = GLFW.GLFW_MOUSE_BUTTON_8;

	public static boolean isButtonPressed(int button) {
		long window = GLFW.glfwGetCurrentContext();
		return GLFW.glfwGetMouseButton(window, button) == GLFW.GLFW_PRESS;
	}

	public static int getAxis(int positiveButton, int negativeButton) {
		boolean positive = isButtonPressed(positiveButton);
		boolean negative = isButtonPressed(negativeButton);
		return positive ? (negative ? 0 : 1) : (negative ? -1 : 0);
	}
}
