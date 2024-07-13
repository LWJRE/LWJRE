package io.github.ardentengine.core.input;

import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Input event that represents a scroll on a mouse wheel or touchpad.
 *
 * @param scroll Scroll amount.
 */
public record InputEventScroll(Vec2f scroll) implements InputEvent {

    /**
     * Input event that represents a scroll on a mouse wheel or touchpad.
     *
     * @param x Scroll amount on the x axis.
     * @param y Scroll amount on the y axis.
     */
    public InputEventScroll(float x, float y) {
        this(new Vec2f(x, y));
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
