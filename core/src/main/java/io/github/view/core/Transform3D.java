package io.github.view.core;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.scene.SceneObject;

public class Transform3D extends Script {

	private final Position3D position;
	private final Rotation3D rotation;
	private final Scale3D scale;

	public Transform3D(SceneObject object) {
		super(object);
		this.position = this.object.getScript(Position3D.class);
		this.rotation = this.object.getScript(Rotation3D.class);
		this.scale = this.object.getScript(Scale3D.class);
	}

	public final Vector3 getPosition() {
		return this.position.get();
	}

	public final Vector3 getRotation() {
		return this.rotation.get();
	}

	public final Vector3 getScale() {
		return this.scale.get();
	}

	// TODO: Setters

	public final void translate(Vector3 translation) {
		this.position.set(this.position.get().plus(translation));
	}

	public final void rotate(float x, float y, float z) {
		this.rotation.set(this.rotation.get().plus(x, y, z));
	}

	// TODO: Other operations

	public final Matrix4 matrix() {
		return this.position.matrix().multiply(this.rotation.matrix()).multiply(this.scale.matrix());
	}
}
