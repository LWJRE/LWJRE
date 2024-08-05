package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.scene.PointLight3D;

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
     * Sets the default clear color to use the next time a new frame is rendered.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @param alpha The alpha component of the color.
     */
    default void setDefaultClearColor(float red, float green, float blue, float alpha) {

    }

    default void draw(Mesh mesh) {

    }

    /**
     * Tells the rendering api to update the given mesh.
     * <p>
     *     The mesh might not be updated immediately.
     *     The rendering api might defer the update until the end of the rendering frame or until the mesh is drawn again.
     * </p>
     *
     * @param mesh The mesh to update.
     */
    default void update(Mesh mesh) {

    }

    /**
     * Tells the rendering api to update the given texture.
     * <p>
     *     The texture might not be updated immediately.
     *     The rendering might may defer the update until the end of the rendering frame or until the texture is used again.
     * </p>
     *
     * @param texture The texture to update.
     */
    default void update(Texture texture) {

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

    default void updateLight(PointLight3D light) {

    }
}