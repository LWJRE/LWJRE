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
}
