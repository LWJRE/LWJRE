package gamma.engine.components;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import gamma.engine.physics.PhysicsSystem;
import gamma.engine.physics.Projection;
import gamma.engine.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.util.List;

public class CollisionObject3D extends Component {

	@EditorVariable
	@EditorRange
	public Vec3f boundingBox = Vec3f.One();

	@EditorVariable
	@EditorRange
	public Vec3f offset = Vec3f.Zero();

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		PhysicsSystem.remove(this);
		PhysicsSystem.add(this);
	}

	public final void resolveCollision(CollisionObject3D that) {
		Vec3f normal = Vec3f.Zero();
		float depth = Float.POSITIVE_INFINITY;
		Mat4f rotationA = this.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
		Mat4f rotationB = that.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
		Vec3f[] axes = new Vec3f[] {
				rotationA.col0().xyz().normalized(),
				rotationA.col1().xyz().normalized(),
				rotationA.col2().xyz().normalized(),
				rotationB.col0().xyz().normalized(),
				rotationB.col1().xyz().normalized(),
				rotationB.col2().xyz().normalized()
		};
		for(Vec3f axis : axes) {
			Projection projectionA = this.projectBoundingBox(axis);
			Projection projectionB = that.projectBoundingBox(axis);
			if(!projectionA.overlaps(projectionB)) {
				return;
			}
			float axisDepth = projectionA.getOverlap(projectionB).length();
			if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		if(this.meanCenter().minus(that.meanCenter()).dot(normal) < 0.0f) {
			normal = normal.negated();
		}
		this.onCollision(new Collision3D(that, normal, depth));
	}

	public final Projection projectBoundingBox(Vec3f axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vec3f vertex : this.getVertices()) {
			float projection = vertex.dot(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(min, max);
	}

	public final List<Vec3f> getVertices() {
		Vec3f halfExtents = this.boundingBox.dividedBy(2.0f);
		return this.getComponent(Transform3D.class).map(transform -> {
			Vec3f origin = transform.globalPosition().plus(this.offset);
			Mat4f rotation = transform.globalRotation();
			Vec3f scale = transform.globalScale();
			return List.of(
					origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
					origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale))
			);
		}).orElseGet(() -> List.of(
				new Vec3f(halfExtents.x(), halfExtents.y(), halfExtents.z()),
				new Vec3f(-halfExtents.x(), halfExtents.y(), halfExtents.z()),
				new Vec3f(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
				new Vec3f(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
				new Vec3f(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),
				new Vec3f(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
				new Vec3f(-halfExtents.x(), halfExtents.y(), -halfExtents.z()),
				new Vec3f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z())
		));
	}

	public final Vec3f meanCenter() {
		return this.getVertices().stream().reduce(Vec3f.Zero(), Vec3f::plus).dividedBy(8);
	}

	protected void onCollision(Collision3D collision) {

	}
}
