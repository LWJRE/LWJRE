package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.rendering.MeshData;
import io.github.hexagonnico.core.rendering.RenderingApi;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class RendererOpenGL implements RenderingApi {

    private static final ArrayList<MeshOpenGL> MESHES = new ArrayList<>();

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
        MeshOpenGL mesh = new MeshOpenGL();
        MESHES.add(mesh);
        return mesh;
    }

    public static void deleteResources() {
        for(MeshOpenGL mesh : MESHES) {
            mesh.delete();
        }
    }
}
