package io.github.ardentengine.core.input;

import io.github.ardentengine.core.math.Vector2;

/**
 * An input event that represents a mouse button being pressed or released.
 *
 * @param button The mouse button identifier.
 * @param modifiers Bitmask for keyboard modifiers.
 * @param position Position of the mouse in pixels from the top left corner of the window.
 * @param isPressed If true, the mouse button's state is pressed. If false, the mouse button's state is released.
 */
public record InputEventMouseButton(int button, int modifiers, Vector2 position, boolean isPressed) implements InputEventMouse, InputEventWithModifiers {

    public static final int BUTTON_LEFT = 0;
    public static final int BUTTON_RIGHT = 1;
    public static final int BUTTON_MIDDLE = 2;
    public static final int BUTTON_4 = 3;
    public static final int BUTTON_5 = 4;
    public static final int BUTTON_6 = 5;
    public static final int BUTTON_7 = 6;
    public static final int BUTTON_8 = 7;

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
        this(button, modifiers, new Vector2(x, y), isPressed);
    }

    public InputEventMouseButton(int button, Vector2 position, boolean isPressed) {
        this(button, 0, position, isPressed);
    }

    public InputEventMouseButton(int button, float x, float y, boolean isPressed) {
        this(button, 0, x, y, isPressed);
    }

    public InputEventMouseButton(int button, int modifiers, boolean isPressed) {
        this(button, modifiers, 0.0f, 0.0f, isPressed);
    }

    public InputEventMouseButton(int button, boolean isPressed) {
        this(button, 0, isPressed);
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