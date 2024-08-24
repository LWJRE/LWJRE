package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.RenderingSystem;

import java.nio.ByteBuffer;

/**
 * A texture based on an image.
 * This type of texture can be loaded from an image file.
 */
public class ImageTexture extends Texture {

    /** Pixels buffer. */
    private ByteBuffer pixels;
    /** Width of the texture */
    private int width = 0;
    /** Height of the texture */
    private int height = 0;

    /**
     * Sets the pixels of this texture.
     *
     * @param pixels A buffer containing data about the pixels. The {@link ByteBuffer#flip()} method must be called on the buffer before passing it to this method.
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public final void setImage(ByteBuffer pixels, int width, int height) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        RenderingSystem.getInstance().update(this);
    }

    @Override
    public ByteBuffer getPixels() {
        return this.pixels;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}