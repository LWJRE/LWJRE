package gamma.engine.input;

import gamma.engine.scene.Scene;
import org.lwjgl.glfw.GLFW;
import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2i;

import java.util.HashSet;

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

	/** Set of buttons currently held down */
	private static final HashSet<Integer> BUTTONS = new HashSet<>();
	/** Current mouse cursor position */
	private static Vec2i position = Vec2i.Zero();

	/**
	 * Method used to record a mouse button event.
	 * Stores the the given button's state and fires a {@link MouseButtonInputEvent}.
	 *
	 * @see org.lwjgl.glfw.GLFWMouseButtonCallbackI#invoke(long, int, int, int)
	 * 
	 * @param button Mouse button code
	 * @param action Either {@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_RELEASE}, or {@link GLFW#GLFW_REPEAT}
	 * @param mods Flags to determine which modifier keys were pressed
	 */
	public static void buttonCallback(int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS)
			BUTTONS.add(button);
		else if(action == GLFW.GLFW_RELEASE)
			BUTTONS.remove(button);
		Scene.getCurrent().processInput(new MouseButtonInputEvent(button, action, mods));
	}

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
	 * Method used to fire a {@link MouseScrollInputEvent} when the mouse wheel is scrolled.
	 *
	 * @see org.lwjgl.glfw.GLFWScrollCallbackI#invoke(long, double, double)
	 *
	 * @param x The scroll offset along the x axis
	 * @param y The scroll offset along the y axis
	 */
	public static void scrollCallback(double x, double y) {
		Scene.getCurrent().processInput(new MouseScrollInputEvent((float) x, (float) y));
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
	 * 
	 * @param button The button's code
	 * @return True if the given mouse button is being held down, otherwise false
	 */
	public static boolean isButtonDown(int button) {
		return BUTTONS.contains(button);
	}
}
