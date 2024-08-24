package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.DisplaySystem;
import io.github.ardentengine.core.RenderingSystem;
import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Mat4f;
import io.github.scalamath.vecmatlib.Vec2f;

public class Camera2D extends Node2D {

    /**
     * The camera's relative offset.
     * Useful for looking around or camera shake animations
     */
    public Vec2f offset = Vec2f.Zero();
    /**
     * The camera's zoom.
     * <p>
     *     A zoom of {@code (2, 2)} doubles the size seen in the viewport.
     *     A zoom of {@code (0.5, 0.5)} halves the size seen in the viewport.
     * </p>
     */
    public Vec2f zoom = Vec2f.One();

    /** True if the camera is enabled, otherwise false. */
    private boolean enabled = true;

    // TODO: Limits, drag, smoothing

    @Override
    public void onUpdate(float delta) {
        RenderingSystem.getInstance().setCamera(this);
    }

    public final Mat4f projectionMatrix() {
        // TODO: Should the projection matrix be related to the camera?
        var windowSize = DisplaySystem.getInstance().getWindowSize().toFloat();
        return Mat4f.orthographicProjection(windowSize.x(), windowSize.aspect(), -4096.0f, 4096.0f);
    }

    public final Mat3f viewMatrix() {
        var pos = this.globalPosition();
        var cos = (float) Math.cos(this.rotation);
        var sin = (float) Math.sin(this.rotation);
        return new Mat3f(
            cos * this.zoom.x(), -sin * this.zoom.y(), -pos.x() - this.offset.x(),
            sin * this.zoom.x(), cos * this.zoom.y(), -pos.y() - this.offset.y(),
            0.0f, 0.0f, 1.0f
        );
    }

    // FIXME: Reimplement the ability to switch between cameras
}