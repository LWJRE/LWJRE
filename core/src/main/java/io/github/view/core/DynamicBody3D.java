package io.github.view.core;

import io.github.view.math.Vector3;

import java.util.ArrayList;

public class DynamicBody3D extends KinematicBody3D {

	protected final ArrayList<Vector3> forces = new ArrayList<>();
	protected final float mass = 1.0f;

	public DynamicBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onStart() {
		this.forces.add(this.acceleration.multipliedBy(this.mass));
		super.onStart();
	}

	@Override
	public void onUpdate(float time) {
		Vector3 resultantForce = this.forces.stream().reduce(Vector3.ZERO, Vector3::plus);
		this.acceleration = resultantForce.dividedBy(this.mass);
		super.onUpdate(time);
	}
}
