package gamma.engine.core.node;

import gamma.engine.core.annotation.EditorVariable;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

/**
 * Node that represents a 3D camera.
 *
 * @author Nico
 */
public class Camera3D extends Transform3D {

	/** Current camera to use when rendering */
	private static Camera3D current = new Camera3D();

	/**
	 * Gets the current camera.
	 * If no camera is current, this will return a default camera.
	 *
	 * @return The current camera or the default camera if no camera is current
	 */
	public static Camera3D current() {
		return current;
	}

	/** Whether this camera should be current on start */
	@EditorVariable
	public boolean startAsCurrent = false;

	@EditorVariable
	public float fov = 70.0f;
	@EditorVariable
	public float nearPlane = 0.1f;
	@EditorVariable
	public float farPlane = 1000.0f;

	@Override
	protected void onEnterTree() {
		if(this.startAsCurrent)
			this.makeCurrent();
		super.onEnterTree();
	}

	@Override
	protected void onExitTree() {
		current = new Camera3D();
		super.onExitTree();
	}

	/**
	 * Makes this camera current.
	 */
	public final void makeCurrent() {
		current = this;
	}

	/**
	 * Sets the camera's pitch, i.e., its rotation on the X axis.
	 *
	 * @param degrees Rotation degrees on the X axis
	 */
	public final void setPitch(float degrees) {
		this.rotation = new Vec3f((float) Math.toRadians(degrees), this.rotation.y(), this.rotation.z());
	}

	/**
	 * Gets the camera's pitch, i.e., its rotation on the X axis.
	 *
	 * @return The camera's rotation degrees on the X axis
	 */
	public final float getPitch() {
		return (float) Math.toDegrees(this.rotation.x());
	}

	/**
	 * Sets the camera's yaw, i.e., its rotation on the Y axis.
	 *
	 * @param degrees Rotation degrees on the Y axis
	 */
	public final void setYaw(float degrees) {
		this.rotation = new Vec3f(this.rotation.x(), (float) Math.toRadians(degrees), this.rotation.z());
	}

	/**
	 * Gets the camera's yaw, i.e., its rotation on the Y axis.
	 *
	 * @return The camera's rotation degrees on the Y axis
	 */
	public final float getYaw() {
		return (float) Math.toDegrees(this.rotation.y());
	}

	/**
	 * Sets the camera's roll, i.e., its rotation on the Z axis.
	 *
	 * @param degrees Rotation degrees on the Z axis
	 */
	public final void setRoll(float degrees) {
		this.rotation = new Vec3f(this.rotation.x(), this.rotation.y(), (float) Math.toRadians(degrees));
	}

	/**
	 * Gets the camera's roll, i.e., its rotation on the Z axis.
	 *
	 * @return The camera's rotation degrees on the Z axis
	 */
	public final float getRoll() {
		return (float) Math.toDegrees(this.rotation.z());
	}

	public final Mat4f projectionMatrix() {
		float m00 = 1.0f / (float) Math.tan(Math.toRadians(fov / 2.0f));
		float m11 = m00 * (960.0f / 540.0f);
		float m22 = -(farPlane + nearPlane) / (farPlane - nearPlane);
		float m23 = -(2 * farPlane * nearPlane) / (farPlane - nearPlane);
		return new Mat4f(
				m00, 0.0f, 0.0f, 0.0f,
				0.0f, m11, 0.0f, 0.0f,
				0.0f, 0.0f, m22, m23,
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	/**
	 * Returns this camera's view matrix in global space.
	 *
	 * @return This camera's view matrix
	 */
	public final Mat4f viewMatrix() {
		Mat4f matrix = Mat4f.translation(this.globalPosition().negated()).multiply(this.localRotation());
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(matrix);
		return matrix;
	}
}
