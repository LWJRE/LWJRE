package io.github.ardentengine.core.rendering;

import java.nio.ByteBuffer;

/**
 * Class used internally by meshes to abstract the representation of a texture across different rendering APIs.
 * <p>
 *     All the methods in this class are dummy methods.
 *     The rendering API module should extend this class to provide its implementation.
 * </p>
 *
 * @see Texture
 */
public class TextureData {

    /**
     * Sets the texture to the given image data.
     *
     * @param pixels Byte buffer containing the image data.
     * @param width Width of the texture.
     * @param height Height of the texture.
     */
    public void setImage(ByteBuffer pixels, int width, int height) {

    }
}