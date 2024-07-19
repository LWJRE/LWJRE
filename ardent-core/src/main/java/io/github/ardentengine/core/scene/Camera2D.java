package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Mat4f;
import io.github.scalamath.vecmatlib.Vec2f;

public class Camera2D extends Node2D {

    private static final Mat3f IDENTITY_VIEW = new Mat3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f);

    private static Camera2D current = null;

    public static Camera2D current() {
        return current;
    }

    public static Mat3f currentView() {
        if(current() != null) {
            return current().viewMatrix();
        }
        return IDENTITY_VIEW;
    }

    public static Mat4f currentProjection() {
        if(current() != null) {
            return current().projectionMatrix();
        }
        var windowSize = DisplayServer.getWindowSize().toFloat(); // TODO: Avoid computing this every frame
        return Mat4f.orthographicProjection(windowSize.x(), windowSize.aspect(), 0.1f, 100.0f);
    }

    public Vec2f offset = Vec2f.Zero();
    public Vec2f zoom = Vec2f.One();
    public boolean enabled = true;

    public float nearPlane = 0.1f;
    public float farPlane = 100.0f;

    // TODO: Limits, drag, smoothing

    @Override
    protected void onEnter() {
        if(this.enabled) {
            current = this;
        }
    }

    public Mat4f projectionMatrix() {
        var windowSize = DisplayServer.getWindowSize().toFloat(); // TODO: Avoid computing this every frame
        return Mat4f.orthographicProjection(windowSize.x(), windowSize.aspect(), this.nearPlane, this.farPlane);
    }

    public Mat3f viewMatrix() {
        var pos = this.globalPosition();
        var cos = (float) Math.cos(this.rotation);
        var sin = (float) Math.sin(this.rotation);
        return new Mat3f(
            cos * this.zoom.x(), -sin * this.zoom.y(), -pos.x() - this.offset.x(),
            sin * this.zoom.x(), cos * this.zoom.y(), -pos.y() - this.offset.y(),
            0.0f, 0.0f, -1.0f
        );
    }
}