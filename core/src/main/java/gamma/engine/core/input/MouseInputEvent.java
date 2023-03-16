package gamma.engine.core.input;

import org.lwjgl.glfw.GLFW;

public class MouseInputEvent implements InputEvent {

	private final int button, action, mods;

	public MouseInputEvent(int button, int action, int mods) {
		this.button = button;
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
