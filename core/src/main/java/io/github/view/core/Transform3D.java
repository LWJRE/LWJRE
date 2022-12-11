package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.scene.SceneObject;

public class Transform3D extends Position3D {

	private Vector3 rotation = Vector3.ZERO;
	private Vector3 scale = Vector3.ONE;

	public Transform3D(SceneObject object) {
		super(object);
	}

	public final Vector3 getRotationRadians() {
		return this.rotation;
	}

	public final Vector3 getRotationDegrees() {
		return new Vector3((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	public final void setRotationRadians(Vector3 rotation) {
		if(rotation == null) this.rotation = Vector3.ZERO;
		else this.rotation = rotation;
	}

	public final void setRotationRadians(float x, float y, float z) {
		this.rotation = new Vector3(x, y, z);
	}

	public final void setRotationDegrees(Vector3 rotation) {
		if(rotation == null) this.rotation = Vector3.ZERO;
		else this.setRotationDegrees(rotation.x(), rotation.y(), rotation.z());
	}

	public final void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vector3((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public final void rotateRadians(Vector3 rotation) {
		this.rotation = this.rotation.plus(rotation);
	}

	public final void rotateRadians(float x, float y, float z) {
		this.rotation = this.rotation.plus(x, y, z);
	}

	public final void rotateDegrees(Vector3 rotation) {
		this.rotateDegrees(rotation.x(), rotation.y(), rotation.z());
	}

	public final void rotateDegrees(float x, float y, float z) {
		this.rotateRadians((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public final Vector3 getScale() {
		return this.scale;
	}

	public final void setScale(Vector3 scale) {
		if(scale == null) this.scale = Vector3.ZERO;
		else this.scale = scale;
	}

	public final void setScale(float x, float y, float z) {
		this.scale = new Vector3(x, y, z);
	}

	public final Matrix4 rotationMatrixX() {
		return new Matrix4(
				1.0f, 0.0f, 0.0f, 0.0f,
				0.0f, (float) Math.cos(-this.rotation.x()), (float) -Math.sin(-this.rotation.x()), 0.0f,
				0.0f, (float) Math.sin(-this.rotation.x()), (float) Math.cos(-this.rotation.x()), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 rotationMatrixY() {
		return new Matrix4(
				(float) Math.cos(-this.rotation.y()), 0.0f, (float) Math.sin(-this.rotation.y()), 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f,
				(float) -Math.sin(-this.rotation.y()), 0.0f, (float) Math.cos(-this.rotation.y()), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 rotationMatrixZ() {
		return new Matrix4(
				(float) Math.cos(-this.rotation.z()), (float) -Math.sin(-this.rotation.z()), 0.0f, 0.0f,
				(float) Math.sin(-this.rotation.z()), (float) Math.cos(-this.rotation.z()), 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 rotationMatrix() {
		return this.rotationMatrixX().multiply(this.rotationMatrixY()).multiply(this.rotationMatrixZ());
	}

	public final Matrix4 scalingMatrix() {
		return new Matrix4(
				this.scale.x(), 0.0f, 0.0f, 0.0f,
				0.0f, this.scale.y(), 0.0f, 0.0f,
				0.0f, 0.0f, this.scale.z(), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 matrix() {
		return this.translationMatrix().multiply(this.rotationMatrix()).multiply(this.scalingMatrix());
	}
}
