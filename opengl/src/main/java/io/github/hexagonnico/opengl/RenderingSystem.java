package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.DisplayServer;
import io.github.hexagonnico.core.EngineSystem;
import io.github.hexagonnico.core.rendering.RenderingServer;
import org.lwjgl.Version;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

/**
 * Rendering system used for OpenGL.
 */
public class RenderingSystem implements EngineSystem {

    @Override
    public void initialize() {
        System.out.println("Initializing OpenGL rendering system using LWJGL " + Version.getVersion());
        GL.createCapabilities();
        RenderingServer.setDefaultClearColor(0.3f, 0.3f, 0.3f);
        // TODO: This should only be used for 2D
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_GEQUAL);
        GL11.glClearDepth(0.0);
    }

    @Override
    public void process() {
        // TODO: Abstract this
        var windowSize = DisplayServer.getWindowSize();
        GL11.glViewport(0, 0, windowSize.x(), windowSize.y());
        RenderingServer.clearScreen();
    }

    @Override
    public void terminate() {
        OpenGLApi.deleteResources();
    }
}
