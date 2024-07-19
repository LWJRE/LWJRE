package io.github.ardentengine.core.rendering;

import java.nio.ByteBuffer;

/**
 * A texture based on an image.
 * This type of texture can be loaded from an image file.
 */
public class ImageTexture extends Texture {

    /** Width of the texture */
    private int width = 0;
    /** Height of the texture */
    private int height = 0;

    /**
     * Sets the pixels of this texture.
     *
     * @param pixels A ByteBuffer containing data about the pixels.
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public final void setImage(ByteBuffer pixels, int width, int height) {
        this.textureData.setImage(pixels, width, height);
        this.width = width;
        this.height = height;
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