package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;

public class Transform3D extends Position3D {

	// TODO: Add rotation
	private Vector3 scale = Vector3.ONE;

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

	public final Matrix4 localScaling() {
		return new Matrix4(
				this.scale.x(), 0.0f, 0.0f, 0.0f,
				0.0f, this.scale.y(), 0.0f, 0.0f,
				0.0f, 0.0f, this.scale.z(), 0.0f,
				0.0f, 0.0f, 0.0f, 1.0f
		);
	}

	public final Matrix4 globalScaling() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalScaling().multiply(this.localScaling());
		return this.localScaling();
	}

	public final Matrix4 localTransformation() {
		return this.localTranslation().multiply(this.localScaling());
	}

	public final Matrix4 globalTransformation() {
		return this.globalTranslation().multiply(this.globalScaling());
	}
}
