package io.github.hexagonnico.core;

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
     * The current display API. Can be null.
     * Use {@link DisplayServer#loadIfNotLoaded()} before accessing.
     */
    private static DisplayApi api;
    /**
     * Keeps track of whether this class has attempted to load the API yet.
     */
    private static boolean loaded = false;

    /**
     * Attempts to load the API using a {@link ServiceLoader}.
     * <p>
     *     The API is only loaded the first time this method is called.
     *     If the loading is failed, {@link DisplayServer#api} will remain null and this method will return false.
     * </p>
     *
     * @return True if the API has been loaded and {@link DisplayServer#api} is not null, otherwise false.
     */
    private static boolean loadIfNotLoaded() {
        if(!loaded) {
            api = ServiceLoader.load(DisplayApi.class).findFirst().orElse(null);
            loaded = true;
        }
        return api != null;
    }

    /**
     * Sets the title of the main window.
     *
     * @param title The title.
     */
    public static void setWindowTitle(String title) {
        if(loadIfNotLoaded()) {
            api.setWindowTitle(title);
        }
    }

    /**
     * Sets the size of the main window.
     *
     * @param width Window width.
     * @param height Window height.
     */
    public static void setWindowSize(int width, int height) {
        if(loadIfNotLoaded()) {
            api.setWindowSize(width, height);
        }
    }
}
