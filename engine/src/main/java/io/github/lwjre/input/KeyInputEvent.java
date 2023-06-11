package io.github.lwjre.input;

import org.lwjgl.glfw.GLFW;

/**
 * Input event that represents a keyboard key event.
 *
 * @param key Key code
 * @param scancode Scancode
 * @param action Either {@link GLFW#GLFW_PRESS}, {@link GLFW#GLFW_RELEASE}, or {@link GLFW#GLFW_REPEAT}
 * @param mods Flags to determine which modifier keys were pressed
 */
public record KeyInputEvent(int key, int scancode, int action, int mods) implements InputEvent {

	@Override
	public boolean isPressed() {
		return this.action == GLFW.GLFW_PRESS;
	}

	@Override
	public boolean isReleased() {
		return this.action == GLFW.GLFW_RELEASE;
	}

	@Override
	public boolean isKey() {
		return true;
	}

	@Override
	public boolean isKey(int key) {
		return this.key == key;
	}

	@Override
	public boolean isKeyPressed(int key) {
		return this.isPressed() && this.isKey(key);
	}

	@Override
	public boolean isKeyReleased(int key) {
		return this.isReleased() && this.isKey(key);
	}

	// TODO: Add methods for mods
}
