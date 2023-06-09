package gamma.engine.core.servers;

import gamma.engine.core.nodes.CollisionObject3D;
import io.github.hexagonnico.vecmatlib.Float2;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

import java.util.*;

/**
 * Static class that handles collision resolution between all the objects in the scene.
 *
 * @author Nico
 */
public final class PhysicsServer implements EngineServer {

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

	@Override
	public void init() {

	}

	@Override
	public void update() {
		HashMap<CollisionObject3D, HashSet<CollisionObject3D>> collisionPairs = new HashMap<>();
		subdivide(COLLIDERS, 2).forEach(partition -> computePairs(partition).forEach((colliderA, colliders) -> {
			if(collisionPairs.containsKey(colliderA)) {
				collisionPairs.get(colliderA).addAll(colliders);
			} else {
				collisionPairs.put(colliderA, colliders);
			}
		}));
		collisionPairs.forEach((colliderA, colliders) -> colliders.forEach(colliderB -> resolveCollision(colliderA, colliderB)));
	}

	@Override
	public void terminate() {

	}

	private static List<HashSet<CollisionObject3D>> subdivide(Collection<CollisionObject3D> colliders, int iterations) {
		HashSet<CollisionObject3D> partition = new HashSet<>(colliders);
		if(iterations > 1) {
			return subdivide(partition).stream().flatMap(subspace -> subdivide(subspace, iterations - 1).stream()).toList();
		} else {
			return subdivide(partition);
		}
	}

	private static List<HashSet<CollisionObject3D>> subdivide(Collection<CollisionObject3D> colliders) {
		List<HashSet<CollisionObject3D>> partitions = List.of(new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		Vec3f center = Vec3f.Zero();
		for(CollisionObject3D collider : colliders) {
			center = center.plus(collider.globalPosition());
		}
		center = center.dividedBy(colliders.size());
		for(CollisionObject3D collider : colliders) {
			for(Vec3f vertex : collider.getVertices()) {
				if(vertex.x() <= center.x() && vertex.y() <= center.y() && vertex.z() <= center.z())
					partitions.get(0).add(collider);
				if(vertex.x() >= center.x() && vertex.y() <= center.y() && vertex.z() <= center.z())
					partitions.get(1).add(collider);
				if(vertex.x() <= center.x() && vertex.y() >= center.y() && vertex.z() <= center.z())
					partitions.get(2).add(collider);
				if(vertex.x() >= center.x() && vertex.y() >= center.y() && vertex.z() <= center.z())
					partitions.get(3).add(collider);
				if(vertex.x() <= center.x() && vertex.y() <= center.y() && vertex.z() >= center.z())
					partitions.get(4).add(collider);
				if(vertex.x() >= center.x() && vertex.y() <= center.y() && vertex.z() >= center.z())
					partitions.get(5).add(collider);
				if(vertex.x() <= center.x() && vertex.y() >= center.y() && vertex.z() >= center.z())
					partitions.get(6).add(collider);
				if(vertex.x() >= center.x() && vertex.y() >= center.y() && vertex.z() >= center.z())
					partitions.get(7).add(collider);
			}
		}
		return partitions;
	}

	private static HashMap<CollisionObject3D, HashSet<CollisionObject3D>> computePairs(Collection<CollisionObject3D> colliders) {
		HashMap<CollisionObject3D, HashSet<CollisionObject3D>> result = new HashMap<>();
		colliders.forEach(colliderA -> {
			Vec3f centerA = colliderA.globalPosition();
			float radiusA = (float) colliderA.boundingBox.dividedBy(2.0f).multiply(colliderA.globalScale()).length();
			colliders.forEach(colliderB -> {
				if(!colliderA.equals(colliderB)) {
					Vec3f centerB = colliderB.globalPosition();
					float radiusB = (float) colliderB.boundingBox.dividedBy(2.0f).multiply(colliderB.globalScale()).length();
					if(centerA.distanceTo(centerB) <= radiusA + radiusB) {
						if(result.containsKey(colliderA)) {
							result.get(colliderA).add(colliderB);
						} else {
							HashSet<CollisionObject3D> paired = new HashSet<>();
							paired.add(colliderB);
							result.put(colliderA, paired);
						}
					}
				}
			});
		});
		return result;
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
			Float2 projectionA = colliderA.projectBoundingBox(axis);
			Float2 projectionB = colliderB.projectBoundingBox(axis);
			if(!overlaps(projectionA, projectionB)) {
				return;
			}
			float axisDepth = getOverlap(projectionA, projectionB);
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

	private static boolean overlaps(Float2 projectionA, Float2 projectionB) {
		return overlaps(projectionA.x(), projectionA.y(), projectionB.x(), projectionB.y());
	}

	private static boolean overlaps(float minA, float maxA, float minB, float maxB) {
		return minA <= maxB && maxA >= minB;
	}

	private static float getOverlap(Float2 projectionA, Float2 projectionB) {
		return getOverlap(projectionA.x(), projectionA.y(), projectionB.x(), projectionB.y());
	}

	private static float getOverlap(float minA, float maxA, float minB, float maxB) {
		return Math.min(maxA, maxB) - Math.max(minA, minB);
	}
}
