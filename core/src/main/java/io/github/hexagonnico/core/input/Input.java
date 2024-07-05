package io.github.hexagonnico.core.input;

import io.github.hexagonnico.core.Application;
import io.github.scalamath.vecmatlib.Vec2f;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Static class used for handling inputs.
 */
public final class Input {

    /**
     * Set of pressed action events.
     */
    private static final HashMap<InputEvent, Long> PRESSED_STATE = new HashMap<>();
    /**
     * Current event dispatch function.
     */
    private static Consumer<InputEvent> eventDispatchFunction = null;

    /**
     * Sets the function to which input events are sent when {@link Input#parseEvent(InputEvent)} is called.
     * <p>
     *     By default, the event dispatch function is set to send input events to the scene tree when the main loop is initialized.
     * </p>
     * <p>
     *     Can be set to {@code null} to disable the event dispatch function.
     *     Other methods in the input will still work.
     * </p>
     *
     * @param inputEventConsumer The new event dispatch function.
     * @return The event dispatch function that was previously set or {@code null} if none was set.
     */
    public static Consumer<InputEvent> setEventDispatchFunction(Consumer<InputEvent> inputEventConsumer) {
        Consumer<InputEvent> previous = eventDispatchFunction;
        eventDispatchFunction = inputEventConsumer;
        return previous;
    }

    /**
     * Parses an input event.
     * Can be used to artificially trigger input events from code.
     *
     * @param event The event to parse.
     */
    public static void parseEvent(InputEvent event) {
        if(eventDispatchFunction != null) {
            eventDispatchFunction.accept(event);
        }
        if(event.isPressed() && !event.isEcho()) {
            PRESSED_STATE.put(event, Application.getProcessFrames());
        } else if(event.isReleased()) {
            PRESSED_STATE.keySet().removeIf(event::matches);
        }
    }

    /**
     * Returns true if the given action is being pressed.
     *
     * @param action The action name.
     * @return True if the action is being pressed, otherwise false.
     */
    public static boolean isActionPressed(String action) {
        for(var event : PRESSED_STATE.keySet()) {
            if(event.isAction(action)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given action has been pressed in the current frame.
     * Used for code that needs to run only once when an action is pressed, instead of every frame while it is pressed.
     *
     * @param action The action name.
     * @return True if the given action has been pressed in the current frame, otherwise false.
     */
    public static boolean isActionJustPressed(String action) {
        for(var entry : PRESSED_STATE.entrySet()) {
            if(entry.getKey().isAction(action) && entry.getValue() == Application.getProcessFrames()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if the given action has been released in the current frame.
     *
     * @param action The action name.
     * @return True if the given action has been released in the current frame, otherwise false.
     */
    public static boolean isActionJustReleased(String action) {
        return false; // TODO: Better implementation of pressed / just pressed / just released
    }

    /**
     * Returns true if the given key is being pressed.
     *
     * @param key The key code.
     * @return True if the key is being pressed, otherwise false.
     */
    public static boolean isKeyPressed(int key) {
        for(var event : PRESSED_STATE.keySet()) {
            if(event instanceof InputEventKey eventKey && eventKey.keyCode() == key) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the strength of the given action as a number between 0 and 1.
     * <p>
     *     For input events that correspond to a key or a button, returns 1 if the event is pressed, otherwise 0.
     *     For input events that correspond to triggers or analog stick, the number indicates how far away the input is from the dead zone.
     * </p>
     * <p>
     *     If {@code exact} is false, additional input modifiers will be ignored.
     * </p>
     *
     * @param action The action name.
     * @param exact If false, additional input modifiers will be ignored.
     * @return The strength of the specified action.
     */
    public static float getActionStrength(String action, boolean exact) {
        for(var event : PRESSED_STATE.keySet()) {
            if(event.isAction(action, exact)) {
                return event.strength();
            }
        }
        return 0.0f;
    }

    /**
     * Returns the strength of the given action as a number between 0 and 1.
     * <p>
     *     For input events that correspond to a key or a button, returns 1 if the event is pressed, otherwise 0.
     *     For input events that correspond to triggers or analog stick, the number indicates how far away the input is from the dead zone.
     * </p>
     *
     * @param action The action name.
     * @return The strength of the specified action.
     */
    public static float getActionStrength(String action) {
        return getActionStrength(action, false);
    }

    /**
     * Returns the axis input specified by the given positive and negative action.
     *
     * @param positive Name of the positive action.
     * @param negative Name of the negative action.
     * @return The axis input specified by the given positive and negative action.
     */
    public static float getAxis(String positive, String negative) {
        return getActionStrength(positive) - getActionStrength(negative);
    }

    /**
     * Returns an input vector specified by the four given actions.
     * Useful to get a vector input, such as from a joystick, directional pad, arrow keys, or WASD keys.
     * The length of the resulting vector is limited to 1.
     *
     * @param up Name of the positive action on the y axis.
     * @param down Name of the negative action on the y axis.
     * @param left Name of the negative action on the x axis.
     * @param right Name of the positive action on the x axis.
     * @return An input vector specified by the four given actions.
     */
    public static Vec2f getVector(String up, String down, String left, String right) {
        var vector = new Vec2f(getAxis(right, left), getAxis(up, down));
        if(vector.lengthSquared() > 1.0f) {
            return vector.normalized();
        }
        return vector;
    }
}
