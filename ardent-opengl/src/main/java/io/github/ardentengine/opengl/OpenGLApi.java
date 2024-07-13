package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.MeshData;
import io.github.ardentengine.core.rendering.RenderingApi;
import io.github.ardentengine.core.rendering.ShaderData;
import io.github.ardentengine.core.rendering.TextureData;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class OpenGLApi implements RenderingApi {

    // TODO: Move these to their own class
    /** Keeps track of meshes for them to be deleted when the rendering system is terminated. */
    private static final ArrayList<OpenGLMesh> MESHES = new ArrayList<>();
    /** Keeps track of textures for them to be deleted when the rendering system is terminated. */
    private static final ArrayList<OpenGLTexture> TEXTURES = new ArrayList<>();

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public MeshData createMesh() {
        var mesh = new OpenGLMesh();
        MESHES.add(mesh);
        return mesh;
    }

    @Override
    public ShaderData createShader() {
        return new OpenGLShader();
    }

    @Override
    public TextureData createTexture() {
        var texture = new OpenGLTexture();
        TEXTURES.add(texture);
        return texture;
    }

    /**
     * Deletes all OpenGL resources.
     * Must be called when the {@link RenderingSystem} is terminated.
     */
    public static void deleteResources() {
        for(var mesh : MESHES) {
            mesh.delete();
        }
        for(var texture : TEXTURES) {
            texture.delete();
        }
    }
}
