package gamma.engine.components;

import gamma.engine.physics.Collision3D;
import gamma.engine.physics.PhysicsSystem;
import gamma.engine.physics.Projection;
import gamma.engine.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

import java.util.HashSet;
import java.util.Optional;

/**
 * Controls an entity's collisions while moving.
 * The entity needs a {@link BoundingBox3D} component to collide with other objects.
 *
 * @see BoundingBox3D
 *
 * @author Nico
 */
public class CollisionObject3D extends Component {

	@Override
	protected void onStart() {
		super.onStart();
		PhysicsSystem.add(this);
	}

	@Override
	protected void onExit() {
		super.onExit();
		PhysicsSystem.remove(this);
	}

	public final void resolveCollision(CollisionObject3D with) {
		this.getComponent(BoundingBox3D.class).ifPresent(boxA -> with.getComponent(BoundingBox3D.class).ifPresent(boxB -> {
			Vec3f normal = Vec3f.Zero();
			float depth = Float.POSITIVE_INFINITY;
			Mat4f rotationA = boxA.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
			Mat4f rotationB = boxB.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
			Vec3f[] axes = new Vec3f[] {
					rotationA.col0().xyz().normalized(),
					rotationA.col1().xyz().normalized(),
					rotationA.col2().xyz().normalized(),
					rotationB.col0().xyz().normalized(),
					rotationB.col1().xyz().normalized(),
					rotationB.col2().xyz().normalized()
			};
			for(Vec3f axis : axes) {
				Projection projectionA = boxA.project(axis);
				Projection projectionB = boxB.project(axis);
				if(!projectionA.overlaps(projectionB))
					return;
				float axisDepth = projectionA.getOverlap(projectionB).length();
				if(axisDepth < depth) {
					depth = axisDepth;
					normal = axis;
				}
			}
			if(boxA.meanCenter().minus(boxB.meanCenter()).dot(normal) < 0.0f) {
				normal = normal.negated();
			}
			this.onCollision(new Collision3D(with, normal, depth));
		}));
	}

	/**
	 * Handles a collision happened after a movement.
	 * Called by {@link CollisionObject3D#moveAndCollide(Vec3f)} when a collision happens.
	 *
	 * @param collision The collision data
	 */
	protected void onCollision(Collision3D collision) {

	}
}
