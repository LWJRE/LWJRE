package engine.physics.tree;

import engine.core.tree.Transform3D;
import engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.Optional;

public class CollisionObject3D extends Transform3D {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	@Override
	protected void onEnterTree() {
		COLLIDERS.add(this);
		super.onEnterTree();
	}

	public final void moveAndCollide(Vec3f translation) {
		this.position = this.position.plus(translation);
		COLLIDERS.stream().filter(collider -> !collider.equals(this))
				.map(this::computeCollision)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach(this::onCollision);
	}

	public final Optional<Collision3D> computeCollision(CollisionObject3D collisionObject) {
		// TODO: Detect collision with multiple shapes
		return this.getChildOfType(BoundingBox3D.class).flatMap(boundingBox -> collisionObject.getChildOfType(BoundingBox3D.class).map(boundingBox::computeCollision));
	}

	protected void onCollision(Collision3D collision) {

	}

	@Override
	protected void onExitTree() {
		COLLIDERS.remove(this);
		super.onExitTree();
	}
}
