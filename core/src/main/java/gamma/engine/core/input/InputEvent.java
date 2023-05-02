package gamma.engine.core.input;

public interface InputEvent {

	default boolean isPressed() {
		return false;
	}

	default boolean isReleased() {
		return false;
	}

	default boolean isKey() {
		return false;
	}

	default boolean isKey(int key) {
		return false;
	}

	default boolean isKeyPressed(int key) {
		return false;
	}

	default boolean isKeyReleased(int key) {
		return false;
	}

	default boolean isMouseButton() {
		return false;
	}

	default boolean isMouseButton(int button) {
		return false;
	}

	default boolean isMouseButtonPressed(int button) {
		return false;
	}

	default boolean isMouseButtonReleased(int button) {
		return false;
	}
}
