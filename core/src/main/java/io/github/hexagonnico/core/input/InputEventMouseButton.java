package io.github.hexagonnico.core.input;

import io.github.scalamath.vecmatlib.Vec2f;

/**
 * An input event that represents a mouse button being pressed or released.
 *
 * @param button The mouse button identifier.
 * @param modifiers Bitmask for keyboard modifiers.
 * @param position Position of the mouse in pixels from the top left corner of the window.
 * @param isPressed If true, the mouse button's state is pressed. If false, the mouse button's state is released.
 */
public record InputEventMouseButton(int button, int modifiers, Vec2f position, boolean isPressed) implements InputEventMouse, InputEventWithModifiers {

    /**
     * An input event that represents a mouse button being pressed or released.
     *
     * @param button The mouse button identifier.
     * @param modifiers Bitmask for keyboard modifiers.
     * @param x Position of the mouse in pixels from the left side of the window.
     * @param y Position of the mouse in pixels from the upper side of the window.
     * @param isPressed If true, the mouse button's state is pressed. If false, the mouse button's state is released.
     */
    public InputEventMouseButton(int button, int modifiers, float x, float y, boolean isPressed) {
        this(button, modifiers, new Vec2f(x, y), isPressed);
    }

    @Override
    public boolean isEcho() {
        return false;
    }

    @Override
    public boolean matches(InputEvent event, boolean exact) {
        return event instanceof InputEventMouseButton inputEventMouseButton && inputEventMouseButton.button() == this.button() && (!exact || inputEventMouseButton.modifiers() == this.modifiers());
    }
}