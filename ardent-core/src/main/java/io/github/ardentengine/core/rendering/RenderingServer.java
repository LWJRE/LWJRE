package io.github.ardentengine.core.rendering;

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

    /**
     * Sets the default clear color to use the next time {@link RenderingServer#clearScreen()} is called.
     *
     * @param color The color to use.
     */
    public static void setDefaultClearColor(Color color) {
        setDefaultClearColor(color.r(), color.g(), color.b(), color.a());
    }

    /**
     * Clears the screen.
     * Used to clear the screen before rendering a new frame.
     */
    public static void clearScreen() {
        getApi().clearScreen();
    }

    /**
     * Returns the {@code MeshData} corresponding to the given mesh or creates a new one.
     * Used to abstract the low-level implementation of meshes across different rendering APIs.
     *
     * @param mesh The mesh object.
     * @return The mesh data corresponding to the given mesh.
     */
    public static MeshData getMeshData(Mesh mesh) {
        return getApi().getMeshData(mesh);
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

    /**
     * Returns the {@code TextureData} corresponding to the given texture or creates a new one.
     * Used to abstract the low-level implementation of textures across different rendering APIs.
     *
     * @param texture The texture object.
     * @return The texture data corresponding to the given texture.
     */
    public static TextureData getTextureData(Texture texture) {
        return getApi().getTextureData(texture);
    }
}