package io.github.lwjre.input;

import org.lwjgl.glfw.GLFW;

/**
 * Input event that represents a mouse button event.
 *
 * @param button Mouse button code
 * @param action Either {@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_RELEASE}, or {@link GLFW#GLFW_REPEAT}
 * @param mods Flags to determine which modifier keys were pressed
 */
public record MouseButtonInputEvent(int button, int action, int mods) implements InputEvent {

	@Override
	public boolean isPressed() {
		return this.action == GLFW.GLFW_PRESS;
	}

	@Override
	public boolean isReleased() {
		return this.action == GLFW.GLFW_RELEASE;
	}

	@Override
	public boolean isMouseButton() {
		return true;
	}

	@Override
	public boolean isMouseButton(int button) {
		return this.button == button;
	}

	@Override
	public boolean isMouseButtonPressed(int button) {
		return this.isPressed() && this.isMouseButton(button);
	}

	@Override
	public boolean isMouseButtonReleased(int button) {
		return this.isReleased() && this.isMouseButton(button);
	}

	// TODO: Add methods for mods
}
