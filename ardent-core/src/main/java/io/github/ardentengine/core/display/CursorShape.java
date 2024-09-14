package io.github.ardentengine.core.display;

/**
 * Enum used to change the cursor shape.
 *
 * @see DisplayServer#setCursorShape(CursorShape)
 */
public enum CursorShape {
    /**
     * The regular arrow cursor shape.
     */
    ARROW,
    /**
     * The text input I-beam cursor shape.
     */
    IBEAM,
    /**
     * The crosshair cursor shape.
     */
    CROSSHAIR,
    /**
     * The pointing hand cursor shape.
     */
    POINTING_HAND,
    /**
     * The horizontal resize/ move arrow shape.
     * This is usually a horizontal double-headed arrow.
     */
    RESIZE_EW,
    /**
     * The vertical resize/ move shape.
     * This is usually a vertical double-headed arrow.
     */
    RESIZE_NS,
    /**
     * The top-left to bottom-right diagonal resize/move shape.
     * This is usually a diagonal double-headed arrow.
     */
    RESIZE_NWSE,
    /**
     * The top-right to bottom-left diagonal resize/move shape.
     * This is usually a diagonal double-headed arrow.
     */
    RESIZE_NESW,
    /**
     * The omnidirectional resize/move shape.
     * This is usually either a combined horizontal and vertical double-headed arrow or a grabbing hand.
     */
    RESIZE_ALL,
    /**
     * The operation-not-allowed shape.
     * This is usually a circle with a diagonal line through it.
     */
    NOT_ALLOWED
}