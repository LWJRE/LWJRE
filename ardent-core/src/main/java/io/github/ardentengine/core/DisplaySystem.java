package io.github.ardentengine.core;

import io.github.scalamath.vecmatlib.Vec2f;
import io.github.scalamath.vecmatlib.Vec2i;

import java.util.ServiceLoader;

/**
 * An abstract representation of the display api.
 * The {@code DisplaySystem} handles everything related to window management.
 * <p>
 *     The {@code DisplaySystem} class is a singleton.
 *     The current display system will be loaded the first time the {@link DisplaySystem#getInstance()} method is called.
 * </p>
 * <p>
 *     If there are multiple implementations of the display system, only the first loaded one will be used and a warning will be logged.
 *     If there are no implementations of the display system, a default implementation that returns dummy values will be used.
 * </p>
 * <p>
 *     Dependencies that provide an implementation of the {@code DisplaySystem} must also provide an implementation of {@link DisplaySystemProvider}.
 * </p>
 */
public abstract class DisplaySystem extends EngineSystem {

    /** Singleton instance. */
    private static DisplaySystem instance;

    /**
     * Returns the singleton instance.
     * <p>
     *     Logs a warning if there are multiple implementations of the {@link DisplaySystem}.
     *     Returns a default implementation if there are none.
     * </p>
     *
     * @return The {@code DisplaySystem} singleton instance.
     */
    public static synchronized DisplaySystem getInstance() {
        if(instance == null) {
            instance = ServiceLoader.load(DisplaySystemProvider.class)
                .findFirst() // TODO: Log a warning if there are more than one
                .map(DisplaySystemProvider::getDisplaySystem)
                .orElse(null); // TODO: Default implementation
        }
        return instance;
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

    // TODO: Get window position

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

    @Override
    protected int priority() {
        return 0;
    }
}