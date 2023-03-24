package gamma.engine.core.input;

/**
 * Interface for a generic input event.
 * Contains default methods to be overridden by input events.
 *
 * @see KeyInputEvent
 * @see MouseButtonInputEvent
 *
 * @author Nico
 */
public interface InputEvent {

	/**
	 * Returns true if this event represents the press of a button, otherwise false.
	 *
	 * @return True if this event represents the press of a button, otherwise false
	 */
	default boolean isPressed() {
		return false;
	}

	/**
	 * Returns true if this event represents the release of a button, otherwise false.
	 *
	 * @return True if this event represents the release of a button, otherwise false
	 */
	default boolean isReleased() {
		return false;
	}

	/**
	 * Returns true if this event comes from a keyboard key, otherwise false.
	 *
	 * @return True if this event comes from a keyboard key, otherwise false
	 */
	default boolean isKey() {
		return false;
	}

	/**
	 * Returns true if this event comes from the given keyboard key, otherwise false.
	 * See {@link Keyboard} for key codes.
	 *
	 * @param key The keyboard key
	 * @return True if this event comes from the given keyboard key, otherwise false.
	 */
	default boolean isKey(int key) {
		return false;
	}

	/**
	 * Returns true if this event comes from the press of the given keyboard key, otherwise false.
	 * See {@link Keyboard} for key codes.
	 *
	 * @param key The keyboard key
	 * @return True if this event comes from the press of the given keyboard key, otherwise false.
	 */
	default boolean isKeyPressed(int key) {
		return false;
	}

	/**
	 * Returns true if this event comes from the release of the given keyboard key, otherwise false.
	 * See {@link Keyboard} for key codes.
	 *
	 * @param key The keyboard key
	 * @return True if this event comes from the release of the given keyboard key, otherwise false.
	 */
	default boolean isKeyReleased(int key) {
		return false;
	}

	/**
	 * Returns true if this event comes from a mouse button, otherwise false.
	 *
	 * @return True if this event comes from a mouse button, otherwise false
	 */
	default boolean isMouseButton() {
		return false;
	}

	/**
	 * Returns true if this event comes from the given mouse button, otherwise false.
	 * See {@link Mouse} for button codes.
	 *
	 * @param button The mouse button
	 * @return True if this event comes from the given mouse button, otherwise false.
	 */
	default boolean isMouseButton(int button) {
		return false;
	}

	/**
	 * Returns true if this event comes from the press of the given mouse button, otherwise false.
	 * See {@link Mouse} for button codes.
	 *
	 * @param button The mouse button
	 * @return True if this event comes from the press of the given mouse button, otherwise false.
	 */
	default boolean isMouseButtonPressed(int button) {
		return false;
	}

	/**
	 * Returns true if this event comes from the release of the given mouse button, otherwise false.
	 * See {@link Mouse} for button codes.
	 *
	 * @param button The mouse button
	 * @return True if this event comes from the release of the given mouse button, otherwise false.
	 */
	default boolean isMouseButtonReleased(int button) {
		return false;
	}
}
