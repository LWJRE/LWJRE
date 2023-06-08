package gamma.engine.core.input;

import gamma.engine.core.tree.SceneTree;
import org.lwjgl.glfw.GLFW;
import io.github.hexagonnico.vecmatlib.vector.Vec2i;

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
	private static final HashSet<Integer> MOUSE = new HashSet<>();

	/** Current mouse cursor position */
	private static Vec2i position = Vec2i.Zero();

	public static void buttonCallback(int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			MOUSE.add(button);
		} else if(action == GLFW.GLFW_RELEASE) {
			MOUSE.remove(button);
		}
		SceneTree.getRoot().input(new MouseButtonInputEvent(button, action, mods));
	}

	public static void cursorPosCallback(double x, double y) {
		position = new Vec2i((int) x, (int) y);
		SceneTree.getRoot().input(new MouseCursorInputEvent((int) x, (int) y));
	}

	public static void scrollCallback(double x, double y) {
		SceneTree.getRoot().input(new MouseScrollInputEvent((float) x, (float) y));
	}

	public static Vec2i position() {
		return position;
	}

	public static boolean isButtonDown(int button) {
		return MOUSE.contains(button);
	}
}
