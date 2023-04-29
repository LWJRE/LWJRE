package gamma.engine.input;

import gamma.engine.window.WindowListener;

import java.util.HashSet;

public class InputSystem implements WindowListener {

	/** Set of keys currently held down */
	private static final HashSet<Integer> KEYBOARD = new HashSet<>();
	/** Set of buttons currently held down */
	private static final HashSet<Integer> MOUSE = new HashSet<>();

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

	@Override
	public void onWindowInput(InputEvent event) {
		if(event.isPressed()) {
			if(event instanceof KeyInputEvent keyEvent) {
				KEYBOARD.add(keyEvent.key());
			} else if(event instanceof MouseButtonInputEvent mouseEvent) {
				MOUSE.add(mouseEvent.button());
			}
		} else if (event instanceof KeyInputEvent keyEvent) {
			KEYBOARD.remove(keyEvent.key());
		} else if (event instanceof MouseButtonInputEvent mouseEvent) {
			MOUSE.remove(mouseEvent.button());
		}
	}
}
