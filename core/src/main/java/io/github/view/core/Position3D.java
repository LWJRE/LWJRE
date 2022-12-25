package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;

public class Position3D extends Node {

	private Vector3 position = Vector3.ZERO;

	public final Vector3 localPosition() {
		return this.position;
	}

	public final Vector3 globalPosition() {
		if(this.getParent() instanceof Position3D parent)
			return parent.globalPosition().plus(this.position);
		return this.position;
	}

	public final Matrix4 localTranslation() {
		return new Matrix4(
				1.0f, 0.0f, 0.0f, this.position.x(),
				0.0f, 1.0f, 0.0f, this.position.y(),
				0.0f, 0.0f, 1.0f, this.position.z(),
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 globalTranslation() {
		if(this.getParent() instanceof Position3D parent)
			return parent.globalTranslation().multiply(this.localTranslation());
		return this.localTranslation();
	}

	public final void setPosition(Vector3 position) {
		if(position != null) this.position = position;
		else this.position = Vector3.ZERO;
	}

	public final void setPosition(float x, float y, float z) {
		this.position = new Vector3(x, y, z);
	}

	public final void translate(Vector3 translation) {
		if(translation != null)
			this.position = this.position.plus(translation);
	}

	public final void translate(float x, float y, float z) {
		this.position = this.position.plus(x, y, z);
	}
}
