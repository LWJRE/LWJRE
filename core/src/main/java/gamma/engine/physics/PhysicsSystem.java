package gamma.engine.physics;

import gamma.engine.tree.CollisionObject3D;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Static class that handles collision resolution between all the objects in the scene.
 *
 * @author Nico
 */
public final class PhysicsSystem {

	/** List of all the colliders in the scene */
	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	/**
	 * Adds a collider to the physics system to be used for collision detection.
	 * Called when a {@link CollisionObject3D} enters the scene.
	 *
	 * @param collider The collider to add
	 */
	public static void add(CollisionObject3D collider) {
		COLLIDERS.add(collider);
	}

	/**
	 * Removes a collider from the physics system.
	 * Called when a {@link CollisionObject3D} exits the scene.
	 *
	 * @param collider The collider to remove
	 */
	public static void remove(CollisionObject3D collider) {
		COLLIDERS.add(collider);
	}

	/**
	 * Updates the physics system.
	 * Called from the {@link gamma.engine.Application} main loop.
	 * Resolves collisions for all the colliders in the scene.
	 */
	public static void update() {
		HashMap<CollisionObject3D, HashSet<CollisionObject3D>> collisionPairs = new HashMap<>();
		SpacePartition.subdivide(COLLIDERS, 2).forEach(partition -> partition.computePairs().forEach((colliderA, colliders) -> {
			if(collisionPairs.containsKey(colliderA)) {
				collisionPairs.get(colliderA).addAll(colliders);
			} else {
				collisionPairs.put(colliderA, colliders);
			}
		}));
		collisionPairs.forEach((colliderA, colliders) -> colliders.forEach(colliderB -> resolveCollision(colliderA, colliderB)));
	}

	/**
	 * Checks if the two given colliders are colliding and calls {@link CollisionObject3D#onCollision(CollisionObject3D, Vec3f, float)} if they are.
	 * Represents the narrow phase of the collision resolution algorithm.
	 *
	 * @param colliderA First collider
	 * @param colliderB Second collider
	 */
	private static void resolveCollision(CollisionObject3D colliderA, CollisionObject3D colliderB) {
		Vec3f normal = Vec3f.Zero();
		float depth = Float.POSITIVE_INFINITY;
		Mat4f rotationA = colliderA.globalRotation();
		Mat4f rotationB = colliderB.globalRotation();
		Vec3f[] axes = new Vec3f[] {
				rotationA.col0().xyz().normalized(),
				rotationA.col1().xyz().normalized(),
				rotationA.col2().xyz().normalized(),
				rotationB.col0().xyz().normalized(),
				rotationB.col1().xyz().normalized(),
				rotationB.col2().xyz().normalized()
		};
		for(Vec3f axis : axes) {
			Projection projectionA = colliderA.projectBoundingBox(axis);
			Projection projectionB = colliderB.projectBoundingBox(axis);
			if(!projectionA.overlaps(projectionB)) {
				return;
			}
			float axisDepth = projectionA.getOverlap(projectionB).length();
			if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		if(colliderA.meanCenter().minus(colliderB.meanCenter()).dot(normal) < 0.0f) {
			normal = normal.negated();
		}
		colliderA.onCollision(colliderB, normal, depth);
	}
}
