package io.github.ardentengine.core.display;

import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

import java.util.ServiceLoader;

/**
 * Static class that allows access to the current {@link DisplayApi}.
 * <p>
 *     This class attempts to load the API when any of its methods are called the first time.
 *     If no API could be loaded the first time, the methods in this class won't have any effect.
 * </p>
 */
public final class DisplayServer {

    /**
     * The current display API.
     * Loaded when {@link DisplayServer#getApi()} is called for the first time.
     */
    private static DisplayApi api;

    /**
     * Private method that loads the {@link DisplayApi} the first time it is called.
     * If no rendering API could be loaded, {@link DisplayServer#api} is set to a default implementation.
     *
     * @return The current rendering API.
     */
    private static DisplayApi getApi() {
        if(api == null) {
            api = ServiceLoader.load(DisplayApi.class).findFirst().orElse(new DisplayApi() {});
            // TODO: Log a warning if there are more than one
        }
        return api;
    }

    /**
     * Sets the title of the main window.
     *
     * @param title The title.
     */
    public static void setWindowTitle(String title) {
        getApi().setWindowTitle(title);
    }

    /**
     * Sets the size of the main window.
     *
     * @param width Width of the window in pixels.
     * @param height Height of the window in pixels.
     */
    public static void setWindowSize(int width, int height) {
        getApi().setWindowSize(width, height);
    }

    /**
     * Sets the size of the main window.
     *
     * @param size Size of the main window in pixels.
     */
    public static void setWindowSize(Vec2i size) {
        setWindowSize(size.x(), size.y());
    }

    /**
     * Sets the position of the main window.
     *
     * @param x Position of the window in pixels from the left side of the monitor.
     * @param y Position of the window in pixels from the upper side of the monitor.
     */
    public static void setWindowPosition(int x, int y) {
        getApi().setWindowPosition(x, y);
    }

    /**
     * Sets the position of the main window.
     *
     * @param position Position of the window in pixels from the top-left corner of the monitor.
     */
    public static void setWindowPosition(Vec2i position) {
        setWindowPosition(position.x(), position.y());
    }

    /**
     * Shows the main window if it is hidden.
     */
    public static void showWindow() {
        getApi().showWindow();
    }

    /**
     * Hides the main window if it is visible.
     */
    public static void hideWindow() {
        getApi().hideWindow();
    }

    /**
     * Returns the size of the main window in pixels.
     *
     * @return The size of the main window.
     */
    public static Vec2i getWindowSize() {
        return getApi().getWindowSize();
    }

    /**
     * Sets the position of the cursor.
     *
     * @param x The position of the cursor in pixels from the left side of the main window.
     * @param y The position of the cursor in pixels from the upper side of the main window.
     */
    public static void setCursorPosition(float x, float y) {
        getApi().setCursorPosition(x, y);
    }

    /**
     * Sets the position of the cursor.
     *
     * @param position The position of the cursor in pixels from the top-left corner of the main window.
     */
    public static void setCursorPosition(Vec2i position) {
        setCursorPosition(position.x(), position.y());
    }

    /**
     * Returns the position of the cursor in pixels from the top-left corner of the main window.
     *
     * @return The position of the cursor.
     */
    public static Vec2f getCursorPosition() {
        return getApi().getCursorPosition();
    }
}