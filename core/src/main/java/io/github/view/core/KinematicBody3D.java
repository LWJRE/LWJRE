package io.github.view.core;

import io.github.view.math.Vector3;

public class KinematicBody3D extends CollisionObject3D {

	private Vector3 velocity = Vector3.ZERO;
	private Vector3 acceleration = new Vector3(0.0f, -9.81f, 0.0f);

	@Override
	protected void onUpdate(float delta) {
		Vector3 velocityDelta = this.acceleration.multipliedBy(delta);
		this.velocity = this.velocity.plus(velocityDelta);
		Vector3 movement = this.velocity.multipliedBy(delta);
		this.moveAndCollide(movement);
		super.onUpdate(delta);
	}
}
