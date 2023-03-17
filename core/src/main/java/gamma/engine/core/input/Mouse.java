package gamma.engine.core.input;

import gamma.engine.core.scene.Scene;
import org.lwjgl.glfw.GLFW;
import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2i;

import java.util.HashSet;

public final class Mouse {

	public static final int BUTTON_LEFT = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int BUTTON_RIGHT = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int BUTTON_MIDDLE = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	public static final int BUTTON_4 = GLFW.GLFW_MOUSE_BUTTON_4;
	public static final int BUTTON_5 = GLFW.GLFW_MOUSE_BUTTON_5;
	public static final int BUTTON_6 = GLFW.GLFW_MOUSE_BUTTON_6;
	public static final int BUTTON_7 = GLFW.GLFW_MOUSE_BUTTON_7;
	public static final int BUTTON_8 = GLFW.GLFW_MOUSE_BUTTON_8;

	private static final HashSet<Integer> BUTTONS = new HashSet<>();
	private static Vec2i position = Vec2i.Zero();

	public static void buttonCallback(int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS)
			BUTTONS.add(button);
		else if(action == GLFW.GLFW_RELEASE)
			BUTTONS.remove(button);
		Scene.getCurrent().processInput(new MouseButtonInputEvent(button, action, mods));
	}

	public static void cursorPosCallback(double x, double y) {
		position = new Vec2d(x, y).toInt();
	}

	public static void scrollWheelCallback(double x, double y) {
		Scene.getCurrent().processInput(new MouseScrollInputEvent(x, y));
	}

	public static Vec2i position() {
		return position;
	}

	public static boolean isButtonDown(int button) {
		return BUTTONS.contains(button);
	}
}
