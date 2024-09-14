package io.github.ardentengine.core.input;

/**
 * Interface for input events affected by modifiers.
 */
public interface InputEventWithModifiers extends InputEvent {

    /** Shift key modifier. */
    int MOD_SHIFT = 0x1;
    /** Control key modifier. */
    int MOD_CONTROL = 0x2;
    /** Alt key modifier. */
    int MOD_ALT = 0x4;
    /** Meta key modifier. Corresponds to the Windows key on Windows, to the Command key on macOS, and to the meta/super key on Linux. */
    int MOD_META = 0x8;

    /**
     * Getter method for the modifiers bitmask.
     *
     * @return The modifiers bitmask.
     */
    int modifiers();

    /**
     * Returns the state of the {@code shift} modifier.
     *
     * @return The state of the {@code shift} modifier.
     */
    default boolean shiftPressed() {
        return (this.modifiers() & MOD_SHIFT) != 0;
    }

    /**
     * Returns the state of the {@code ctrl} modifier.
     *
     * @return The state of the {@code ctrl} modifier.
     */
    default boolean ctrlPressed() {
        return (this.modifiers() & MOD_CONTROL) != 0;
    }

    /**
     * Returns the state of the {@code alt} modifier.
     *
     * @return The state of the {@code alt} modifier.
     */
    default boolean altPressed() {
        return (this.modifiers() & MOD_ALT) != 0;
    }

    /**
     * Returns the state of the {@code meta} modifier.
     * Corresponds to the Windows key on Windows and Linux (also called {@code meta} or {@code super} on Linux) and to the command key on macOS.
     *
     * @return The state of the {@code meta} modifier.
     */
    default boolean metaPressed() {
        return (this.modifiers() & MOD_META) != 0;
    }
}