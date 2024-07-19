package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.*;
import org.lwjgl.opengl.GL11;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class OpenGLApi implements RenderingApi {

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public MeshData getMeshData(Mesh mesh) {
        return OpenGLMesh.getOrCreate(mesh);
    }

    @Override
    public ShaderData getShaderData(Shader shader) {
        return OpenGLShader.getOrCreate(shader);
    }

    @Override
    public TextureData getTextureData(Texture texture) {
        return OpenGLTexture.getOrCreate(texture);
    }
}