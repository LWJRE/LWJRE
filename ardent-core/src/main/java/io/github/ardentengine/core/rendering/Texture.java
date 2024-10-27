package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.math.Vector2i;

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

    // TODO: Turn this into Texture2D and make it extend from Texture, then create a CanvasTexture that extends Texture to implement normal maps and let the rendering api handle it

    /**
     * Returns a byte buffer containing the pixels of this texture.
     * <p>
     *     The rendering api may call this method when a texture update is requested using {@link RenderingServer#update(Texture)}.
     *     Implementations of the {@code Texture} class may use this method to create the texture and therefore it should only be called when the texture needs to be created or updated to avoid overhead.
     * </p>
     * <p>
     *     The {@link ByteBuffer#flip()} method must be called before the buffer is returned.
     * </p>
     *
     * @return A byte buffer containing the pixels of this texture.
     */
    public abstract ByteBuffer pixels();

    /**
     * Returns the width of this texture.
     *
     * @return The width of this texture.
     */
    public abstract int width();

    /**
     * Returns the height of this texture.
     *
     * @return The height of this texture.
     */
    public abstract int height();

    /**
     * Returns the size of this texture.
     * Combines the results of {@link Texture#width()} and {@link Texture#height()} into a {@code Vector2i}.
     *
     * @return The size of this texture.
     */
    public final Vector2i size() {
        return new Vector2i(this.width(), this.height());
    }
}