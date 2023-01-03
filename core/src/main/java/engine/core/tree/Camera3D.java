package engine.core.tree;

import vecmatlib.matrix.Mat4f;

public class Camera3D extends Transform3D {

	// TODO: Make better camera

	private static Camera3D current;

	public static Camera3D current() {
		return current;
	}

	public static Mat4f currentProjectionMatrix() {
		return current != null ? current.projectionMatrix() : Mat4f.Identity();
	}

	public static Mat4f currentViewMatrix() {
		return current != null ? current.viewMatrix() : Mat4f.Identity();
	}

	public float fov = 70.0f;
	public float nearPlane = 0.1f;
	public float farPlane = 1000.0f;

	public Camera3D() {
		this.makeCurrent();
	}

	public final void makeCurrent() {
		current = this;
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

	public final Mat4f viewMatrix() {
		return new Mat4f(
				1.0f, 0.0f, 0.0f, -this.globalPosition().x(),
				0.0f, 1.0f, 0.0f, -this.globalPosition().y(),
				0.0f, 0.0f, 1.0f, -this.globalPosition().z(),
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}
}
