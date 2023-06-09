package gamma.engine.core.nodes;

import gamma.engine.core.annotations.EditorAngle;
import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.window.Window;
import gamma.engine.core.resources.Shader;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

/**
 * Node that represents a perspective camera in a 3D space.
 *
 * @author Nico
 */
public class Camera3D extends Node3D {

	/** The reference to the current camera is needed to make the previous one non-current when changing camera */
	private static Camera3D currentCamera = null;

	/**
	 * Whether this camera is current or not.
	 */
	@EditorVariable(name = "Is current")
	private boolean current = false;

	/**
	 * The camera's field of view.
	 */
	@EditorVariable(name = "Fov")
	@EditorAngle(min = 0.0f)
	public float fov = 1.22173f;

	/**
	 * Near plane distance.
	 */
	@EditorVariable(name = "Near plane")
	@EditorRange(min = 0.001f)
	public float nearPlane = 0.1f;

	/**
	 * Far plane distance.
	 */
	@EditorVariable(name = "Far plane")
	@EditorRange(min = 0.001f)
	public float farPlane = 1000.0f;

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		if(this.current) {
			Shader.setUniformStatic("projection_matrix", this.projectionMatrix());
			Shader.setUniformStatic("view_matrix", this.viewMatrix());
		}
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
//		DebugRenderer.addToBatch(ConeMesh.INSTANCE, mesh -> {
//			Mat4f cone = Mat4f.translation(this.globalPosition().plus(0.0f, -0.5f, 0.0f)).multiply(Mat4f.rotation((float) (-Math.PI * 0.5), 0.0f, 0.0f));
//			DebugRenderer.SHADER.setUniform("transformation_matrix", cone);
//			DebugRenderer.SHADER.setUniform("color", 0.0f, 1.0f, 0.0f, 1.0f);
//			mesh.drawElements();
//		});
	}

	/**
	 * Gets the camera's pitch angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the x axis.
	 *
	 * @return The camera's pitch angle in radians.
	 */
	public float pitch() {
		return this.rotation.x();
	}

	/**
	 * Gets the camera's yaw angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the y axis.
	 *
	 * @return The camera's yaw angle in radians.
	 */
	public float yaw() {
		return this.rotation.y();
	}

	/**
	 * Gets the camera's roll angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the z axis.
	 *
	 * @return The camera's roll angle in radians.
	 */
	public float roll() {
		return this.rotation.z();
	}

	/**
	 * Gets the camera's pitch angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the x axis.
	 *
	 * @return The camera's pitch angle in degrees.
	 */
	public float pitchDegrees() {
		return (float) Math.toDegrees(this.pitch());
	}

	/**
	 * Gets the camera's yaw angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the y axis.
	 *
	 * @return The camera's yaw angle in degrees.
	 */
	public float yawDegrees() {
		return (float) Math.toDegrees(this.yaw());
	}

	/**
	 * Gets the camera's roll angle.
	 * This is equivalent to the {@link Camera3D#rotation} on the z axis.
	 *
	 * @return The camera's roll angle in degrees.
	 */
	public float rollDegrees() {
		return (float) Math.toDegrees(this.roll());
	}

	/**
	 * Sets the camera's pitch angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the x axis.
	 *
	 * @param pitch The camera's pitch angle in radians
	 */
	public void setPitch(float pitch) {
		this.rotation = new Vec3f(pitch, this.rotation.yz());
	}

	/**
	 * Sets the camera's yaw angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the y axis.
	 *
	 * @param yaw The camera's yaw angle in radians
	 */
	public void setYaw(float yaw) {
		this.rotation = new Vec3f(this.rotation.x(), yaw, this.rotation.z());
	}

	/**
	 * Sets the camera's roll angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the z axis.
	 *
	 * @param roll The camera's roll angle in radians
	 */
	public void setRoll(float roll) {
		this.rotation = new Vec3f(this.rotation.xy(), roll);
	}

	/**
	 * Sets the camera's pitch angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the x axis.
	 *
	 * @param pitch The camera's pitch angle in degrees
	 */
	public void setPitchDegrees(float pitch) {
		this.setPitch((float) Math.toRadians(pitch));
	}

	/**
	 * Sets the camera's yaw angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the y axis.
	 *
	 * @param yaw The camera's yaw angle in degrees
	 */
	public void setYawDegrees(float yaw) {
		this.setYaw((float) Math.toRadians(yaw));
	}

	/**
	 * Sets the camera's roll angle.
	 * This is equivalent to changing the {@link Camera3D#rotation} on the z axis.
	 *
	 * @param roll The camera's roll angle in degrees
	 */
	public void setRollDegrees(float roll) {
		this.setRoll((float) Math.toRadians(roll));
	}

	/**
	 * Makes the previous camera non-current and makes this the current camera.
	 *
	 * @see Camera3D#isCurrent()
	 */
	public void makeCurrent() {
		if(currentCamera != null) {
			currentCamera.current = false;
		}
		currentCamera = this;
		this.current = true;
	}

	/**
	 * Returns true if this is the current camera, otherwise false.
	 *
	 * @return True if this is the current camera, otherwise false
	 */
	public boolean isCurrent() {
		return this.current;
	}

	/**
	 * Computes the camera's projection matrix, a 3D perspective projection.
	 *
	 * @return The camera's projection matrix
	 */
	public Mat4f projectionMatrix() {
		float focalLength = (float) (1.0f / Math.tan(this.fov / 2.0f));
		// TODO: Don't get the window every frame
		return new Mat4f(
				focalLength, 0.0f, 0.0f, 0.0f,
				0.0f, focalLength * Window.current().aspectRatio(), 0.0f, 0.0f,
				0.0f, 0.0f, -(this.farPlane + this.nearPlane) / (this.farPlane - this.nearPlane), -(2 * this.farPlane * this.nearPlane) / (this.farPlane - this.nearPlane),
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	/**
	 * Computes the camera's view matrix.
	 *
	 * @return The camera's view matrix
	 */
	public Mat4f viewMatrix() {
		return this.globalRotation().multiply(Mat4f.translation(this.globalPosition().negated()));
	}
}
