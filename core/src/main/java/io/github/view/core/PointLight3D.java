package io.github.view.core;

import io.github.view.math.Vector3;

public class PointLight3D extends Light {

	private final Position3D position;
	// TODO: Attenuation

	public PointLight3D(SceneObject object) {
		super(object);
		this.position = this.object.getScript(Position3D.class);
	}

	public final Vector3 getPosition() {
		return this.position.get();
	}

	public final void setPosition(Vector3 position) {
		this.position.set(position);
	}
}
