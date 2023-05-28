package gamma.engine.input;

import io.github.hexagonnico.vecmatlib.vector.Vec2f;

/**
 * Input event that represents a mouse scroll event.
 * 
 * @see Mouse#scrollCallback(double, double) 
 * 
 * @param horizontal
 * @param vertical
 */
public record MouseScrollInputEvent(float horizontal, float vertical) implements InputEvent {

	/**
	 * Returns a float vector containing the mouse's horizontal and vertical scroll.
	 *
	 * @return A {@link Vec2f} containing the mouse's scroll on the x and y axis
	 */
	public Vec2f scroll() {
		return new Vec2f(this.horizontal(), this.vertical());
	}
}
