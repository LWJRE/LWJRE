package io.github.hexagonnico.core;

/**
 * Interface used to abstract the rendering API.
 * <p>
 *     Modules providing rendering APIs must implement this interface and declare its implementation as a service.
 *     Only one rendering API may be used at once.
 * </p>
 * <p>
 *     The rendering API can be access with the {@link RenderingServer} class.
 * </p>
 */
public interface RenderingApi {

    /**
     * Sets the default clear color to use the next time {@link RenderingApi#clearScreen()} is called.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @param alpha The alpha component of the color.
     */
    void setDefaultClearColor(float red, float green, float blue, float alpha);

    /**
     * Clears the screen.
     * Used to clear the screen before rendering a new frame.
     */
    void clearScreen();
}
