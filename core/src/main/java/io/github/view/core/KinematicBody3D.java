package io.github.view.core;

import io.github.view.math.Vector3;

public class KinematicBody3D extends CollisionObject3D {

	private Vector3 velocity = Vector3.ZERO;
	private Vector3 acceleration = new Vector3(0.0f, -9.81f, 0.0f);
}
