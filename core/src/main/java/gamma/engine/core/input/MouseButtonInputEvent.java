package gamma.engine.core.input;

import org.lwjgl.glfw.GLFW;

public class MouseButtonInputEvent implements InputEvent {

	private final int button, action, mods;

	public MouseButtonInputEvent(int button, int action, int mods) {
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
}
