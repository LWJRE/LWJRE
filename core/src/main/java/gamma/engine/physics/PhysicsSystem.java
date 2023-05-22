package gamma.engine.physics;

import gamma.engine.tree.CollisionObject3D;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Static class that handles collision resolution between all the objects in the scene.
 *
 * @author Nico
 */
public final class PhysicsSystem {

	/** Number of partitions in which each dimension of the space is divided */
	public static final int PARTITIONS = 4; // TODO: Add this as an option in application.properties
	// TODO: Collisions become inaccurate for more than 4 partitions and only 2 objects in the scene

	/** List of all the colliders in the scene */
	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();
	/** Space partitions */
	private static final SpacePartition[][][] SPACE_PARTITIONS = Stream.generate(() -> Stream.generate(() -> Stream.generate(SpacePartition::new).limit(PARTITIONS).toArray(SpacePartition[]::new)).limit(PARTITIONS).toArray(SpacePartition[][]::new)).limit(PARTITIONS).toArray(SpacePartition[][][]::new);

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
	 * Distributes the collider in different {@link SpacePartition}s to improve the efficiency of collision detection.
	 */
	public static void update() {
		// Find max and min of space
		float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY, minZ = Float.POSITIVE_INFINITY;
		float maxX = Float.NEGATIVE_INFINITY, maxY = Float.NEGATIVE_INFINITY, maxZ = Float.NEGATIVE_INFINITY;
		for(CollisionObject3D collider : COLLIDERS) {
			for(Vec3f vertex : collider.getVertices()) {
				if(vertex.x() < minX) minX = vertex.x();
				if(vertex.y() < minY) minY = vertex.y();
				if(vertex.z() < minZ) minZ = vertex.z();
				if(vertex.x() > maxX) maxX = vertex.x();
				if(vertex.y() > maxY) maxY = vertex.y();
				if(vertex.z() > maxZ) maxZ = vertex.z();
			}
		}
		// Clear previous partition
		for(SpacePartition[][] spacePartition : SPACE_PARTITIONS) {
			for(SpacePartition[] spacePartitions : spacePartition) {
				for(SpacePartition partition : spacePartitions) {
					partition.clear();
				}
			}
		}
		// Divide the colliders into the partitions
		for(CollisionObject3D collider : COLLIDERS) {
			for(Vec3f vertex : collider.getVertices()) {
				int x = (int) ((vertex.x() - minX) / ((maxX - minX) / PARTITIONS));
				int y = (int) ((vertex.y() - minY) / ((maxY - minY) / PARTITIONS));
				int z = (int) ((vertex.z() - minZ) / ((maxZ - minZ) / PARTITIONS));
				SPACE_PARTITIONS[Math.min(x, PARTITIONS - 1)][Math.min(y, PARTITIONS - 1)][Math.min(z, PARTITIONS - 1)].add(collider);
			}
		}
	}

	/**
	 * Handles the collision resolution of the given object.
	 * Must be called every frame when a collider moves.
	 *
	 * @param collider The collider that moved
	 */
	public static void resolveCollision(CollisionObject3D collider) {
		for(SpacePartition[][] spacePartition : SPACE_PARTITIONS) {
			for(SpacePartition[] spacePartitions : spacePartition) {
				for(SpacePartition partition : spacePartitions) {
					partition.resolveCollision(collider);
				}
			}
		}
	}
}
