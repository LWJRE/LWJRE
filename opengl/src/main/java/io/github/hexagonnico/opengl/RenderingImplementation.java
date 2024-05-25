package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.RenderingApi;
import org.lwjgl.opengl.GL11;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class RenderingImplementation implements RenderingApi {

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
