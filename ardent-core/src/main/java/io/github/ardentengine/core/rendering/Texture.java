package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.resources.ResourceManager;
import io.github.ardentengine.core.RenderingSystem;
import io.github.scalamath.vecmatlib.Vec2i;

import java.nio.ByteBuffer;

/**
 * Base class for all types of texture.
 * <p>
 *     TODO: Explain where Textures can be used.
 * </p>
 * <p>
 *     The current rendering api is responsible for loading the texture in the video hardware before it is used in a {@link Shader}.
 *     Implementations of the {@code Texture} class contain the relevant data that needs to be used for creating or updating the texture.
 * </p>
 * <p>
 *     Textures are usually created by loading an image file.
 *     See {@link ImageTexture}.
 * </p>
 */
public abstract class Texture {

    /**
     * Utility method to get a texture resource using {@link ResourceManager#getOrLoad(String)}.
     * <p>
     *     Loads the texture at the given path or returns the same instance if it was already loaded.
     * </p>
     * <p>
     *     Returns null and logs an error if the resource at the given path is not of class {@code Texture}.
     * </p>
     * @param resourcePath Path at which to load the texture resource. Must point to an image file or a {@code .yaml} resource file in the classpath.
     * @return The requested texture.
     */
    public static Texture getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Texture) {
            return (Texture) resource;
        } else if(resource != null) {
            Logger.error("Resource " + resourcePath + " is not a texture");
        }
        return null;
    }

    /**
     * Returns a byte buffer containing the pixels of this texture.
     * <p>
     *     The rendering api may call this method when a texture update is requested using {@link RenderingSystem#update(Texture)}.
     *     Implementations of the {@code Texture} class may use this method to create the texture and therefore it should only be called when the texture needs to be created or updated to avoid overhead.
     * </p>
     * <p>
     *     The {@link ByteBuffer#flip()} method must be called before the buffer is returned.
     * </p>
     *
     * @return A byte buffer containing the pixels of this texture.
     */
    public abstract ByteBuffer getPixels();

    /**
     * Returns the width of this texture.
     *
     * @return The width of this texture.
     */
    public abstract int getWidth();

    /**
     * Returns the height of this texture.
     *
     * @return The height of this texture.
     */
    public abstract int getHeight();

    /**
     * Returns the size of this texture.
     * Combines the results of {@link Texture#getWidth()} and {@link Texture#getHeight()} into a {@code Vec2i}.
     *
     * @return The size of this texture.
     */
    public final Vec2i getSize() {
        return new Vec2i(this.getWidth(), this.getHeight());
    }
}