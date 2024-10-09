package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.RenderingServer;
import io.github.ardentengine.core.rendering.Shader;

import java.util.Map;

/**
 * Base class for all visual 2D nodes.
 */
public abstract class VisualInstance2D extends Node2D {

    public Shader shader;

    // TODO: Add properties common to all VisualInstance2Ds

    /**
     * True if this visual instance is visible.
     * The visual instance won't be rendered if {@code visible} is set to false.
     */
    public boolean visible = true;

    @Override
    void update(float deltaTime) {
        if(this.visible) {
            // TODO: Check if this thing is inside of the camera's bounding rect
            RenderingServer.getInstance().render(this);
        }
        super.update(deltaTime);
    }

    // TODO: Find a better alternative to this

    public abstract String shaderType();

    public abstract Map<String, Object> getShaderParameters();
}