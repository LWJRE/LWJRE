package io.github.ardentengine.core.input;

/**
 * Input event that represents an input action.
 * <p>
 *     This event isn't triggered by the engine, but can be fired manually to simulate an action event.
 * </p>
 *
 * @param action Name of the input action as it is defined in {@link InputMap}.
 * @param isPressed True if the action is pressed, false if it is released.
 * @param strength Strength of the input action.
 */
public record InputEventAction(String action, boolean isPressed, float strength) implements InputEvent {

    /**
     * Input event that represents an input action.
     * <p>
     *     This event isn't triggered by the engine, but can be fired manually to simulate an action event.
     * </p>
     *
     * @param action Name of the input action as it is defined in {@link InputMap}.
     * @param isPressed True if the action is pressed, false if it is released.
     */
    public InputEventAction(String action, boolean isPressed) {
        this(action, isPressed, isPressed ? 1.0f : 0.0f);
    }

    @Override
    public boolean isEcho() {
        return false;
    }

    @Override
    public boolean isAction(String action, boolean exact) {
        return this.action().equals(action);
    }

    @Override
    public boolean matches(InputEvent event, boolean exact) {
        return event.isAction(this.action(), exact);
    }
}