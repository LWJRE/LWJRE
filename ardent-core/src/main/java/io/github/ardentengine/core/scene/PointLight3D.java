package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.RenderingServer;
import io.github.scalamath.colorlib.Col3f;

public class PointLight3D extends Node3D {

    public Col3f color = new Col3f(1.0f, 1.0f, 1.0f);

    @Override
    protected void onUpdate(float delta) {
        RenderingServer.updateLight(this);
    }
}