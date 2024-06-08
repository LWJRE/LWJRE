package io.github.hexagonnico.core.scene;

import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Vec2f;

public class Camera2D extends Node2D {

    private static Camera2D current = null;

    public static Camera2D current() {
        return current;
    }

    public Vec2f offset = Vec2f.Zero();
    public Vec2f zoom = Vec2f.One();
    public boolean enabled = true;

    @Override
    protected void onEnter() {
        if(this.enabled) {
            current = this;
        }
    }

    public Mat3f viewMatrix() {
        // TODO: Cache the view matrix until the camera is updated
        var pos = this.globalPosition().negated();
        var cos = (float) Math.cos(this.rotation);
        var sin = (float) Math.sin(this.rotation);
        // TODO: Add zoom and offset
        return new Mat3f(cos, -sin, pos.x(), sin, cos, pos.y(), 0.0f, 0.0f, -1.0f);
    }
}
