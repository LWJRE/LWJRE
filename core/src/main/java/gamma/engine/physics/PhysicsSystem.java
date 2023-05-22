package gamma.engine.physics;

import gamma.engine.tree.CollisionObject3D;
import gamma.engine.tree.Node3D;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PhysicsSystem {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	public static void add(CollisionObject3D collider) {
		COLLIDERS.add(collider);
	}

	public static void remove(CollisionObject3D collider) {
		COLLIDERS.add(collider);
	}

	public static void update() {
		// TODO: Add option to log time in ApplicationSettings
//		long time = System.nanoTime();
		narrowPhase(sweepAndPrune(spatialSubdivision()));
//		time = System.nanoTime() - time;
//		System.out.println("Time: " + (time / 1_000_000_000.0) + " seconds");
	}

	// TODO: This acts differently if the colliders are in a different order

	private static List<ArrayList<CollisionObject3D>> spatialSubdivision() {
		Vec3f center = COLLIDERS.stream().map(Node3D::globalPosition).reduce(Vec3f.Zero(), Vec3f::plus).dividedBy(COLLIDERS.size());
		List<ArrayList<CollisionObject3D>> partitions = List.of(
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>(),
				new ArrayList<>()
		);
		for(CollisionObject3D collider : COLLIDERS) {
			List<Vec3f> vertices = collider.getVertices();
			if(vertices.stream().anyMatch(vertex -> vertex.x() > center.x() && vertex.y() > center.y() && vertex.z() > center.z())) {
				partitions.get(0).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() < center.x() && vertex.y() > center.y() && vertex.z() > center.z())) {
				partitions.get(1).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() > center.x() && vertex.y() < center.y() && vertex.z() > center.z())) {
				partitions.get(2).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() > center.x() && vertex.y() > center.y() && vertex.z() < center.z())) {
				partitions.get(3).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() < center.x() && vertex.y() < center.y() && vertex.z() > center.z())) {
				partitions.get(4).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() < center.x() && vertex.y() > center.y() && vertex.z() < center.z())) {
				partitions.get(5).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() > center.x() && vertex.y() < center.y() && vertex.z() < center.z())) {
				partitions.get(6).add(collider);
			}
			if(vertices.stream().anyMatch(vertex -> vertex.x() < center.x() && vertex.y() < center.y() && vertex.z() < center.z())) {
				partitions.get(7).add(collider);
			}
		}
		return partitions;
	}

	private static HashSet<CollisionPair> sweepAndPrune(List<ArrayList<CollisionObject3D>> partitions) {
		HashSet<CollisionPair> collisionPairs = new HashSet<>();
		partitions.forEach(partition -> {
			for(int i = 0; i < partition.size() - 1; i++) {
				for(int j = i + 1; j < partition.size(); j++) {
					CollisionObject3D colliderA = partition.get(i);
					CollisionObject3D colliderB = partition.get(j);
					// TODO: Check that they are not both static
					Projection xA = colliderA.projectBoundingBox(Vec3f.Right());
					Projection yA = colliderA.projectBoundingBox(Vec3f.Up());
					Projection zA = colliderA.projectBoundingBox(Vec3f.Forward());
					Projection xB = colliderB.projectBoundingBox(Vec3f.Right());
					Projection yB = colliderB.projectBoundingBox(Vec3f.Up());
					Projection zB = colliderB.projectBoundingBox(Vec3f.Forward());
					if(xA.overlaps(xB) && yA.overlaps(yB) && zA.overlaps(zB)) {
						collisionPairs.add(new CollisionPair(colliderA, colliderB));
					}
				}
			}
		});
		return collisionPairs;
	}

	private static void narrowPhase(Iterable<CollisionPair> collisionPairs) {
		collisionPairs.forEach(CollisionPair::resolveCollision);
	}

	private record CollisionPair(CollisionObject3D colliderA, CollisionObject3D colliderB) {

		private void resolveCollision() {
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
			this.colliderA().onCollision(new Collision3D(this.colliderB(), normal, depth));
		}
	}
}
