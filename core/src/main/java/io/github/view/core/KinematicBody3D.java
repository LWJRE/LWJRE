package io.github.view.core;

import io.github.view.math.Vector3;

public class KinematicBody3D extends StaticBody3D {

	private Vector3 velocity = Vector3.ZERO;
	private Vector3 acceleration = Vector3.DOWN.multipliedBy(9.81f);

	public KinematicBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onUpdate(float time) {
		Vector3 velocityDelta = this.acceleration.multipliedBy(time);
		this.velocity = this.velocity.plus(velocityDelta);
		Vector3 positionDelta = this.velocity.multipliedBy(time);
		this.transform.translate(positionDelta);
		super.onUpdate(time);
	}
}
