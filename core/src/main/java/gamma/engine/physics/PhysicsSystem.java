package gamma.engine.physics;

import gamma.engine.components.BoundingBox3D;
import gamma.engine.components.CollisionObject3D;
import vecmatlib.vector.Vec2i;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;

public class PhysicsSystem {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();
	private static final ArrayList<Vec2i> COLLISION_PAIRS = new ArrayList<>();

	public static void add(CollisionObject3D collider) {
		COLLIDERS.add(collider);
	}

	public static void remove(CollisionObject3D collider) {
		COLLIDERS.remove(collider);
	}

	public static void physicsStep() {
		long time = System.nanoTime();
		sweepAndPrune();
		long delta = System.nanoTime() - time;
		time = System.nanoTime();
		System.out.println("Broad phase " + (delta / 1_000_000_000.0f));
		narrowPhase();
		delta = System.nanoTime() - time;
		System.out.println("Narrow phase " + (delta / 1_000_000_000.0f));
	}

	private static void sweepAndPrune() {
		for(int i = 0; i < COLLIDERS.size() - 1; i++) {
			for(int j= i + 1; j < COLLIDERS.size(); j++) {
				// TODO: Check if they are not both static
				int finalI = i, finalJ = j; // TODO: Allow easier access to bounding boxes
				COLLIDERS.get(i).getComponent(BoundingBox3D.class).ifPresent(boxA -> COLLIDERS.get(finalJ).getComponent(BoundingBox3D.class).ifPresent(boxB -> {
					Projection xA = boxA.project(Vec3f.Right());
					Projection yA = boxA.project(Vec3f.Up());
					Projection zA = boxA.project(Vec3f.Forward());
					Projection xB = boxB.project(Vec3f.Right());
					Projection yB = boxB.project(Vec3f.Up());
					Projection zB = boxB.project(Vec3f.Forward());
					if(xA.overlaps(xB) && yA.overlaps(yB) && zA.overlaps(zB)) {
						COLLISION_PAIRS.add(new Vec2i(finalI, finalJ));
					}
				}));
			}
		}
	}

	private static void narrowPhase() {
		for(Vec2i pair : COLLISION_PAIRS) {
			CollisionObject3D colliderA = COLLIDERS.get(pair.x());
			CollisionObject3D colliderB = COLLIDERS.get(pair.y());
			colliderA.resolveCollision(colliderB);
		}
		COLLISION_PAIRS.clear();
	}
}
