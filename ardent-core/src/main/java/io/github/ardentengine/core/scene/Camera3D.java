package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.logging.Logger;
import io.github.scalamath.vecmatlib.Mat4f;

/**
 * Camera node for 3D scenes.
 * <p>
 *     A camera makes itself current when it enters the scene tree and if it is enabled and no other camera is current.
 *     Multiple cameras can be in the same scene, but only one of them can be the current one.
 *     Cameras can be set to current with {@link Camera3D#makeCurrent()}.
 * </p>
 */
public class Camera3D extends Node3D {

    /** The camera's field of view in radians. */
    public double fov = 7.0 / 18.0 * Math.PI;
    /** Distance to the camera's near plane. */
    public float nearPlane = 0.01f;
    /** Distance to the camera's far plane. Higher values allow the camera to see further away. */
    public float farPlane = 1000.0f;

    /** True if the camera is enabled, otherwise false. */
    private boolean enabled = true;

    @Override
    protected void onEnter() {
        if(this.enabled && this.getSceneTree().getCamera3D() == null) {
            this.getSceneTree().setCamera3D(this);
        }
    }

    @Override
    protected void onExit() {
        if(this.isCurrent()) {
            this.getSceneTree().setCamera3D(null);
        }
    }

    /**
     * Returns the camera's projection matrix.
     * Used in shaders.
     *
     * @return The camera's projection matrix.
     */
    public final Mat4f projectionMatrix() {
        return Mat4f.perspectiveProjection(this.fov, DisplayServer.getWindowSize(), this.nearPlane, this.farPlane);
    }

    /**
     * Returns the camera's view matrix.
     * Used in shaders.
     *
     * @return The camera's view matrix.
     */
    public final Mat4f viewMatrix() {
        return Mat4f.translation(this.globalPosition().negated()).multiply(Mat4f.rotation(this.rotation));
    }

    /**
     * Sets this camera as the current one in this scene.
     * <p>
     *     The camera must be inside the scene tree and must be enabled.
     *     Logs an error if this camera is not enabled or is not inside the scene tree.
     * </p>
     *
     * @see Camera3D#isCurrent()
     * @see Camera3D#isEnabled()
     */
    public final void makeCurrent() {
        if(this.isInsideTree()) {
            this.getSceneTree().setCamera3D(this);
        } else {
            Logger.error("Camera " + this + " cannot be made current because it is not inside the scene tree");
        }
    }

    /**
     * Returns true if this camera is the current one in the current scene.
     * Returns false if this node is not inside the scene tree.
     *
     * @return True if this camera is the current one in the current scene, otherwise false.
     *
     * @see Camera3D#makeCurrent()
     */
    public final boolean isCurrent() {
        return this.isInsideTree() && this.getSceneTree().getCamera3D() == this;
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
     * @see Camera3D#isEnabled()
     */
    public final void setEnabled(boolean enabled) {
        if(this.isCurrent() && !enabled) {
            this.getSceneTree().setCamera3D(null);
        }
        this.enabled = enabled;
    }

    /**
     * Getter method.
     * Returns true if this camera is enabled, false if it is disabled.
     *
     * @return True if this camera is enabled, false if it is disabled.
     *
     * @see Camera3D#setEnabled(boolean)
     */
    public final boolean isEnabled() {
        return this.enabled;
    }
}