package io.github.view.core;

import io.github.view.math.Vector3;
import io.github.view.physics.Collision3D;

import java.util.ArrayList;
import java.util.Optional;

public class CollisionObject3D extends Transform3D {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	@Override
	protected void onStart() {
		COLLIDERS.add(this);
		super.onStart();
	}

	// TODO: Better shape detection

	public final void moveAndCollide(Vector3 translation) {
		this.translate(translation);
		COLLIDERS.stream().filter(collider -> !collider.equals(this))
				.map(this::computeCollision)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(this::onCollision);
	}

	public final Optional<Collision3D> computeCollision(CollisionObject3D collisionObject) {
		return this.getChild(BoundingBox3D.class).flatMap(boundingBox -> collisionObject.getChild(BoundingBox3D.class).map(boundingBox::computeCollision));
	}

	protected void onCollision(Collision3D collision) {
		System.out.println(collision);
	}

	@Override
	protected void onExit() {
		COLLIDERS.remove(this);
		super.onExit();
	}
}
