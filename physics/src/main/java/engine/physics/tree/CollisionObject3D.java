package engine.physics.tree;

import engine.core.tree.Transform3D;
import engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

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

	public final void moveAndCollide(Vec3f translation) {
		if(translation.lengthSquared() > 0.0f) {
			this.position = this.position.plus(translation);
			COLLIDERS.stream().filter(collider -> !collider.equals(this))
					.map(this::computeCollision)
					.filter(Optional::isPresent)
					.map(Optional::get)
					.forEach(this::onCollision);
		}
	}

	public final Optional<Collision3D> computeCollision(CollisionObject3D collisionObject) {
		return this.getChild(BoundingBox3D.class)
				.flatMap(boundingBox -> collisionObject.getChild(BoundingBox3D.class)
						.map(boundingBox::computeCollision)
						.map(collision -> new Collision3D(collisionObject, collision.normal(), collision.depth())));
	}

	protected void onCollision(Collision3D collision) {

	}

	@Override
	protected void onExit() {
		COLLIDERS.remove(this);
		super.onExit();
	}
}
