package gamma.engine.core.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashSet;

public final class Keyboard {

	private static final HashSet<Integer> KEYS = new HashSet<>();

	public static void keyCallback(int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS)
			KEYS.add(key);
		else if(action == GLFW.GLFW_RELEASE)
			KEYS.remove(key);
	}

	public static boolean isKeyDown(int key) {
		return KEYS.contains(key);
	}

	public static int getAxis(int negative, int positive) {
		boolean isNegative = isKeyDown(negative);
		boolean isPositive = isKeyDown(positive);
		return isPositive && !isNegative ? 1 : !isPositive && isNegative ? -1 : 0;
	}
}
