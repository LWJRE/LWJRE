package io.github.ardentengine.core.input;

import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Input event that represents a scroll on a mouse wheel or touchpad.
 *
 * @param horizontal Horizontal scroll.
 * @param vertical Vertical scroll.
 */
public record InputEventScroll(float horizontal, float vertical) implements InputEvent {

    public InputEventScroll(Vec2f scroll) {
        this(scroll.x(), scroll.y());
    }

    public Vec2f scroll() {
        return new Vec2f(this.horizontal, this.vertical);
    }

    @Override
    public boolean isPressed() {
        return false;
    }

    @Override
    public boolean isReleased() {
        return false;
    }

    @Override
    public boolean isEcho() {
        return false;
    }

    @Override
    public boolean matches(InputEvent event, boolean exact) {
        return event instanceof InputEventScroll inputEventScroll && inputEventScroll.scroll().equalsApprox(this.scroll());
    }

    @Override
    public float strength() {
        return 0.0f;
    }
}