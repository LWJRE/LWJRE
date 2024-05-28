package io.github.hexagonnico.core.rendering;

/**
 * Interface used to abstract the rendering API.
 * <p>
 *     Modules providing rendering APIs must implement this interface and declare its implementation as a service.
 *     Only one rendering API may be used at once.
 * </p>
 * <p>
 *     The rendering API can be access with the {@link RenderingServer} class.
 * </p>
 * <p>
 *     All methods in this interface have a default implementation.
 *     This allows new methods to be added without requiring implementations in separate modules to be updated.
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
    default void setDefaultClearColor(float red, float green, float blue, float alpha) {

    }

    /**
     * Clears the screen.
     * Used to clear the screen before rendering a new frame.
     */
    default void clearScreen() {

    }

    /**
     * Creates a {@link MeshData}, an interface used internally in the {@link Mesh} class to abstract the representation of a mesh in different rendering APIs.
     * <p>
     *     The default implementation of this method returns an empty {@link MeshData}.
     *     An implementation of {@link RenderingApi} should provide its own implementation of {@link MeshData} as well.
     * </p>
     *
     * @return An instance of {@link MeshData}.
     */
    default MeshData createMesh() {
        return new MeshData() {};
    }
}
