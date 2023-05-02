package gamma.engine.window.input;

import gamma.engine.core.input.InputEvent;
import vecmatlib.vector.Vec2i;

public record MouseCursorInputEvent(int x, int y) implements InputEvent {

	public Vec2i position() {
		return new Vec2i(this.x(), this.y());
	}
}
