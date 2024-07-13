package io.github.ardentengine.core.input;

/**
 * Interface for input events affected by modifiers.
 */
public interface InputEventWithModifiers extends InputEvent {

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
        return (this.modifiers() & Keyboard.MOD_SHIFT) != 0;
    }

    /**
     * Returns the state of the {@code ctrl} modifier.
     *
     * @return The state of the {@code ctrl} modifier.
     */
    default boolean ctrlPressed() {
        return (this.modifiers() & Keyboard.MOD_CONTROL) != 0;
    }

    /**
     * Returns the state of the {@code alt} modifier.
     *
     * @return The state of the {@code alt} modifier.
     */
    default boolean altPressed() {
        return (this.modifiers() & Keyboard.MOD_ALT) != 0;
    }

    /**
     * Returns the state of the {@code meta} modifier.
     * Corresponds to the Windows key on Windows and Linux (also called {@code meta} or {@code super} on Linux) and to the command key on macOS.
     *
     * @return The state of the {@code meta} modifier.
     */
    default boolean metaPressed() {
        return (this.modifiers() & Keyboard.MOD_META) != 0;
    }
}
