package io.github.ardentengine.opengl;

import io.github.ardentengine.core.EngineSystem;
import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.RenderingServer;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * Rendering system used for OpenGL.
 */
public class RenderingSystem implements EngineSystem {

    @Override
    public void initialize() {
        Logger.info("Initializing OpenGL rendering system using LWJGL " + Version.getVersion());
        GL.createCapabilities();
        RenderingServer.setDefaultClearColor(0.3f, 0.3f, 0.3f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    @Override
    public void process() {
        // TODO: Add viewport scaling options
        var windowSize = DisplayServer.getWindowSize();
        GL11.glViewport(0, 0, windowSize.x(), windowSize.y());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void terminate() {
        OpenGLMesh.deleteMeshes();
        OpenGLShader.deleteShaders();
        OpenGLTexture.deleteTextures();
        UniformBufferObject.deleteBuffers();
    }
}