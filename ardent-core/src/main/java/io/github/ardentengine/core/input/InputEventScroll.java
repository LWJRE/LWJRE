package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;

/**
 * Input event that represents a scroll on a mouse wheel or touchpad.
 *
 * @param horizontal Horizontal scroll.
 * @param vertical Vertical scroll.
 */
public record InputEventScroll(float horizontal, float vertical) implements InputEvent {

    public InputEventScroll(Vector2 scroll) {
        this(scroll.x(), scroll.y());
    }

    public Vector2 scroll() {
        return new Vector2(this.horizontal, this.vertical);
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