package io.github.view.core;

import io.github.view.math.Vector3;

public class KinematicBody3D extends Script {

	private Transform3D transform;
	private Vector3 velocity = Vector3.ZERO;
	private Vector3 acceleration = Vector3.ZERO;

	public KinematicBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.transform = this.object.getScript(Transform3D.class);
		super.onStart();
	}

	@Override
	public void onUpdate() {
		super.onUpdate(); // TODO: Delta time
		Vector3 velocityDelta = this.acceleration.multipliedBy(0.01f);
		this.velocity = this.velocity.plus(velocityDelta);
		Vector3 positionDelta = this.velocity.multipliedBy(0.01f);
		this.transform.translate(positionDelta);
	}
}
