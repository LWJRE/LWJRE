package io.github.view.core;

import io.github.view.math.Vector3;
import io.github.view.physics.Collision3D;
import io.github.view.scene.SceneObject;

public class KinematicBody3D extends PhysicObject3D {

	protected Vector3 velocity = Vector3.DOWN.multipliedBy(5.0f);
	protected Vector3 acceleration = Vector3.ZERO;

	public KinematicBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onPhysicsUpdate(float time) {
		Vector3 velocityDelta = this.acceleration.multipliedBy(time);
		this.velocity = this.velocity.plus(velocityDelta);
		Vector3 positionDelta = this.velocity.multipliedBy(time);
		this.transform.translate(positionDelta);
	}

	@Override
	public void onCollision(Collision3D collision) {
		this.transform.translate(collision.normal().multipliedBy(collision.depth()));
	}
}
