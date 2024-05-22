package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.EngineSystem;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class RenderingSystem implements EngineSystem {

    @Override
    public void initialize() {
        GL.createCapabilities();
        GL11.glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
    }

    @Override
    public void process() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void terminate() {

    }

    @Override
    public int priority() {
        return 2;
    }
}
