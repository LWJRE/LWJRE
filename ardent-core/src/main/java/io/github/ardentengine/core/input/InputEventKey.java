package io.github.ardentengine.core.input;

/**
 * An input event that represents a key on a keyboard being pressed or released.
 *
 * @param keyCode The key that was pressed or released.
 * @param modifiers Bitmask for keyboard modifiers.
 * @param isPressed If true, the key's state is pressed. If false, the key's state is released.
 * @param isEcho If true, the key was already pressed before this event. It means the user is holding the key down.
 */
public record InputEventKey(int keyCode, int modifiers, boolean isPressed, boolean isEcho) implements InputEventWithModifiers {

    @Override
    public boolean matches(InputEvent event, boolean exact) {
        return event instanceof InputEventKey inputEventKey && inputEventKey.keyCode() == this.keyCode && (!exact || inputEventKey.modifiers() == this.modifiers());
    }
}