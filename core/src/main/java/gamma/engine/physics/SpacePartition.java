package gamma.engine.physics;

import gamma.engine.tree.CollisionObject3D;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

import java.util.*;
import java.util.stream.Stream;

/**
 * Class that represents a section of the 3D space that contains {@link CollisionObject3D}s.
 * Uses octree partitioning.
 *
 * @author Nico
 */
public class SpacePartition {

	/**
	 * Subdivides the given colliders into a list of space partitions.
	 * The given number of iterations represents the height of the octree.
	 *
	 * @param colliders The colliders to subdivide
	 * @param iterations The number of iteration for the space partition algorithm or the height of the resulting octree
	 * @return A list containing all the resulting partitions in no order
	 */
	public static List<SpacePartition> subdivide(Collection<CollisionObject3D> colliders, int iterations) {
		SpacePartition space = new SpacePartition();
		space.colliders.addAll(colliders);
		if(iterations > 1) {
			return space.divideFurther().stream().flatMap(partition -> subdivide(partition.colliders, iterations - 1).stream()).toList();
		} else {
			return space.divideFurther();
		}
	}

	/** Set of colliders in this partition */
	private final HashSet<CollisionObject3D> colliders;

	/**
	 * Creates an empty space partition.
	 */
	private SpacePartition() {
		this.colliders = new HashSet<>();
	}

	/**
	 * Divides this space partition into 8 smaller ones.
	 *
	 * @return A list containing 8 space partitions
	 */
	public List<SpacePartition> divideFurther() {
		List<SpacePartition> result = Stream.generate(SpacePartition::new).limit(8).toList();
		Vec3f center = Vec3f.Zero();
		for(CollisionObject3D collider : colliders) {
			center = center.plus(collider.globalPosition());
		}
		center = center.dividedBy(colliders.size());
		for(CollisionObject3D collider : colliders) {
			for(Vec3f vertex : collider.getVertices()) {
				if(vertex.x() <= center.x() && vertex.y() <= center.y() && vertex.z() <= center.z())
					result.get(0).colliders.add(collider);
				if(vertex.x() >= center.x() && vertex.y() <= center.y() && vertex.z() <= center.z())
					result.get(1).colliders.add(collider);
				if(vertex.x() <= center.x() && vertex.y() >= center.y() && vertex.z() <= center.z())
					result.get(2).colliders.add(collider);
				if(vertex.x() >= center.x() && vertex.y() >= center.y() && vertex.z() <= center.z())
					result.get(3).colliders.add(collider);
				if(vertex.x() <= center.x() && vertex.y() <= center.y() && vertex.z() >= center.z())
					result.get(4).colliders.add(collider);
				if(vertex.x() >= center.x() && vertex.y() <= center.y() && vertex.z() >= center.z())
					result.get(5).colliders.add(collider);
				if(vertex.x() <= center.x() && vertex.y() >= center.y() && vertex.z() >= center.z())
					result.get(6).colliders.add(collider);
				if(vertex.x() >= center.x() && vertex.y() >= center.y() && vertex.z() >= center.z())
					result.get(7).colliders.add(collider);
			}
		}
		return result;
	}

	/**
	 * Groups all the colliders in this partition into collision pairs, pairs of colliders that may be colliding.
	 * Represents the broad phase of the collision resolution algorithm.
	 *
	 * @return A {@code HashMap} whose keys are the colliders in this partition and whose values are {@code HashSet}s of the colliders that may collide with them
	 */
	public HashMap<CollisionObject3D, HashSet<CollisionObject3D>> computePairs() {
		HashMap<CollisionObject3D, HashSet<CollisionObject3D>> result = new HashMap<>();
		this.colliders.forEach(colliderA -> {
			Vec3f centerA = colliderA.globalPosition();
			float radiusA = colliderA.boundingBox.dividedBy(2.0f).lengthSquared();
			this.colliders.forEach(colliderB -> {
				if(!colliderA.equals(colliderB)) {
					Vec3f centerB = colliderB.globalPosition();
					float radiusB = colliderB.boundingBox.dividedBy(2.0f).lengthSquared();
					if(centerA.distanceSquaredTo(centerB) <= radiusA + radiusB) {
						if(result.containsKey(colliderA)) {
							result.get(colliderA).add(colliderB);
						} else {
							HashSet<CollisionObject3D> colliders = new HashSet<>();
							colliders.add(colliderB);
							result.put(colliderA, colliders);
						}
					}
				}
			});
		});
		return result;
	}
}
