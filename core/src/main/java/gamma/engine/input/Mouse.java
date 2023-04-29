package gamma.engine.input;

import org.lwjgl.glfw.GLFW;
import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2i;

/**
 * Class that represents the main mouse.
 * 
 * @author Nico
 */
public final class Mouse {

	/** Left mouse button code */
	public static final int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	/** Right mouse button code */
	public static final int BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	/** Middle mouse button code */
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	/** First mouse thumb button code */
	public static final int BUTTON_4 = GLFW.GLFW_MOUSE_BUTTON_4;
	/** Second mouse thumb button code */
	public static final int BUTTON_5 = GLFW.GLFW_MOUSE_BUTTON_5;
	/** Third mouse thumb button code */
	public static final int BUTTON_6 = GLFW.GLFW_MOUSE_BUTTON_6;
	/** Fourth mouse thumb button code */
	public static final int BUTTON_7 = GLFW.GLFW_MOUSE_BUTTON_7;
	/** Fifth mouse thumb button code */
	public static final int BUTTON_8 = GLFW.GLFW_MOUSE_BUTTON_8;

	// TODO: Move mouse position into InputSystem
	/** Current mouse cursor position */
	private static Vec2i position = Vec2i.Zero();

	/**
	 * Method used to store the current mouse position when the cursor is moved.
	 * Get the position with {@link Mouse#position()}.
	 *
	 * @see org.lwjgl.glfw.GLFWCursorPosCallbackI#invoke(long, double, double)
	 * 
	 * @param x The new cursor x coordinate
	 * @param y The new cursor y coordinate
	 */
	public static void cursorPosCallback(double x, double y) {
		position = new Vec2d(x, y).toInt();
	}

	/**
	 * Gets the current mouse position in pixels relative to the top-left edge of the window.
	 * 
	 * @return A {@link Vec2i} containing the current mouse position
	 */
	public static Vec2i position() {
		return position;
	}

	/**
	 * Returns true if the given mouse button is being held down, otherwise false.
	 * This method is equivalent to {@link InputSystem#isMouseButtonDown(int)}.
	 * 
	 * @param button The button's code
	 * @return True if the given mouse button is being held down, otherwise false
	 */
	public static boolean isButtonDown(int button) {
		return InputSystem.isMouseButtonDown(button);
	}
}
