package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;

/**
 * Input event that represents a mouse movement.
 *
 * @param position Position of the mouse in pixels from the top left corner of the window.
 * @param motion Movement of the mouse in pixels from its last position.
 */
public record InputEventMouseMotion(Vector2 position, Vector2 motion) implements InputEventMouse {

    /**
     * Input event that represents a mouse movement.
     *
     * @param px Position of the mouse in pixels from the left side of the window.
     * @param py Position of the mouse in pixels from the upper side of the window.
     * @param motion Movement of the mouse in pixels from its last position.
     */
    public InputEventMouseMotion(float px, float py, Vector2 motion) {
        this(new Vector2(px, py), motion);
    }

    /**
     * Input event that represents a mouse movement.
     *
     * @param position Position of the mouse in pixels from the top left corner of the window.
     * @param mx Movement of the mouse in pixels from its last position on the x axis.
     * @param my Movement of the mouse in pixels from its last position on the y axis.
     */
    public InputEventMouseMotion(Vector2 position, float mx, float my) {
        this(position, new Vector2(mx, my));
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
        this(new Vector2(px, py), new Vector2(mx, my));
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

    /**
     * Returns the mouse motion on the x axis.
     * Equivalent to {@code event.motion().x()}.
     *
     * @return The mouse motion on the x axis.
     */
    public float x() {
        return this.motion().x();
    }

    /**
     * Returns the mouse motion on the y axis.
     * Equivalent to {@code event.motion().y()}.
     *
     * @return The mouse motion on the y axis.
     */
    public float y() {
        return this.motion().y();
    }
}