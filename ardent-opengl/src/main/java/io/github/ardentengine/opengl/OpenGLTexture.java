package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.TextureData;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * OpenGL implementation of a texture.
 */
public class OpenGLTexture extends TextureData {

    /** Texture object. */
    private final int id;

    /**
     * Creates an OpenGL texture.
     *
     * @see GL11#glGenTextures()
     */
    public OpenGLTexture() {
        this.id = GL11.glGenTextures();
    }

    @Override
    public void setImage(ByteBuffer pixels, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // TODO: Texture properties
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
    }

    @Override
    public void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }

    /**
     * Deletes this texture.
     *
     * @see GL11#glDeleteTextures(int)
     */
    public void delete() {
        GL11.glDeleteTextures(this.id);
    }
}
