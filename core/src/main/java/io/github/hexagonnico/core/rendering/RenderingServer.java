package io.github.hexagonnico.core.rendering;

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
     * Tells the rendering API that a mesh has been created.
     * The returned {@link MeshData} is used internally in the {@link Mesh} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link MeshData}.
     *     Different rendering APIs must return their implementation of a mesh.
     * </p>
     *
     * @return Mesh data used internally for meshes.
     */
    public static MeshData createMesh() {
        return getApi().createMesh();
    }

    /**
     * Tells the rendering API that a shader has been created.
     * The returned {@link ShaderData} is used internally in the {@link Shader} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link ShaderData}.
     *     Different rendering APIs must return their implementation of a shader.
     * </p>
     *
     * @return Shader data used internally for shaders.
     */
    public static ShaderData createShader() {
        return getApi().createShader();
    }

    /**
     * Tells the rendering API that a texture has been created.
     * The returned {@link TextureData} is used internally in the {@link Texture} class.
     * <p>
     *     The default implementation of this method returns an instance of {@link TextureData}.
     *     Different rendering APIs must return their implementation of a texture.
     * </p>
     *
     * @return Texture data used internally for textures.
     */
    public static TextureData createTexture() {
        return getApi().createTexture();
    }
}
