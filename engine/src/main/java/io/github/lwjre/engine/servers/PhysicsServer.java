package io.github.lwjre.engine.servers;

import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.lwjre.engine.nodes.CollisionObject3D;

import java.util.*;

/**
 * Static class that handles collision resolution between all the objects in the scene.
 *
 * @author Nico
 */
public final class PhysicsServer implements EngineServer {

	/** List of all the colliders in the scene */
	private static final HashMap<CollisionObject3D, HashSet<CollisionObject3D>> COLLISION_PAIRS = new HashMap<>();

	/**
	 * Adds a collider to the physics system to be used for collision detection.
	 * Called when a {@link CollisionObject3D} enters the scene.
	 *
	 * @param collider The collider to add
	 */
	public static void add(CollisionObject3D collider) {
		COLLISION_PAIRS.put(collider, new HashSet<>());
	}

	/**
	 * Removes a collider from the physics system.
	 * Called when a {@link CollisionObject3D} exits the scene.
	 *
	 * @param collider The collider to remove
	 */
	public static void remove(CollisionObject3D collider) {
		COLLISION_PAIRS.remove(collider);
	}

	public static boolean resolveCollision(CollisionObject3D collider) {
		if(COLLISION_PAIRS.containsKey(collider)) {
			COLLISION_PAIRS.get(collider).forEach(collider::resolveCollision);
			return true;
		}
		return false;
	}

	@Override
	public void init() {

	}

	@Override
	public void update() {
		COLLISION_PAIRS.values().forEach(HashSet::clear);
		subdivide(COLLISION_PAIRS.keySet(), 2).forEach(partition -> computePairs(partition).forEach((colliderA, colliders) -> colliders.forEach(colliderB -> {
			COLLISION_PAIRS.get(colliderA).add(colliderB);
			COLLISION_PAIRS.get(colliderB).add(colliderA);
		})));
	}

	@Override
	public void terminate() {

	}

	@Override
	public int priority() {
		return 2;
	}

	/**
	 * Divides the given colliders into several subspaces.
	 * The number of iteration indicates how many times the colliders should be divided into 8 subspaces.
	 *
	 * @param colliders The list of colliders
	 * @param iterations The number of iterations
	 * @return A {@link List} of subspaces, represented by {@link HashSet}s.
	 */
	private static List<HashSet<CollisionObject3D>> subdivide(Collection<CollisionObject3D> colliders, int iterations) {
		HashSet<CollisionObject3D> partition = new HashSet<>(colliders);
		if(iterations > 1) {
			return subdivide(partition).stream().flatMap(subspace -> subdivide(subspace, iterations - 1).stream()).toList();
		} else {
			return subdivide(partition);
		}
	}

	/**
	 * Divides the given colliders into 8 subspaces based on their position.
	 *
	 * @param colliders The list of colliders
	 * @return A {@link List} containing 8 subspaces, represented by {@link HashSet}s.
	 */
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

	/**
	 * Pairs up the given colliders according to which of them might collide.
	 * Represents the broad phase of the collision detection algorithm.
	 * Checks if the bounding spheres of two colliders overlap.
	 *
	 * @param colliders The list of colliders
	 * @return A {@link HashMap} whose keys are the given colliders and whose values are {@link HashSet}s of colliders that may collide with the one used as key
	 */
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
}
