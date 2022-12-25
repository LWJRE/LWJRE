package io.github.view.core;

import io.github.view.geometry.BoundingBox3D;
import io.github.view.math.Vector3;

import java.util.ArrayList;

public class CollisionObject3D extends Transform3D {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	private final BoundingBox3D boundingBox = new BoundingBox3D(new Vector3(-0.5f, -0.5f, -0.5f), Vector3.RIGHT, Vector3.UP, Vector3.FORWARD);

	@Override
	protected void onStart() {
		COLLIDERS.add(this);
		super.onStart();
	}

	public final void moveAndCollide(Vector3 translation) {
		this.translate(translation);
		/*COLLIDERS.forEach(collider -> COLLIDERS.stream()
				.filter(otherCollider -> !otherCollider.equals(collider))
				.map(collider::computeCollision)
				.filter(Objects::nonNull)
				.findFirst()
				.ifPresent(collider::onCollision));*/
	}
/*
	protected void onCollision(Collision3D collision) {

	}

	public final Collision3D computeCollision(CollisionObject3D collider) {
		return null;
	}
*/
	@Override
	protected void onExit() {
		COLLIDERS.remove(this);
		super.onExit();
	}
}
