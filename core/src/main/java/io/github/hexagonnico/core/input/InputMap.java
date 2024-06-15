package io.github.hexagonnico.core.input;

import java.util.*;

/**
 * Static class that manages input actions.
 */
public final class InputMap {

    /**
     * The input map.
     */
    private static final HashMap<String, Set<InputEvent>> INPUT_MAP = new HashMap<>();

    static {
        // TODO: Load from file
        addActionEvent("up", new InputEventKey(Keyboard.KEY_W, 0, true, false));
        addActionEvent("down", new InputEventKey(Keyboard.KEY_S, 0, true, false));
        addActionEvent("left", new InputEventKey(Keyboard.KEY_A, 0, true, false));
        addActionEvent("right", new InputEventKey(Keyboard.KEY_D, 0, true, false));
    }

    /**
     * Adds an input event to an action.
     * The {@link InputEvent#isAction(String)} method will return true if that event matches the one that was passed here.
     *
     * @param action Name of the action.
     * @param event The input event to add to the given action.
     * @return True if the given input event was not already mapped to the given action.
     */
    public static boolean addActionEvent(String action, InputEvent event) {
        var actionEvents = INPUT_MAP.computeIfAbsent(action, key -> new HashSet<>());
        return actionEvents.add(event);
    }

    /**
     * Adds the given inputs events to the given action.
     * The {@link InputEvent#isAction(String)} method will return true if that event matches any of the ones that were passed here.
     *
     * @param action Name of the action.
     * @param events Collection of events to add.
     * @return True if any of the given events was not already mapped to the given action.
     */
    public static boolean addActionEvents(String action, Collection<? extends InputEvent> events) {
        var actionEvents = INPUT_MAP.computeIfAbsent(action, key -> new HashSet<>());
        return actionEvents.addAll(events);
    }

    /**
     * Removes all input events from the specified action.
     *
     * @param action Name of the action to be removed from the input map.
     * @return The set of input events that were previously mapped to the given action or an empty set if the given action did not exist.
     */
    public static Set<InputEvent> removeAction(String action) {
        var previous = INPUT_MAP.remove(action);
        return previous != null ? previous : new HashSet<>();
    }

    /**
     * Removes the given input event from the given action.
     *
     * @param action Name of the action from which the event should be removed.
     * @param event The input event to remove.
     * @return True if the given event was previously mapped to the given action, otherwise false.
     */
    public static boolean removeActionEvent(String action, InputEvent event) {
        var actionEvents = INPUT_MAP.get(action);
        if(actionEvents != null) {
            var res = actionEvents.remove(event);
            if(actionEvents.isEmpty()) {
                INPUT_MAP.remove(action);
            }
            return res;
        }
        return false;
    }

    /**
     * Checks if the input map contains an action with the given name.
     *
     * @param action Name of the action.
     * @return True if the input map contains an action with the given name, otherwise false.
     */
    public static boolean actionExists(String action) {
        return INPUT_MAP.containsKey(action);
    }

    /**
     * Checks if the given input event is associated with the given action.
     *
     * @param action Name of the action.
     * @param event The input event.
     * @return True if the given input event is associated with the given action, otherwise false.
     */
    public static boolean actionHasEvent(String action, InputEvent event) {
        var actions = INPUT_MAP.get(action);
        return actions != null && actions.contains(event);
    }

    public static boolean eventIsAction(InputEvent event, String action, boolean exact) {
        var actionEvents = INPUT_MAP.get(action);
        if(actionEvents != null) {
            for(var actionEvent : actionEvents) {
                if(actionEvent.matches(event, exact)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the given event is associated with the given action.
     *
     * @param event The input event.
     * @param action Name of the action.
     * @return True if the given input event matches any of the events mapped to the given action, otherwise false.
     */
    public static boolean eventIsAction(InputEvent event, String action) {
        return eventIsAction(event, action, false);
    }
}
