package io.github.view.core;

import io.github.view.math.Vector3;
import io.github.view.physics.PhysicsSystem3D;
import io.github.view.geometry.BoundingBox3D;

public class StaticBody3D extends Script {

	protected final Transform3D transform;
	protected final BoundingBox3D boundingBox = new BoundingBox3D(Vector3.ZERO, Vector3.RIGHT, Vector3.UP, Vector3.FORWARD);

	public StaticBody3D(SceneObject object) {
		super(object);
		this.transform = this.object.getScript(Transform3D.class);
	}

	@Override
	public void onStart() {
		PhysicsSystem3D.addObject(this);
		super.onStart();
	}

	@Override
	public void onExit() {
		PhysicsSystem3D.removeObject(this);
		super.onExit();
	}

	public BoundingBox3D worldSpaceBoundingBox() {
		return this.boundingBox.transformed(this.transform.matrix());
	}

	public boolean isColliding(StaticBody3D collider) {
		return this.worldSpaceBoundingBox().intersects(collider.worldSpaceBoundingBox());
	}
}
