package io.github.view.core;

import io.github.view.math.Vector3;

public class KinematicBody3D extends Script {

	private final Transform3D transform;
	private Vector3 velocity = Vector3.ZERO;
	private Vector3 acceleration = new Vector3(0.0f, -9.81f, 0.0f);

	public KinematicBody3D(SceneObject object) {
		super(object);
		this.transform = this.object.getScript(Transform3D.class);
	}

	@Override
	public void onUpdate(float time) {
		super.onUpdate(time);
		Vector3 velocityDelta = this.acceleration.multipliedBy(time);
		this.velocity = this.velocity.plus(velocityDelta);
		Vector3 positionDelta = this.velocity.multipliedBy(time);
		this.transform.translate(positionDelta);
	}
}
