package io.github.ardentengine.core.input;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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
        addActionEvent("up", new InputEventKey(InputEventKey.KEY_W, true));
        addActionEvent("down", new InputEventKey(InputEventKey.KEY_S, true));
        addActionEvent("left", new InputEventKey(InputEventKey.KEY_A, true));
        addActionEvent("right", new InputEventKey(InputEventKey.KEY_D, true));
        addActionEvent("jump", new InputEventKey(InputEventKey.KEY_SPACE, true));
        addActionEvent("run", new InputEventKey(InputEventKey.KEY_LEFT_SHIFT, true));
        addActionEvent("run", new InputEventKey(InputEventKey.KEY_RIGHT_SHIFT, true));
        addActionEvent("cut", new InputEventKey(InputEventKey.KEY_X, InputEventKey.MOD_CONTROL, true));
        addActionEvent("copy", new InputEventKey(InputEventKey.KEY_C, InputEventKey.MOD_CONTROL, true));
        addActionEvent("paste", new InputEventKey(InputEventKey.KEY_V, InputEventKey.MOD_CONTROL, true));
        addActionEvent("undo", new InputEventKey(InputEventKey.KEY_Z, InputEventKey.MOD_CONTROL, true));
        addActionEvent("redo", new InputEventKey(InputEventKey.KEY_Z, InputEventKey.MOD_CONTROL | InputEventKey.MOD_SHIFT, true));
    }

    public static Set<String> getActions() {
        return new HashSet<>(INPUT_MAP.keySet());
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
        return INPUT_MAP.computeIfAbsent(action, key -> new HashSet<>()).add(event);
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
     * Adds the given inputs events to the given action.
     * The {@link InputEvent#isAction(String)} method will return true if that event matches any of the ones that were passed here.
     *
     * @param action Name of the action.
     * @param events Collection of events to add.
     * @return True if any of the given events was not already mapped to the given action.
     */
    public static boolean addActionEvents(String action, Collection<? extends InputEvent> events) {
        return INPUT_MAP.computeIfAbsent(action, key -> new HashSet<>()).addAll(events);
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
     * Checks if the input map contains an action with the given name.
     *
     * @param action Name of the action.
     * @return True if the input map contains an action with the given name, otherwise false.
     */
    public static boolean hasAction(String action) {
        return INPUT_MAP.containsKey(action);
    }

    public static Set<InputEvent> getActionEvents(String action) {
        var events = INPUT_MAP.get(action);
        return events == null ? new HashSet<>() : new HashSet<>(events);
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