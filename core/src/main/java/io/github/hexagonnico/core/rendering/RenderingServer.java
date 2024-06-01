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
     * </p>
     *
     * @param mesh The created mesh.
     * @return The mesh data to use internally for the given mesh.
     */
    public static MeshData createMesh(Mesh mesh) {
        return getApi().createMesh(mesh);
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
    public static ShaderData createShader(Shader shader) {
        return getApi().createShader(shader);
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
    public static TextureData createTexture(Texture texture) {
        return getApi().createTexture(texture);
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
    public static void render(Mesh mesh, Shader shader) {
        mesh.onRender();
        getApi().render(mesh, shader);
    }
}
