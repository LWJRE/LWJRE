package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.math.Matrix3;
import io.github.ardentengine.core.math.Matrix4;
import io.github.ardentengine.core.math.Vector2;
import io.github.ardentengine.core.rendering.RenderingServer;

public class Camera2D extends Node2D {

    /**
     * The camera's relative offset.
     * Useful for looking around or camera shake animations
     */
    public Vector2 offset = Vector2.ZERO;
    /**
     * The camera's zoom.
     * <p>
     *     A zoom of {@code (2, 2)} doubles the size seen in the viewport.
     *     A zoom of {@code (0.5, 0.5)} halves the size seen in the viewport.
     * </p>
     */
    public Vector2 zoom = Vector2.ONE;

    /** True if the camera is enabled, otherwise false. */
    private boolean enabled = true;

    // TODO: Limits, drag, smoothing

    @Override
    void update(float delta) {
        RenderingServer.getInstance().setCamera(this);
        super.update(delta);
    }

    public final Matrix4 projectionMatrix() {
        // TODO: Should the projection matrix be related to the camera?
        var windowSize = DisplayServer.getInstance().getWindowSize();
        return Matrix4.orthographicProjection(windowSize.x(), windowSize.aspect(), -4096.0f, 4096.0f);
    }

    public final Matrix3 viewMatrix() {
        var pos = this.globalPosition();
        var cos = (float) Math.cos(this.rotation());
        var sin = (float) Math.sin(this.rotation());
        return new Matrix3(
            cos * this.zoom.x(), -sin * this.zoom.y(), -pos.x() - this.offset.x(),
            sin * this.zoom.x(), cos * this.zoom.y(), -pos.y() - this.offset.y(),
            0.0f, 0.0f, 1.0f
        );
    }

    // FIXME: Reimplement the ability to switch between cameras
}