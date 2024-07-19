package io.github.ardentengine.core.rendering;

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
     * Returns the {@code MeshData} corresponding to the given mesh or creates a new one.
     * Used to abstract the low-level implementation of meshes across different rendering APIs.
     * <p>
     *     The returned object must be an implementation of {@code MeshData} for the current rendering API.
     *     Low-level functions are normally only accessed by the rendering API.
     * </p>
     * <p>
     *     The rendering API must guarantee that there is at most one instance of {@code MeshData} for each instance of {@code Mesh}.
     *     This method must return the same instance if it already exists.
     * </p>
     *
     * @param mesh The mesh object.
     * @return The mesh data object.
     */
    default MeshData getMeshData(Mesh mesh) {
        return new MeshData();
    }

    /**
     * Returns the {@code ShaderData} corresponding to the given shader or creates a new one.
     * Used to abstract the low-level implementation of shaders across different rendering APIs.
     * <p>
     *     The returned object must be an implementation of {@code ShaderData} for the current rendering API.
     *     Low-level functions are normally only accessed by the rendering API.
     * </p>
     * <p>
     *     The rendering API must guarantee that there is at most one instance of {@code ShaderData} for each instance of {@code Shader}.
     *     This method must return the same instance if it already exists.
     * </p>
     *
     * @param shader The shader object.
     * @return The shader data object.
     */
    default ShaderData getShaderData(Shader shader) {
        return new ShaderData();
    }

    /**
     * Returns the {@code TextureData} corresponding to the given texture or creates a new one.
     * Used to abstract the low-level implementation of textures across different rendering APIs.
     * <p>
     *     The returned object must be an implementation of {@code TextureData} for the current rendering API.
     *     Low-level functions are normally only accessed by the rendering API.
     * </p>
     * <p>
     *     The rendering API must guarantee that there is at most one instance of {@code TextureData} for each instance of {@code Texture}.
     *     This method must return the same instance if it already exists.
     * </p>
     *
     * @param texture The texture object.
     * @return The texture data object.
     */
    default TextureData getTextureData(Texture texture) {
        return new TextureData();
    }
}