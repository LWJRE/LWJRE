package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.EngineSystem;
import io.github.hexagonnico.core.rendering.RenderingServer;
import org.lwjgl.opengl.GL;

/**
 * Rendering system used for OpenGL.
 */
public class RenderingSystem implements EngineSystem {

    @Override
    public void initialize() {
        GL.createCapabilities();
        RenderingServer.setDefaultClearColor(0.5f, 0.5f, 1.0f);
    }

    @Override
    public void process() {
        RenderingServer.clearScreen();
    }

    @Override
    public void terminate() {
        OpenGLApi.deleteResources();
    }
}
