package gamma.engine.physics;

import gamma.engine.tree.CollisionObject3D;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

import java.util.HashSet;

/**
 * Class that represents a section of the 3D space that contains {@link CollisionObject3D}s.
 * Used to resolve collision only for objects that are close by.
 *
 * @author Nico
 */
public class SpacePartition {

	/** Set of colliders in this partition */
	private final HashSet<CollisionObject3D> colliders = new HashSet<>();

	/**
	 * Adds a collider to this space partition if it is not already present.
	 *
	 * @param collider The collider to add
	 */
	public void add(CollisionObject3D collider) {
		this.colliders.add(collider);
	}

	/**
	 * Removes all the collider from this partition.
	 */
	public void clear() {
		this.colliders.clear();
	}

	/**
	 * Handles collisions for the given collider using the sweep and prune algorithm if it is in this partition.
	 * Called from {@link PhysicsSystem#resolveCollision(CollisionObject3D)}.
	 *
	 * @param collisionObject The collider
	 */
	public void resolveCollision(CollisionObject3D collisionObject) {
		if(this.colliders.contains(collisionObject)) {
			HashSet<CollisionPair> collisionPairs = new HashSet<>();
			// Broad phase
			this.colliders.forEach(collider -> {
				if(!collider.equals(collisionObject)) {
					Projection xA = collisionObject.projectBoundingBox(Vec3f.Right());
					Projection yA = collisionObject.projectBoundingBox(Vec3f.Up());
					Projection zA = collisionObject.projectBoundingBox(Vec3f.Forward());
					Projection xB = collider.projectBoundingBox(Vec3f.Right());
					Projection yB = collider.projectBoundingBox(Vec3f.Up());
					Projection zB = collider.projectBoundingBox(Vec3f.Forward());
					if(xA.overlaps(xB) && yA.overlaps(yB) && zA.overlaps(zB)) {
						collisionPairs.add(new CollisionPair(collisionObject, collider));
					}
				}
			});
			// Narrow phase
			collisionPairs.forEach(CollisionPair::resolveCollision);
		}
	}

	/**
	 * Collision pair used to group two colliders that may collide.
	 * Needed to separate the narrow phase from the broad phase.
	 *
	 * @param colliderA First collider
	 * @param colliderB Second collider
	 */
	private record CollisionPair(CollisionObject3D colliderA, CollisionObject3D colliderB) {

		public void resolveCollision() {
			Vec3f normal = Vec3f.Zero();
			float depth = Float.POSITIVE_INFINITY;
			Mat4f rotationA = this.colliderA().globalRotation();
			Mat4f rotationB = this.colliderB().globalRotation();
			Vec3f[] axes = new Vec3f[] {
					rotationA.col0().xyz().normalized(),
					rotationA.col1().xyz().normalized(),
					rotationA.col2().xyz().normalized(),
					rotationB.col0().xyz().normalized(),
					rotationB.col1().xyz().normalized(),
					rotationB.col2().xyz().normalized()
			};
			for(Vec3f axis : axes) {
				Projection projectionA = this.colliderA().projectBoundingBox(axis);
				Projection projectionB = this.colliderB().projectBoundingBox(axis);
				if(!projectionA.overlaps(projectionB)) {
					return;
				}
				float axisDepth = projectionA.getOverlap(projectionB).length();
				if(axisDepth < depth) {
					depth = axisDepth;
					normal = axis;
				}
			}
			if(this.colliderA().meanCenter().minus(this.colliderB().meanCenter()).dot(normal) < 0.0f) {
				normal = normal.negated();
			}
			this.colliderA().onCollision(this.colliderB(), normal, depth);
		}
	}
}
