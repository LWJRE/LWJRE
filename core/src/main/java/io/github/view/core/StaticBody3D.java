package io.github.view.core;

import io.github.view.physics.Collision3D;
import io.github.view.scene.SceneObject;

public class StaticBody3D extends PhysicObject3D {

	public StaticBody3D(SceneObject object) {
		super(object);
	}

	@Override
	public void onPhysicsUpdate(float time) {

	}

	@Override
	public void onCollision(Collision3D collision) {

	}
}
