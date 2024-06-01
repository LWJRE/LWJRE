package io.github.hexagonnico.core.rendering;

import java.nio.ByteBuffer;

/**
 * A texture based on an image.
 * This type of texture can be loaded from an image file.
 */
public class ImageTexture extends Texture {

    /**
     * Sets the pixels of this texture.
     *
     * @param pixels A ByteBuffer containing data about the pixels.
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public final void setPixels(ByteBuffer pixels, int width, int height) {
        this.textureData.setPixels(pixels, width, height);
    }
}
