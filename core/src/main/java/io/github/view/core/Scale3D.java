package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;

public class Scale3D extends Script {

	private Vector3 scale = Vector3.ONE;

	public Scale3D(SceneObject object) {
		super(object);
	}

	public final Vector3 get() {
		return this.scale;
	}

	public final void set(Vector3 scale) {
		if(scale == null)
			throw new NullPointerException();
		this.scale = scale;
	}

	public final Matrix4 matrix() {
		return new Matrix4(
				this.scale.x(), 0.0f, 0.0f, 0.0f,
				0.0f, this.scale.y(), 0.0f, 0.0f,
				0.0f, 0.0f, this.scale.z(), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}
}
