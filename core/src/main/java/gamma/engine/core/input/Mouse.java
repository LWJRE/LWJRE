package gamma.engine.core.input;

import org.lwjgl.glfw.GLFW;
import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2i;

import java.util.HashSet;

public final class Mouse {

	private static final HashSet<Integer> BUTTONS = new HashSet<>();
	private static Vec2i position = Vec2i.Zero();

	public static void buttonCallback(int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS)
			BUTTONS.add(button);
		else if(action == GLFW.GLFW_RELEASE)
			BUTTONS.remove(button);
	}

	public static void cursorPosCallback(double x, double y) {
		position = new Vec2d(x, y).toInt();
	}

	public static Vec2i position() {
		return position;
	}

	public static boolean isButtonDown(int button) {
		return BUTTONS.contains(button);
	}
}
