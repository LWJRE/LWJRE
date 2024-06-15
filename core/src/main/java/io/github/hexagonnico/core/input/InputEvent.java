package io.github.hexagonnico.core.input;

/**
 * Base interface for input events.
 *
 * @see io.github.hexagonnico.core.scene.Node#onInput(InputEvent)
 */
public interface InputEvent {

    /**
     * Checks if this input event is pressed.
     * Used for input events that represent a key or button.
     *
     * @return True if this input event is pressed, otherwise false.
     */
    boolean isPressed();

    /**
     * Checks if this input event is released.
     * Used for input events that represent a key or button.
     *
     * @return True if this input event is released, otherwise false.
     */
    default boolean isReleased() {
        return !this.isPressed();
    }

    /**
     * Checks if this input event is an echo event.
     * <p>
     *     Only used for {@link InputEventKey}.
     *     Any other event returns false.
     * </p>
     *
     * @return True if this input event is an echo event, otherwise false.
     */
    boolean isEcho();

    /**
     * Checks if this event matches the given one.
     * Can be used to check if the given event corresponds to the same key or button.
     * <p>
     *     Only valid for {@link InputEventKey}, {@link InputEventMouseButton}, and {@link InputEventScroll}.
     *     Any other event returns false.
     * </p>
     * <p>
     *     If {@code exact} is false, additional input modifiers will be ignored.
     * </p>
     *
     * @param event The input event.
     * @param exact If false, additional input modifiers will be ignored.
     * @return True if this event matches the given one, otherwise false.
     */
    boolean matches(InputEvent event, boolean exact);

    /**
     * Checks if this event matches the given one.
     * Can be used to check if the given event corresponds to the same key or button.
     * <p>
     *     Only valid for {@link InputEventKey} and {@link InputEventMouseButton}.
     *     Any other event returns false.
     * </p>
     *
     * @param event The input event.
     * @return True if this event matches the given one, otherwise false.
     */
    default boolean matches(InputEvent event) {
        return this.matches(event, false);
    }

    /**
     * Checks if this input event matches an action specified in {@link InputMap}.
     * <p>
     *     If {@code exact} is false, additional input modifiers will be ignored.
     * </p>
     *
     * @param action Name of the action.
     * @param exact If false, additional input modifiers will be ignored.
     * @return True if this input event matches the given action, otherwise false.
     */
    default boolean isAction(String action, boolean exact) {
        return InputMap.eventIsAction(this, action, exact);
    }

    /**
     * Checks if this input event matches an action specified in {@link InputMap}.
     *
     * @param action Name of the action.
     * @return True if this input event matches the given action, otherwise false.
     */
    default boolean isAction(String action) {
        return this.isAction(action, false);
    }

    /**
     * Checks if this input event is pressed, is not an echo event, and matches the given action.
     * <p>
     *     If {@code exact} is false, additional input modifiers will be ignored.
     * </p>
     *
     * @param action Name of the action.
     * @param exact If false, additional input modifiers will be ignored.
     * @return True if this input event is pressed and matches the given action.
     * @see InputEvent#isAction(String)
     * @see InputEvent#isPressed()
     */
    default boolean isActionPressed(String action, boolean exact) {
        return this.isPressed() && !this.isEcho() && this.isAction(action, exact);
    }

    /**
     * Checks if this input event is pressed, is not an echo event, and matches the given action.
     *
     * @param action Name of the action.
     * @return True if this input event is pressed and matches the given action.
     * @see InputEvent#isActionPressed(String, boolean)
     */
    default boolean isActionPressed(String action) {
        return this.isActionPressed(action, false);
    }

    /**
     * Checks if this input event is released and matches the given action.
     * <p>
     *     If {@code exact} is false, additional input modifiers will be ignored.
     * </p>
     *
     * @param action Name of the action.
     * @param exact If false, additional input modifiers will be ignored.
     * @return True if this input event is released and matches the given action.
     * @see InputEvent#isAction(String)
     * @see InputEvent#isReleased()
     */
    default boolean isActionReleased(String action, boolean exact) {
        return this.isReleased() && this.isAction(action, exact);
    }

    /**
     * Checks if this input event is released and matches the given action.
     *
     * @param action Name of the action.
     * @return True if this input event is released and matches the given action.
     * @see InputEvent#isActionReleased(String, boolean)
     */
    default boolean isActionReleased(String action) {
        return this.isAction(action, false);
    }

    /**
     * Returns the strength of this input event as a number between 0 and 1.
     * <p>
     *     For input events that correspond to a key or a button, returns 1 if the event is pressed, otherwise 0.
     *     For input events that correspond to triggers or analog stick, the number indicates how far away the input is from the dead zone.
     * </p>
     *
     * @return The strength of this input event.
     */
    default float strength() {
        return this.isPressed() ? 1.0f : 0.0f;
    }

    /**
     * Returns the {@link InputEvent#strength()} of this input event if it corresponds to the given action.
     * Returns 0 if this input event does not correspond to the given action.
     *
     * @param action Name of the action.
     * @return The strength of this input event if it corresponds to the given action, otherwise 0.
     * @see InputEvent#isAction(String)
     */
    default float getActionStrength(String action) {
        return this.isAction(action) ? this.strength() : 0.0f;
    }

    /**
     * Returns the {@link InputEvent#strength()} of this input event if it corresponds to the given action.
     * Returns 0 if this input event does not correspond to the given action.
     *
     * @param action Name of the action.
     * @return The strength of this input event if it corresponds to the given action, otherwise 0.
     * @see InputEvent#isAction(String, boolean)
     */
    default float getActionStrength(String action, boolean exact) {
        return this.isAction(action, exact) ? this.strength() : 0.0f;
    }
}
