package io.github.hexagonnico.core.rendering;

/**
 * Base texture class.
 * Other classes may extend this one to provide different ways of updating the texture.
 */
public abstract class Texture {

    /**
     * Texture data used internally to abstract the representation of a texture across different rendering APIs.
     */
    protected final TextureData textureData = RenderingServer.createTexture(this);

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
}
