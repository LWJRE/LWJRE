package gamma.engine.physics;

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
				CollisionObject3D colliderA = this.colliders.get(i);
				CollisionObject3D colliderB = this.colliders.get(j);
				// TODO: Check that they are not both static
				Projection xA = colliderA.projectBoundingBox(Vec3f.Right());
				Projection yA = colliderA.projectBoundingBox(Vec3f.Up());
				Projection zA = colliderA.projectBoundingBox(Vec3f.Forward());
				Projection xB = colliderB.projectBoundingBox(Vec3f.Right());
				Projection yB = colliderB.projectBoundingBox(Vec3f.Up());
				Projection zB = colliderB.projectBoundingBox(Vec3f.Forward());
				if(xA.overlaps(xB) && yA.overlaps(yB) && zA.overlaps(zB)) {
					this.collisionPairs.add(new CollisionPair(this.colliders.get(i), this.colliders.get(j)));
				}
			}
		}
	}

	private void narrowPhase() {
		this.collisionPairs.forEach(CollisionPair::resolveCollision);
		this.collisionPairs.clear();
	}

	private record CollisionPair(CollisionObject3D colliderA, CollisionObject3D colliderB) {

		private void resolveCollision() {
			this.colliderA().resolveCollision(this.colliderB());
		}
	}
}
