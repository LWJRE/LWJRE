package io.github.ardentengine.core;

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

    private static DisplayApi api;

    private static DisplayApi getApi() {
        if(api == null) {
            api = ServiceLoader.load(DisplayApi.class).findFirst().orElse(new DisplayApi() {});
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
     * @param width Window width.
     * @param height Window height.
     */
    public static void setWindowSize(int width, int height) {
        getApi().setWindowSize(width, height);
    }

    public static void setWindowSize(Vec2i size) {
        setWindowSize(size.x(), size.y());
    }

    public static void setWindowPosition(int x, int y) {
        getApi().setWindowPosition(x, y);
    }

    public static void setWindowPosition(Vec2i position) {
        setWindowPosition(position.x(), position.y());
    }

    public static void showWindow() {
        getApi().showWindow();
    }

    public static void hideWindow() {
        getApi().hideWindow();
    }

    public static Vec2i getWindowSize() {
        return getApi().getWindowSize();
    }
}
