package gamma.engine.input;

import io.github.hexagonnico.vecmatlib.vector.Vec2i;

public record MouseCursorInputEvent(int x, int y) implements InputEvent {

	public Vec2i position() {
		return new Vec2i(this.x(), this.y());
	}
}
