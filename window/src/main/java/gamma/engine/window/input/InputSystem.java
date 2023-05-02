package gamma.engine.window.input;

import gamma.engine.core.scene.Scene;
import gamma.engine.core.WindowListener;
import org.lwjgl.glfw.GLFW;
import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2i;

import java.util.HashSet;

public class InputSystem implements WindowListener {

	/** Set of keys currently held down */
	private static final HashSet<Integer> KEYBOARD = new HashSet<>();
	/** Set of buttons currently held down */
	private static final HashSet<Integer> MOUSE = new HashSet<>();

	private static Vec2i mousePosition = Vec2i.Zero();

	/**
	 * Returns true if the given key is being held down, otherwise false.
	 * This method is equivalent to {@link Keyboard#isKeyDown(int)}.
	 *
	 * @param key The key's code
	 * @return True if the given key is being held down, otherwise false
	 */
	public static boolean isKeyDown(int key) {
		return KEYBOARD.contains(key);
	}

	/**
	 * Returns true if the given mouse button is being held down, otherwise false.
	 * This method is equivalent to {@link Mouse#isButtonDown(int)}.
	 *
	 * @param button The button's code
	 * @return True if the given mouse button is being held down, otherwise false
	 */
	public static boolean isMouseButtonDown(int button) {
		return MOUSE.contains(button);
	}

	public static Vec2i mousePosition() {
		return mousePosition;
	}

	@Override
	public void onKeyInput(int key, int scancode, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			KEYBOARD.add(key);
		} else if(action == GLFW.GLFW_RELEASE) {
			KEYBOARD.remove(key);
		}
		Scene.getRoot().input(new KeyInputEvent(key, scancode, action, mods));
	}

	@Override
	public void onMouseButtonInput(int button, int action, int mods) {
		if(action == GLFW.GLFW_PRESS) {
			MOUSE.add(button);
		} else if(action == GLFW.GLFW_RELEASE) {
			MOUSE.remove(button);
		}
		Scene.getRoot().input(new MouseButtonInputEvent(button, action, mods));
	}

	@Override
	public void onCursorInput(double x, double y) {
		mousePosition = new Vec2d(x, y).toInt();
		Scene.getRoot().input(new MouseCursorInputEvent((int) x, (int) y));
	}

	@Override
	public void onMouseScrollInput(double x, double y) {
		Scene.getRoot().input(new MouseScrollInputEvent((float) x, (float) y));
	}
}
