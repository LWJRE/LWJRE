package gamma.engine.physics;

import gamma.engine.components.BoundingBox3D;
import gamma.engine.components.CollisionObject3D;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.LinkedList;

public class SweepAndPrune {

	private final ArrayList<CollisionObject3D> colliders = new ArrayList<>();
	private final LinkedList<CollisionPair> collisionPairs = new LinkedList<>();

	public void add(CollisionObject3D collider) {
		this.colliders.add(collider);
	}

	public void remove(CollisionObject3D collider) {
		this.colliders.remove(collider);
	}

	public void resolveCollisions() {
		this.broadPhase();
		this.narrowPhase();
	}

	private void broadPhase() {
		for(int i = 0; i < this.colliders.size() - 1; i++) {
			for(int j= i + 1; j < this.colliders.size(); j++) {
				// TODO: Check if they are not both static
				int finalI = i, finalJ = j; // TODO: Allow easier access to bounding boxes
				this.colliders.get(i).getComponent(BoundingBox3D.class).ifPresent(boxA -> this.colliders.get(finalJ).getComponent(BoundingBox3D.class).ifPresent(boxB -> {
					Projection xA = boxA.project(Vec3f.Right());
					Projection yA = boxA.project(Vec3f.Up());
					Projection zA = boxA.project(Vec3f.Forward());
					Projection xB = boxB.project(Vec3f.Right());
					Projection yB = boxB.project(Vec3f.Up());
					Projection zB = boxB.project(Vec3f.Forward());
					if(xA.overlaps(xB) && yA.overlaps(yB) && zA.overlaps(zB)) {
						this.collisionPairs.add(new CollisionPair(this.colliders.get(finalI), this.colliders.get(finalJ)));
					}
				}));
			}
		}
	}

	private void narrowPhase() {
		this.collisionPairs.forEach(pair -> pair.colliderA().resolveCollision(pair.colliderB()));
		this.collisionPairs.clear();
	}

	private record CollisionPair(CollisionObject3D colliderA, CollisionObject3D colliderB) {

	}
}
