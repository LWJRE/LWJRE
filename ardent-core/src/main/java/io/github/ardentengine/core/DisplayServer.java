package io.github.ardentengine.core;

import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

import java.util.ServiceLoader;

/**
 * An abstract representation of the display api.
 * The {@code DisplayServer} handles everything related to window management.
 * <p>
 *     The {@code DisplayServer} class is a singleton.
 *     The current display server will be loaded the first time the {@link DisplayServer#getInstance()} method is called.
 *     Classes that extend {@code DisplayServer} are not required to be singletons.
 * </p>
 * <p>
 *     Modules providing an implementation of the display server must declare it as a service in the {@code META-INF/services} file.
 * </p>
 * <p>
 *     If there are multiple implementations of the display server, only the first loaded one will be used and a warning will be logged.
 *     If there are no implementations of the display server, a default implementation that returns dummy values will be used.
 * </p>
 */
public abstract class DisplayServer {

    /** Singleton instance. */
    private static DisplayServer instance;

    /**
     * Returns the singleton instance of the {@link DisplayServer}.
     * The implementation is loaded the first time this method is called.
     * <p>
     *     Logs a warning if there are multiple implementations of the {@code DisplayServer}.
     *     Returns a default implementation if there are none.
     * </p>
     *
     * @return The {@code DisplayServer} singleton instance.
     */
    public static synchronized DisplayServer getInstance() {
        // TODO: Log a warning if there are more than one and add a default implementation
        return instance == null ? instance = ServiceLoader.load(DisplayServer.class).findFirst().orElse(null) : instance;
    }

    /**
     * Sets the title of the main window.
     *
     * @param title The new title.
     */
    public abstract void setWindowTitle(String title);

    /**
     * Sets the size of the main window.
     *
     * @param width Width of the window in pixels.
     * @param height Height of the window in pixels.
     */
    public abstract void setWindowSize(int width, int height);

    /**
     * Sets the size of the main window.
     *
     * @param size Size of the window in pixels.
     */
    public void setWindowSize(Vec2i size) {
        this.setWindowSize(size.x(), size.y());
    }

    /**
     * Returns the size of the main window in pixels.
     *
     * @return The size of the main window.
     */
    public abstract Vec2i getWindowSize();

    /**
     * Sets the position of the main window.
     *
     * @param x Position of the window in pixels from the left side of the monitor.
     * @param y Position of the window in pixels from the upper side of the monitor.
     */
    public abstract void setWindowPosition(int x, int y);

    /**
     * Sets the position of the main window.
     *
     * @param position Position of the window in pixels from the top-left corner of the monitor.
     */
    public void setWindowPosition(Vec2i position) {
        this.setWindowPosition(position.x(), position.y());
    }

    /**
     * Returns the position of the main window in pixels from the upper-left corner of the monitor.
     *
     * @return Position of the window in pixels from the upper-left corner of the monitor.
     */
    public abstract Vec2i getWindowPosition();

    /**
     * Shows the main window if it is hidden.
     */
    public abstract void showWindow();

    /**
     * Hides the main window if it is visible.
     */
    public abstract void hideWindow();

    /**
     * Sets the position of the cursor.
     *
     * @param x The position of the cursor in pixels from the left side of the main window.
     * @param y The position of the cursor in pixels from the upper side of the main window.
     */
    public abstract void setCursorPosition(float x, float y);

    /**
     * Sets the position of the cursor.
     *
     * @param position The position of the cursor in pixels from the top-left corner of the main window.
     */
    public void setCursorPosition(Vec2f position) {
        this.setCursorPosition(position.x(), position.y());
    }

    /**
     * Returns the position of the cursor in pixels from the top-left corner of the main window.
     *
     * @return The position of the cursor.
     */
    public abstract Vec2f getCursorPosition();
}