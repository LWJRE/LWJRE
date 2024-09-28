package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.math.Color;
import io.github.ardentengine.core.rendering.RenderingServer;

public class PointLight3D extends Node3D {

    public Color color = new Color(1.0f, 1.0f, 1.0f);

    // TODO: Finish 3D lighting

    @Override
    void update(float delta) {
        RenderingServer.getInstance().updateLight(this);
        super.update(delta);
    }
}