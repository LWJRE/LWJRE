package gamma.engine.physics.components;

import gamma.engine.core.components.Transform3D;
import gamma.engine.core.scene.Component;
import gamma.engine.physics.Collision3D;
import gamma.engine.physics.Projection;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.Optional;

public class CollisionObject3D extends Component {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	@Override
	protected void onStart() {
		super.onStart();
		COLLIDERS.add(this);
	}

	public final void moveAndCollide(Vec3f movement) {
		if(movement.lengthSquared() > 0.0f) {
			this.getComponent(Transform3D.class).ifPresent(transform -> {
				transform.position = transform.position.plus(movement);
				COLLIDERS.stream().filter(collider -> !collider.equals(this))
						.map(this::computeCollision)
						.filter(Optional::isPresent)
						.map(Optional::get)
						.forEach(this::onCollision);
			});
		}
	}

	private Optional<Collision3D> computeCollision(CollisionObject3D with) {
		return this.getComponent(BoundingBox3D.class).map(boxA -> with.getComponent(BoundingBox3D.class).map(boxB -> {
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
					return null;
				float axisDepth = projectionA.getOverlap(projectionB).length();
				if(axisDepth < depth) {
					depth = axisDepth;
					normal = axis;
				}
			}
			if(boxA.meanCenter().minus(boxB.meanCenter()).dot(normal) < 0.0f) {
				normal = normal.negated();
			}
			return new Collision3D(with, normal, depth);
		})).filter(Optional::isPresent).map(Optional::get);
	}

	protected void onCollision(Collision3D collision) {

	}
}
