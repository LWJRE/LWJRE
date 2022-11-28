package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;

public class Position3D extends Script {

	private Vector3 position = Vector3.ZERO;

	public Position3D(SceneObject object) {
		super(object);
	}

	public final Vector3 get() {
		return this.position;
	}

	public final void set(Vector3 position) {
		if(position == null)
			throw new NullPointerException();
		this.position = position;
	}

	public final Matrix4 matrix() {
		return new Matrix4(
				1.0f, 0.0f, 0.0f, this.position.x(),
				0.0f, 1.0f, 0.0f, this.position.y(),
				0.0f, 0.0f, 1.0f, this.position.z(),
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}
}
