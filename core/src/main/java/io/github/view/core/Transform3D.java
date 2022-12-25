package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;

public class Transform3D extends Position3D {

	// TODO: Add rotation
	private Vector3 rotation = Vector3.ZERO;
	private Vector3 scale = Vector3.ONE;

	public final Vector3 rotationRadians() {
		return this.rotation;
	}

	public final Vector3 rotationDegrees() {
		return new Vector3((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	public final void setRotationRadians(Vector3 radians) {
		if(radians != null) this.rotation = radians;
		else this.rotation = Vector3.ZERO;
	}

	public final void setRotationRadians(float x, float y, float z) {
		this.rotation = new Vector3(x, y, z);
	}

	public final void setRotationDegrees(Vector3 degrees) {
		if(degrees != null) this.setRotationDegrees(degrees.x(), degrees.y(), degrees.z());
		else this.rotation = Vector3.ZERO;
	}

	public final void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vector3((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public final void rotateRadians(Vector3 radians) {
		if(radians != null)
			this.rotation = this.rotation.plus(radians);
	}

	public final void rotateRadians(float x, float y, float z) {
		this.rotation = this.rotation.plus(x, y, z);
	}

	public final void rotateDegrees(Vector3 degrees) {
		if(degrees != null)
			this.rotateDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	public final void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
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

	public final Vector3 localScale() {
		return this.scale;
	}

	public final Vector3 globalScale() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalScale().multiply(this.scale);
		return this.scale;
	}

	public final void setScale(Vector3 scale) {
		if(scale != null) this.scale = scale;
		else this.scale = Vector3.ZERO;
	}

	public final void setScale(float x, float y, float z) {
		this.scale = new Vector3(x, y, z);
	}

	public final Matrix4 scalingMatrix() {
		return new Matrix4(
				this.scale.x(), 0.0f, 0.0f, 0.0f,
				0.0f, this.scale.y(), 0.0f, 0.0f,
				0.0f, 0.0f, this.scale.z(), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 localTransformation() {
		return this.localTranslation().multiply(this.rotationMatrix()).multiply(this.scalingMatrix());
	}

	public final Matrix4 globalTransformation() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(this.localTransformation());
		if(this.getParent() instanceof Position3D parent)
			return parent.globalTranslation().multiply(this.localTransformation());
		return this.localTransformation();
	}
}
