package io.github.view.core;

import io.github.view.math.Vector3;

public class PointLight3D extends Light {

	private Vector3 position;
	// TODO: Attenuation

	public final Vector3 getPosition() {
		return this.position;
	}

	public final void setPosition(Vector3 position) {
		if(position == null) this.position = Vector3.ZERO;
		else this.position = position;
	}
}
