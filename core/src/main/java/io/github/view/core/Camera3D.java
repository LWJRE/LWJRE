package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.scene.SceneObject;

public class Camera3D extends Script {

	private static Camera3D current;

	public static Matrix4 currentProjectionMatrix() {
		return current != null ? current.projectionMatrix() : Matrix4.IDENTITY;
	}

	public static Matrix4 currentViewMatrix() {
		return current != null ? current.viewMatrix() : Matrix4.IDENTITY;
	}

	private final Position3D position;

	public float fov = 70.0f;
	public float nearPlane = 0.1f;
	public float farPlane = 1000.0f;

	public Camera3D(SceneObject object) {
		super(object);
		this.position = this.object.getScript(Position3D.class);
	}

	public final void makeCurrent() {
		current = this;
	}

	public final Matrix4 projectionMatrix() {
		float m00 = 1.0f / (float) Math.tan(Math.toRadians(fov / 2.0f));
		float m11 = m00 * (960.0f / 540.0f);
		float m22 = -(farPlane + nearPlane) / (farPlane - nearPlane);
		float m23 = -(2 * farPlane * nearPlane) / (farPlane - nearPlane);
		return new Matrix4(
				m00, 0.0f, 0.0f, 0.0f,
				0.0f, m11, 0.0f, 0.0f,
				0.0f, 0.0f, m22, m23,
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	public final Matrix4 viewMatrix() {
		return new Matrix4(
				1.0f, 0.0f, 0.0f, -this.position.getPosition().x(),
				0.0f, 1.0f, 0.0f, -this.position.getPosition().y(),
				0.0f, 0.0f, 1.0f, -this.position.getPosition().z(),
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}
}
