package io.github.view.core;

import io.github.view.math.Vector3;

public class DirectionalLight3D extends Light {

	private Vector3 direction = Vector3.DOWN;

	public final Vector3 getDirection() {
		return this.direction;
	}

	public final void setDirection(Vector3 direction) {
		if(direction == null) this.direction = Vector3.ZERO;
		else this.direction = direction;
	}
}
