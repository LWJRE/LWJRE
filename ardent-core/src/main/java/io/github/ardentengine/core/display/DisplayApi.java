package io.github.ardentengine.core.display;

import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

/**
 * Interface used to abstract the display API.
 * <p>
 *     Modules providing display APIs must implement this interface and declare its implementation as a service.
 *     Only one display API may be used at once.
 * </p>
 * <p>
 *     The display API can be access with the {@link DisplayServer} class.
 * </p>
 */
public interface DisplayApi {

    /**
     * Sets the title of the main window.
     *
     * @param title The title.
     */
    default void setWindowTitle(String title) {

    }

    /**
     * Sets the size of the main window.
     *
     * @param width Width of the window in pixels.
     * @param height Height of the window in pixels.
     */
    default void setWindowSize(int width, int height) {

    }

    /**
     * Sets the position of the main window.
     *
     * @param x Position of the window in pixels from the left side of the monitor.
     * @param y Position of the window in pixels from the upper side of the monitor.
     */
    default void setWindowPosition(int x, int y) {

    }

    /**
     * Shows the main window if it is hidden.
     */
    default void showWindow() {

    }

    /**
     * Hides the main window if it is visible.
     */
    default void hideWindow() {

    }

    /**
     * Returns the size of the main window in pixels.
     *
     * @return The size of the main window.
     */
    default Vec2i getWindowSize() {
        return Vec2i.Zero();
    }

    /**
     * Sets the position of the cursor.
     *
     * @param x The position of the cursor in pixels from the left side of the main window.
     * @param y The position of the cursor in pixels from the upper side of the main window.
     */
    default void setCursorPosition(float x, float y) {

    }

    /**
     * Returns the position of the cursor in pixels from the top-left corner of the main window.
     *
     * @return The position of the cursor.
     */
    default Vec2f getCursorPosition() {
        return Vec2f.Zero();
    }
}