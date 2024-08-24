package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.DisplaySystem;
import io.github.ardentengine.core.RenderingSystem;
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
    public void onUpdate(float delta) {
        RenderingSystem.getInstance().setCamera(this);
    }

    /**
     * Returns the camera's projection matrix.
     * Used in shaders.
     *
     * @return The camera's projection matrix.
     */
    public final Mat4f projectionMatrix() {
        return Mat4f.perspectiveProjection(this.fov, DisplaySystem.getInstance().getWindowSize(), this.nearPlane, this.farPlane);
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

    // FIXME: Reimplement the ability to switch between cameras
}