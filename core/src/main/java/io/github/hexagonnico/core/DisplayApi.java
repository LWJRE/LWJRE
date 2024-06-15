package io.github.hexagonnico.core;

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
     * @param width Window width.
     * @param height Window height.
     */
    default void setWindowSize(int width, int height) {

    }

    default void setWindowPosition(int x, int y) {

    }

    default void showWindow() {

    }

    default void hideWindow() {

    }

    default Vec2i getWindowSize() {
        return Vec2i.Zero();
    }

    default void setCursorPosition(float x, float y) {

    }

    default Vec2f getCursorPosition() {
        return Vec2f.Zero();
    }
}
