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
     * Tells the rendering API that a mesh has been created.
     * The returned {@link MeshData} is used internally in the {@link Mesh} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link MeshData}.
     * </p>
     *
     * @param mesh The created mesh.
     * @return The mesh data to use internally for the given mesh.
     */
    default MeshData createMesh(Mesh mesh) {
        return new MeshData();
    }

    /**
     * Tells the rendering API that a shader has been created.
     * The returned {@link ShaderData} is used internally in the {@link Shader} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link ShaderData}.
     * </p>
     *
     * @param shader The created shader.
     * @return The shader data to use internally for the given shader.
     */
    default ShaderData createShader(Shader shader) {
        return new ShaderData();
    }

    /**
     * Tells the rendering API that a texture has been created.
     * The returned {@link TextureData} is used internally in the {@link Texture} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link TextureData}.
     * </p>
     *
     * @param texture The created texture.
     * @return The texture data to use internally for the given texture.
     */
    default TextureData createTexture(Texture texture) {
        return new TextureData();
    }

    /**
     * Renders the given mesh using the given shader.
     * <p>
     *     Rendering may not happen immediately.
     *     The rendering API may render meshes in batch to speed up the process.
     * </p>
     *
     * @param mesh The mesh to render.
     * @param shader The shader to use.
     */
    default void render(Mesh mesh, Shader shader) {

    }
}
