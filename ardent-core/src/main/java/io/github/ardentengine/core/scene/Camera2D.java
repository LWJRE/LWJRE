package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.logging.Logger;
import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Mat4f;
import io.github.scalamath.vecmatlib.Vec2f;

/**
 * Camera node for 2D scenes.
 * <p>
 *     A camera makes itself current when it enters the scene tree and if it is enabled and no other camera is current.
 *     Multiple cameras can be in the same scene, but only one of them can be the current one.
 *     Cameras can be set to current with {@link Camera2D#makeCurrent()}.
 * </p>
 */
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
    protected void onEnter() {
        if(this.enabled && this.getSceneTree().getCamera2D() == null) {
            this.getSceneTree().setCamera2D(this);
        }
    }

    @Override
    protected void onExit() {
        if(this.getSceneTree().getCamera2D() == this) {
            this.getSceneTree().setCamera2D(null);
        }
    }

    public final Mat4f projectionMatrix() {
        // TODO: Should the projection matrix be related to the camera?
        var windowSize = DisplayServer.getWindowSize().toFloat();
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

    /**
     * Sets this camera as the current one in this scene.
     * <p>
     *     The camera must be inside the scene tree and must be enabled.
     *     Logs an error if this camera is not enabled or is not inside the scene tree.
     * </p>
     *
     * @see Camera2D#isCurrent()
     * @see Camera2D#isEnabled()
     */
    public final void makeCurrent() {
        if(this.enabled) {
            if(this.isInsideTree()) {
                this.getSceneTree().setCamera2D(this);
            } else {
                Logger.error("Camera " + this + " cannot be made current because it is not inside the scene tree");
            }
        } else {
            Logger.error("Camera " + this + " cannot be made current because it is disabled");
        }
    }

    /**
     * Returns true if this camera is the current one in the current scene.
     * Returns false if this node is not inside the scene tree.
     *
     * @return True if this camera is the current one in the current scene, otherwise false.
     *
     * @see Camera2D#makeCurrent()
     */
    public final boolean isCurrent() {
        return this.isInsideTree() && this.getSceneTree().getCamera2D() == this;
    }

    /**
     * Setter method.
     * Enables or disables this camera.
     * <p>
     *     If this camera is the current one and is disabled, it will be made as non current.
     * </p>
     *
     * @param enabled True to enable the camera, false to disable it.
     *
     * @see Camera2D#isEnabled()
     */
    public final void setEnabled(boolean enabled) {
        if(this.isCurrent() && !enabled) {
            this.getSceneTree().setCamera2D(null);
        }
        this.enabled = enabled;
    }

    /**
     * Getter method.
     * Returns true if this camera is enabled, false if it is disabled.
     *
     * @return True if this camera is enabled, false if it is disabled.
     *
     * @see Camera2D#setEnabled(boolean)
     */
    public final boolean isEnabled() {
        return this.enabled;
    }
}