package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;

/**
 * Input event that represents a scroll on a mouse wheel or touchpad.
 *
 * @param horizontal Horizontal scroll.
 * @param vertical Vertical scroll.
 */
public record InputEventScroll(float horizontal, float vertical) implements InputEvent {

    /**
     * Constructs a scroll event with the give scroll.
     *
     * @param scroll Scroll on the x and y axes.
     */
    public InputEventScroll(Vector2 scroll) {
        this(scroll.x(), scroll.y());
    }

    /**
     * Returns the scroll on the x and y axes.
     *
     * @return The scroll on the x and y axes.
     */
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
        return this.equals(event);
    }

    @Override
    public float strength() {
        return 0.0f;
    }
}