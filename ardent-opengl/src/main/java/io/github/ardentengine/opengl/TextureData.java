package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Texture;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

/**
 * OpenGL implementation of a texture.
 */
public class TextureData {

    /** Keeps track of created textures for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Texture, TextureData> TEXTURES = new HashMap<>();

    /**
     * Returns the texture data corresponding to the given texture or creates a new one if it does not exist.
     * <p>
     *     This method also updates the texture data if it has requested to be updated with {@link TextureData#requestUpdate(Texture)}.
     * </p>
     *
     * @param texture Texture object.
     * @return The corresponding texture data.
     */
    public static TextureData getOrCreate(Texture texture) {
        var textureData = TEXTURES.computeIfAbsent(texture, TextureData::new);
        if(textureData.dirty) {
            textureData.updateTexture(texture);
            textureData.dirty = false;
        }
        return textureData;
    }

    /**
     * Requests the given texture to be updated.
     * <p>
     *     The texture data corresponding to the given texture will be updated the next time {@link TextureData#getOrCreate(Texture)} is called.
     * </p>
     *
     * @param texture Texture object.
     */
    public static void requestUpdate(Texture texture) {
        var textureData = TEXTURES.get(texture);
        if(textureData != null) {
            textureData.dirty = true;
        }
    }

    /** Texture object. */
    private final int id;

    /** Set to true from {@link TextureData#requestUpdate(Texture)} when the texture must be updated. */
    private boolean dirty = false;

    /**
     * Creates the texture data for the given texture.
     *
     * @param texture Texture object.
     */
    private TextureData(Texture texture) {
        this.id = GL11.glGenTextures();
        this.updateTexture(texture);
    }

    /**
     * Updates this texture.
     *
     * @param texture Texture object.
     */
    private void updateTexture(Texture texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // TODO: Texture properties
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        // TODO: Add support for more image formats
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texture.getWidth(), texture.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texture.getPixels());
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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