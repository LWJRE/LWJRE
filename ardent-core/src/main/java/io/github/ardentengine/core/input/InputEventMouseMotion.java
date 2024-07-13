package io.github.ardentengine.core.input;

import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Input event that represents a mouse movement.
 *
 * @param position Position of the mouse in pixels from the top left corner of the window.
 * @param motion Movement of the mouse in pixels from its last position.
 */
public record InputEventMouseMotion(Vec2f position, Vec2f motion) implements InputEventMouse {

    /**
     * Input event that represents a mouse movement.
     *
     * @param px Position of the mouse in pixels from the left side of the window.
     * @param py Position of the mouse in pixels from the upper side of the window.
     * @param motion Movement of the mouse in pixels from its last position.
     */
    public InputEventMouseMotion(float px, float py, Vec2f motion) {
        this(new Vec2f(px, py), motion);
    }

    /**
     * Input event that represents a mouse movement.
     *
     * @param position Position of the mouse in pixels from the top left corner of the window.
     * @param mx Movement of the mouse in pixels from its last position on the x axis.
     * @param my Movement of the mouse in pixels from its last position on the y axis.
     */
    public InputEventMouseMotion(Vec2f position, float mx, float my) {
        this(position, new Vec2f(mx, my));
    }

    /**
     * Input event that represents a mouse movement.
     *
     * @param px Position of the mouse in pixels from the left side of the window.
     * @param py Position of the mouse in pixels from the upper side of the window.
     * @param mx Movement of the mouse in pixels from its last position on the x axis.
     * @param my Movement of the mouse in pixels from its last position on the y axis.
     */
    public InputEventMouseMotion(float px, float py, float mx, float my) {
        this(new Vec2f(px, py), new Vec2f(mx, my));
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
        return false;
    }

    @Override
    public float strength() {
        return 0.0f;
    }
}
