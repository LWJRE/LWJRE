package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Texture;
import io.github.ardentengine.core.rendering.TextureData;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * OpenGL implementation of a texture.
 */
public class OpenGLTexture extends TextureData {

    /** Keeps track of created textures for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Texture, OpenGLTexture> TEXTURES = new HashMap<>();

    /**
     * Returns the texture data corresponding to the given texture or returns a new one if it does not exist.
     *
     * @param texture Texture object.
     * @return The corresponding texture data.
     */
    public static OpenGLTexture getOrCreate(Texture texture) {
        return TEXTURES.computeIfAbsent(texture, key -> new OpenGLTexture());
    }

    /** Texture object. */
    private final int id;

    /**
     * Creates an OpenGL texture.
     *
     * @see GL11#glGenTextures()
     */
    private OpenGLTexture() {
        this.id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // TODO: Texture properties
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    }

    @Override
    public void setImage(ByteBuffer pixels, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
    }

    /**
     * Binds this texture.
     * Called before using the texture in the shader.
     *
     * @see GL11#glBindTexture(int, int)
     */
    public void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }

    /**
     * Deletes this texture.
     *
     * @see GL11#glDeleteTextures(int)
     */
    private void delete() {
        GL11.glDeleteTextures(this.id);
    }

    /**
     * Deletes all textures that were created.
     * Called when the {@link RenderingSystem} is terminated.
     */
    public static void deleteTextures() {
        for(var texture : TEXTURES.values()) {
            texture.delete();
        }
    }
}