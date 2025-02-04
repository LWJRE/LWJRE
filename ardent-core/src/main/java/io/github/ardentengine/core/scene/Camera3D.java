package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.math.Matrix4;
import io.github.ardentengine.core.rendering.RenderingServer;

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
    void update(float deltaTime) {
        RenderingServer.getInstance().setCamera(this);
        super.update(deltaTime);
    }

    public final float yaw() {
        return this.rotation().y();
    }

    public final void setYaw(float yaw) {
        this.setRotation(this.pitch(), yaw, this.roll());
    }

    public final float pitch() {
        return this.rotation().x();
    }

    public final void setPitch(float pitch) {
        this.setRotation(pitch, this.yaw(), this.roll());
    }

    public final float roll() {
        return this.rotation().z();
    }

    public final void setRoll(float roll) {
        this.setRotation(this.pitch(), this.yaw(), roll);
    }

    /**
     * Returns the camera's projection matrix.
     * Used in shaders.
     *
     * @return The camera's projection matrix.
     */
    public final Matrix4 projectionMatrix() {
        return Matrix4.perspectiveProjection(this.fov, DisplayServer.getInstance().getWindowSize(), this.nearPlane, this.farPlane);
    }

    /**
     * Returns the camera's view matrix.
     * Used in shaders.
     *
     * @return The camera's view matrix.
     */
    public final Matrix4 viewMatrix() {
        return Matrix4.rotation(this.rotation()).multiply(Matrix4.translation(this.globalPosition().negated()));
    }

    // FIXME: Reimplement the ability to switch between cameras
}