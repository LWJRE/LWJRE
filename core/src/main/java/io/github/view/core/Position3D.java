package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.scene.SceneObject;

public class Position3D extends Script {

	private Vector3 position = Vector3.ZERO;

	public Position3D(SceneObject object) {
		super(object);
	}

	public final Vector3 getPosition() {
		return this.position;
	}

	public final void setPosition(Vector3 position) {
		if(position == null) this.position = Vector3.ZERO;
		else this.position = position;
	}

	public final void setPosition(float x, float y, float z) {
		this.position = new Vector3(x, y, z);
	}

	public final void translate(Vector3 translation) {
		this.position = this.position.plus(translation);
	}

	public final void translate(float x, float y, float z) {
		this.position = this.position.plus(x, y, z);
	}

	public final Matrix4 translationMatrix() {
		return new Matrix4(
				1.0f, 0.0f, 0.0f, this.position.x(),
				0.0f, 1.0f, 0.0f, this.position.y(),
				0.0f, 0.0f, 1.0f, this.position.z(),
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}
}
