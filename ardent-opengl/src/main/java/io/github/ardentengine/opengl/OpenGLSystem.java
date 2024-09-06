package io.github.ardentengine.opengl;

import io.github.ardentengine.core.DisplayServer;
import io.github.ardentengine.core.EngineSystem;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class OpenGLSystem extends EngineSystem {

    @Override
    protected void initialize() {
        GL.createCapabilities();
        // Set the default clear color
        GL11.glClearColor(0.3f, 0.3f, 0.3f, 1.0f);
        // Enable depth test
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        // Enable back face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    @Override
    protected void process() {
        // TODO: Add viewport scaling options
        var windowSize = DisplayServer.getInstance().getWindowSize();
        GL11.glViewport(0, 0, windowSize.x(), windowSize.y());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        Renderer3D.getInstance().renderingProcess();
        Renderer2D.getInstance().renderingProcess();
    }

    @Override
    protected void terminate() {
        MeshData.deleteMeshes();
        ShaderData.deleteShaders();
        TextureData.deleteTextures();
    }

    @Override
    protected int priority() {
        return 1;
    }
}