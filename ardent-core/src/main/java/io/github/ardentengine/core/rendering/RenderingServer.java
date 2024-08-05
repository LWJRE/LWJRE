package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.scene.PointLight3D;
import io.github.scalamath.colorlib.Color;

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
     * Sets the default clear color to use the next time a new frame is rendered.
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
     * Sets the default clear color to use the next time a new frame is rendered.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     */
    public static void setDefaultClearColor(float red, float green, float blue) {
        setDefaultClearColor(red, green, blue, 1.0f);
    }

    /**
     * Sets the default clear color to use the next time a new frame is rendered.
     *
     * @param color The color to use.
     */
    public static void setDefaultClearColor(Color color) {
        setDefaultClearColor(color.r(), color.g(), color.b(), color.a());
    }

    public static void draw(Mesh mesh) {
        getApi().draw(mesh);
    }

    /**
     * Tells the rendering api to update the given mesh.
     * <p>
     *     Implementations of the {@link Mesh} class should call this method every time the mesh is modified.
     * </p>
     * <p>
     *     The mesh might not be updated immediately.
     *     The rendering api might defer the update until the end of the rendering frame or until the mesh is drawn again.
     * </p>
     *
     * @param mesh The mesh to update.
     */
    public static void update(Mesh mesh) {
        getApi().update(mesh);
    }

    /**
     * Tells the rendering api to update the given texture.
     * <p>
     *     Implementations of the {@link Texture} class should call this method every time the texture is modified.
     * </p>
     * <p>
     *     The texture might not be updated immediately.
     *     The rendering might may defer the update until the end of the rendering frame or until the texture is used again.
     * </p>
     *
     * @param texture The texture to update.
     */
    public static void update(Texture texture) {
        getApi().update(texture);
    }

    /**
     * Returns the {@code ShaderData} corresponding to the given shader or creates a new one.
     * Used to abstract the low-level implementation of shaders across different rendering APIs.
     *
     * @param shader The shader object.
     * @return The shader data corresponding to the given shader.
     */
    public static ShaderData getShaderData(Shader shader) {
        return getApi().getShaderData(shader);
    }

    public static void updateLight(PointLight3D light) {
        getApi().updateLight(light);
    }
}