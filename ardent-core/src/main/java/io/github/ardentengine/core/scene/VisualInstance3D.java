package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Shader;

import java.util.Map;

/**
 * Base class for all visual 3D nodes.
 */
public abstract class VisualInstance3D extends Node3D {

    public Shader shader;

    // TODO: Add properties common to all VisualInstance3Ds

    /**
     * True if this visual instance is visible.
     * The visual instance won't be rendered if {@code visible} is set to false.
     */
    public boolean visible = true;

    public abstract String shaderType();

    public abstract Map<String, Object> getShaderParameters();
}