package gamma.engine.core.input;

import vecmatlib.vector.Vec2d;
import vecmatlib.vector.Vec2f;

public class MouseScrollInputEvent implements InputEvent {

	public final Vec2f scroll;

	public MouseScrollInputEvent(double x, double y) {
		this.scroll = new Vec2d(x, y).toFloat();
	}

	public float horizontal() {
		return this.scroll.x();
	}

	public float vertical() {
		return this.scroll.y();
	}
}
