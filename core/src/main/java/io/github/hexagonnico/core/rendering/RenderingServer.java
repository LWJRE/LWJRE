package io.github.hexagonnico.core.rendering;

import java.util.ServiceLoader;

/**
 * Static class that allows access to the current {@link RenderingApi}.
 * <p>
 *     This class attempts to load the API when any of its methods are called the first time.
 *     If no API could be loaded the first time, the methods in this class won't have any effect.
 *     Only one rendering API may be used at once.
 * </p>
 */
public final class RenderingServer {

    /**
     * The current rendering API.
     * Loaded when {@link RenderingServer#getApi()} is called for the first time.
     */
    private static RenderingApi api;

    /**
     * Private method that loads the {@link RenderingApi} the first time it is called.
     * If no rendering API could be loaded, {@link RenderingServer#api} is set to a default implementation.
     *
     * @return The current rendering API.
     */
    private static RenderingApi getApi() {
        if(api == null) {
            api = ServiceLoader.load(RenderingApi.class).findFirst().orElse(new RenderingApi() {});
            // TODO: Log a warning if there are more than one
        }
        return api;
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
        getApi().setDefaultClearColor(red, green, blue, alpha);
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

    // TODO: Add overloaded methods

    /**
     * Clears the screen.
     * Used to clear the screen before rendering a new frame.
     */
    public static void clearScreen() {
        getApi().clearScreen();
    }

    /**
     * Creates a {@link MeshData}, an interface used internally in the {@link Mesh} class to abstract the representation of a mesh in different rendering APIs.
     * <p>
     *     If no {@link RenderingApi} could be loaded, the default implementation returns an empty {@link MeshData}.
     * </p>
     * <p>
     *     This method is used internally by the engine.
     *     Users should use the {@link Mesh} class instead.
     * </p>
     *
     * @return An instance of {@link MeshData}.
     */
    public static MeshData createMesh() {
        return getApi().createMesh();
    }
}
