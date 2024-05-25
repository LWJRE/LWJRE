package io.github.hexagonnico.core;

import java.util.ServiceLoader;

/**
 * Static class that allows access to the current {@link RenderingApi}.
 * <p>
 *     This class attempts to load the API when any of its methods are called the first time.
 *     If no API could be loaded the first time, the methods in this class won't have any effect.
 * </p>
 */
public final class RenderingServer {

    /**
     * The current rendering API. Can be null.
     * Use {@link RenderingServer#loadIfNotLoaded()} before accessing.
     */
    private static RenderingApi api;
    /**
     * Keeps track of whether this class has attempted to load the API yet.
     */
    private static boolean loaded = false;

    /**
     * Attempts to load the API using a {@link ServiceLoader}.
     * <p>
     *     The API is only loaded the first time this method is called.
     *     If the loading is failed, {@link RenderingServer#api} will remain null and this method will return false.
     * </p>
     *
     * @return True if the API has been loaded and {@link RenderingServer#api} is not null, otherwise false.
     */
    private static boolean loadIfNotLoaded() {
        if(!loaded) {
            api = ServiceLoader.load(RenderingApi.class).findFirst().orElse(null);
            loaded = true;
        }
        return api != null;
    }

    /**
     * Sets the default clear color to use the next time {@link RenderingServer#clearScreen()} is called.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @param alpha The alpha component of the color.
     */
    public static void setDefaultClearColor(float red, float green, float blue, float alpha) {
        if(loadIfNotLoaded()) {
            api.setDefaultClearColor(red, green, blue, alpha);
        }
    }

    /**
     * Sets the default clear color to use the next time {@link RenderingServer#clearScreen()} is called.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     */
    public static void setDefaultClearColor(float red, float green, float blue) {
        setDefaultClearColor(red, green, blue, 1.0f);
    }

    /**
     * Clears the screen.
     * Used to clear the screen before rendering a new frame.
     */
    public static void clearScreen() {
        if(loadIfNotLoaded()) {
            api.clearScreen();
        }
    }
}
