package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.math.Color;
import io.github.ardentengine.core.math.Matrix2x3;
import io.github.ardentengine.core.math.Matrix3x4;
import io.github.ardentengine.core.math.Vector2;
import io.github.ardentengine.core.scene.Camera2D;
import io.github.ardentengine.core.scene.Camera3D;
import io.github.ardentengine.core.scene.PointLight3D;

import java.util.ServiceLoader;

/**
 * An abstract representation of the rendering api.
 * The {@code RenderingServer} handles everything related to rendering and graphics.
 * <p>
 *     The {@code RenderingServer} class is a singleton.
 *     The current rendering system will be loaded the first time the {@link RenderingServer#getInstance()} method is called.
 *     Classes that extend {@code RenderingServer} are not required to be singletons.
 * </p>
 * <p>
 *     Modules providing an implementation of the rendering server must declare it as a service in the {@code META-INF/services} file.
 * </p>
 * <p>
 *     If there are multiple implementations of the rendering server, only the first loaded one will be used and a warning will be logged.
 *     If there are no implementations of the rendering server, a default implementation that returns dummy values will be used.
 * </p>
 */
public abstract class RenderingServer {

    /** Singleton instance. */
    private static RenderingServer instance;

    /**
     * Returns the singleton instance of the {@link RenderingServer}.
     * The implementation is loaded the first time this method is called.
     * <p>
     *     Logs a warning if there are multiple implementations of the {@code RenderingServer}.
     *     Returns a default implementation if there are none.
     * </p>
     *
     * @return The {@code RenderingServer} singleton instance.
     */
    public static synchronized RenderingServer getInstance() {
        // TODO: Log a warning if there are more than one and add a default implementation
        return instance == null ? instance = ServiceLoader.load(RenderingServer.class).findFirst().orElse(null) : instance;
    }

    // TODO: Change these methods so that they don't require nodes

    /**
     * Sets the default clear color to use when the screen is cleared before rendering a new frame.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     * @param alpha The alpha component of the color.
     */
    public abstract void setDefaultClearColor(float red, float green, float blue, float alpha);

    /**
     * Sets the default clear color to use when the screen is cleared before rendering a new frame.
     *
     * @param red The red component of the color.
     * @param green The green component of the color.
     * @param blue The blue component of the color.
     */
    public void setDefaultClearColor(float red, float green, float blue) {
        this.setDefaultClearColor(red, green, blue, 1.0f);
    }

    /**
     * Sets the default clear color to use when the screen is cleared before rendering a new frame.
     *
     * @param color The color to use.
     */
    public void setDefaultClearColor(Color color) {
        this.setDefaultClearColor(color.r(), color.g(), color.b(), color.a());
    }

    public abstract void setCamera(Camera3D camera);

    public abstract void draw(Mesh mesh, Material materialOverride, Matrix3x4 transform);

    public final void draw(Mesh mesh, Matrix3x4 transform) {
        this.draw(mesh, null, transform);
    }

    public abstract void setCamera(Camera2D camera);

    // TODO: Use two rectangles instead of four vectors

    public abstract void draw(Texture texture, Material material, Vector2 vertexOffset, Vector2 vertexScale, Vector2 uvOffset, Vector2 uvScale, Matrix2x3 transform);

    // TODO: Add overloaded methods

    /**
     * Requests the rendering api to update the given mesh.
     * This method must be called by implementations of the {@code Mesh} class when the mesh is modified.
     * <p>
     *     The rendering api may defer the update until the next frame or until the mesh is drawn again, therefore it is safe to call this method multiple times per frame.
     * </p>
     *
     * @param mesh The mesh to update.
     */
    public abstract void update(Mesh mesh);

    /**
     * Requests the rendering api to update the given texture.
     * This method must be called by implementations of the {@code Texture} class when the texture is modified.
     * <p>
     *     The rendering api may defer the update until the next frame or until the texture is drawn again, therefore it is safe to call this method multiple times per frame.
     * </p>
     *
     * @param texture The texture to update.
     */
    public abstract void update(Texture texture);

    public abstract void updateLight(PointLight3D light);
}