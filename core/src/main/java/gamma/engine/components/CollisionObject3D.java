package gamma.engine.components;

import gamma.engine.annotations.EditorIndex;
import gamma.engine.physics.Collision3D;
import gamma.engine.physics.Projection;
import gamma.engine.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

import java.util.HashSet;
import java.util.Optional;

/**
 * Controls an entity's collisions while moving.
 * The entity needs a {@link BoundingBox3D} component to collide with other objects.
 *
 * @see BoundingBox3D
 *
 * @author Nico
 */
@EditorIndex(2)
public class CollisionObject3D extends Component {

	/** Set of all the colliders in the scene */
	private static final HashSet<CollisionObject3D> COLLIDERS = new HashSet<>();

	@Override
	protected void onStart() {
		super.onStart();
		COLLIDERS.add(this);
	}

	/**
	 * Moves this collider by the given movement and handles possible collisions.
	 * The entity needs a {@link Transform3D} component to move.
	 * Calls {@link CollisionObject3D#onCollision(Collision3D)} if a collision happened.
	 *
	 * @param movement The translation to apply to this collider
	 */
	public final void moveAndCollide(Vec3f movement) {
		if(movement.lengthSquared() > 0.0f) {
			this.getComponent(Transform3D.class).ifPresent(transform -> {
				transform.position = transform.position.plus(movement);
				COLLIDERS.stream().filter(collider -> !collider.equals(this))
						.map(this::computeCollision)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.forEach(this::onCollision);
			});
		}
	}

	@Override
	protected void onExit() {
		super.onExit();
		COLLIDERS.remove(this);
	}

	/**
	 * Checks if this collider collided with the given one and computes the collision.
	 *
	 * @param with The other collider to check
	 *
	 * @return An {@link Optional} containing the result of the collision or an empty {@code Optional} if no collision happened.
	 */
	private Optional<Collision3D> computeCollision(CollisionObject3D with) {
		return this.getComponent(BoundingBox3D.class).map(boxA -> with.getComponent(BoundingBox3D.class).map(boxB -> {
			Vec3f normal = Vec3f.Zero();
			float depth = Float.POSITIVE_INFINITY;
			Mat4f rotationA = boxA.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
			Mat4f rotationB = boxB.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
			Vec3f[] axes = new Vec3f[] {
					rotationA.col0().xyz().normalized(),
					rotationA.col1().xyz().normalized(),
					rotationA.col2().xyz().normalized(),
					rotationB.col0().xyz().normalized(),
					rotationB.col1().xyz().normalized(),
					rotationB.col2().xyz().normalized()
			};
			for(Vec3f axis : axes) {
				Projection projectionA = boxA.project(axis);
				Projection projectionB = boxB.project(axis);
				if(!projectionA.overlaps(projectionB))
					return null;
				float axisDepth = projectionA.getOverlap(projectionB).length();
				if(axisDepth < depth) {
					depth = axisDepth;
					normal = axis;
				}
			}
			if(boxA.meanCenter().minus(boxB.meanCenter()).dot(normal) < 0.0f) {
				normal = normal.negated();
			}
			return new Collision3D(with, normal, depth);
		})).filter(Optional::isPresent).map(Optional::get);
	}

	/**
	 * Handles a collision happened after a movement.
	 * Called by {@link CollisionObject3D#moveAndCollide(Vec3f)} when a collision happens.
	 *
	 * @param collision The collision data
	 */
	protected void onCollision(Collision3D collision) {

	}
}
