package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.rendering.TextureData;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;

public class OpenGLTexture extends TextureData {

    private final int id;

    public OpenGLTexture() {
        this.id = GL15.glGenTextures();
    }

    @Override
    public void setPixels(ByteBuffer pixels, int width, int height) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR); // TODO: Texture properties
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    }

    public void delete() {
        GL11.glDeleteTextures(this.id);
    }
}
