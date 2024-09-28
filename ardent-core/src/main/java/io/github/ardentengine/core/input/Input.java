package io.github.ardentengine.core.input;

import io.github.ardentengine.core.Application;
import io.github.ardentengine.core.math.Vector2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Static class used for handling inputs.
 */
public final class Input {

    /** Map used to keep track of input action states. */
    private static final HashMap<String, ActionState> ACTION_STATES = new HashMap<>();
    /** Set of keyboard keys that are currently being pressed. */
    private static final HashSet<Integer> PRESSED_KEYS = new HashSet<>();
    /** Set of mouse buttons that are currently being pressed. */
    private static final HashSet<Integer> PRESSED_MOUSE_BUTTONS = new HashSet<>();

    /** Current event dispatch function. */
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
        // Update action states
        for(var action : InputMap.getActions()) {
            if(event.isAction(action)) {
                var actionState = ACTION_STATES.computeIfAbsent(action, a -> new ActionState());
                if(event.isPressed()) {
                    // Update the action state for a pressed event
                    if(!event.isEcho()) {
                        actionState.pressed = Application.processFrames();
                    }
                    actionState.released = -1L;
                    // Use the highest strenth for pressed events
                    actionState.strength = Math.max(actionState.strength, event.strength());
                } else if(event.isReleased()) {
                    // Update the action state for a released event
                    actionState.pressed = -1L;
                    actionState.released = Application.processFrames();
                    // Use the lower strength for released events
                    actionState.strength = Math.min(actionState.strength, event.strength());
                }
                // TODO: Handle events that are not pressed nor released
            }
        }
        if(event instanceof InputEventKey inputEventKey) {
            // Update key state
            if(event.isPressed()) {
                PRESSED_KEYS.add(inputEventKey.keyCode());
            } else if(event.isReleased()) {
                PRESSED_KEYS.remove(inputEventKey.keyCode());
            }
        } else if(event instanceof InputEventMouseButton inputEventMouseButton) {
            // Update mouse button state
            if(event.isPressed()) {
                PRESSED_MOUSE_BUTTONS.add(inputEventMouseButton.button());
            } else if(event.isReleased()) {
                PRESSED_MOUSE_BUTTONS.remove(inputEventMouseButton.button());
            }
        }
        if(eventDispatchFunction != null) {
            eventDispatchFunction.accept(event);
        }
    }

    /**
     * Returns true if the given action is being pressed.
     *
     * @param action The action name.
     * @return True if the action is being pressed, otherwise false.
     */
    public static boolean isActionPressed(String action) {
        var actionState = ACTION_STATES.get(action);
        return actionState != null && actionState.pressed >= 0;
    }

    /**
     * Returns true if the given action has been pressed in the current frame.
     * Used for code that needs to run only once when an action is pressed, instead of every frame while it is pressed.
     *
     * @param action The action name.
     * @return True if the given action has been pressed in the current frame, otherwise false.
     */
    public static boolean isActionJustPressed(String action) {
        var actionState = ACTION_STATES.get(action);
        return actionState != null && actionState.pressed == Application.processFrames();
    }

    /**
     * Returns true if the given action has been released in the current frame.
     *
     * @param action The action name.
     * @return True if the given action has been released in the current frame, otherwise false.
     */
    public static boolean isActionJustReleased(String action) {
        var actionState = ACTION_STATES.get(action);
        return actionState != null && actionState.released == Application.processFrames();
    }

    /**
     * Returns true if the given key is being pressed.
     * <p>
     *     See {@link InputEventKey} for key code constants.
     * </p>
     *
     * @param key The key code.
     * @return True if the key is being pressed, otherwise false.
     */
    public static boolean isKeyPressed(int key) {
        return PRESSED_KEYS.contains(key);
    }

    /**
     * Returns true if the given mouse button is being pressed.
     * <p>
     *     See {@link InputEventMouseButton} for mouse button constants.
     * </p>
     *
     * @param button The button code.
     * @return True if the button is being pressed, otherwise false.
     */
    public static boolean isMouseButtonPressed(int button) {
        return PRESSED_MOUSE_BUTTONS.contains(button);
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
        var actionState = ACTION_STATES.get(action);
        return actionState == null ? 0.0f : actionState.strength;
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
    public static Vector2 getVector(String up, String down, String left, String right) {
        var vector = new Vector2(getAxis(right, left), getAxis(up, down));
        if(vector.lengthSquared() > 1.0f) {
            return vector.normalized();
        }
        return vector;
    }

    private static class ActionState {

        private long pressed = -1L;
        private long released = -1L;
        private float strength = 0.0f;
    }
}