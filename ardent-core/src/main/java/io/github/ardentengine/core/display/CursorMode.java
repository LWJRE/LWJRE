package io.github.ardentengine.core.display;

/**
 * Enum used to change the cursor mode.
 *
 * @see DisplayServer#setCursorMode(CursorMode)
 * @see DisplayServer#getCursorMode()
 */
public enum CursorMode {
    /**
     * Makes the cursor visible and behaving normally.
     */
    VISIBLE,
    /**
     * Makes the cursor invisible when it is over the content area of the window but does not restrict the cursor from leaving.
     */
    HIDDEN,
    /**
     * Hides and grabs the cursor, providing virtual and unlimited cursor movement.
     * This is useful for implementing for example 3D camera controls.
     */
    CAPTURED,
    /**
     * Makes the cursor visible and confines it to the content area of the window.
     */
    CONFINED;
}