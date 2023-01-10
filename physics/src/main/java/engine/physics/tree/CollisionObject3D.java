package engine.physics.tree;

import engine.core.tree.Transform3D;
import engine.physics.Collision3D;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

public class CollisionObject3D extends Transform3D {

	public static final int ITERATIONS = 5;

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	@Override
	protected void onEnterTree() {
		COLLIDERS.add(this);
		super.onEnterTree();
	}

	public final void moveAndCollide(Vec3f translation) {
		if(translation.lengthSquared() > 0.0f) {
			for(int i = 0; i < ITERATIONS; i++) {
				this.position = this.position.plus(translation.dividedBy(ITERATIONS));
				COLLIDERS.stream().filter(collider -> !collider.equals(this))
						.flatMap(this::computeCollision)
						.forEach(this::onCollision);
			}
		}
	}

	private Stream<Collision3D> computeCollision(CollisionObject3D collisionObject) {
		return this.getChildrenOfType(BoundingBox3D.class)
				.flatMap(boundingBox -> collisionObject.getChildrenOfType(BoundingBox3D.class)
						.map(boundingBox::computeCollision)
						.filter(Objects::nonNull)
						.map(collision -> new Collision3D(collisionObject, collision.normal(), collision.depth())));
	}

	protected void onCollision(Collision3D collision) {

	}

	@Override
	protected void onExitTree() {
		COLLIDERS.remove(this);
		super.onExitTree();
	}
}
