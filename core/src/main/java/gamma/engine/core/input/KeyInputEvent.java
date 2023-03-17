package gamma.engine.core.input;

import org.lwjgl.glfw.GLFW;

public class KeyInputEvent implements InputEvent {

	private final int key, scancode, action, mods;

	public KeyInputEvent(int key, int scancode, int action, int mods) {
		this.key = key;
		this.scancode = scancode;
		this.action = action;
		this.mods = mods;
	}

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
}
