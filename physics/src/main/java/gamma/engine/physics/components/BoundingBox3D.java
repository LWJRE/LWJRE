package gamma.engine.physics.components;

import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.components.Transform3D;
import gamma.engine.core.scene.Component;
import gamma.engine.physics.Projection;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

public class BoundingBox3D extends Component {

	@EditorVariable
	public Vec3f offset = Vec3f.Zero();
	@EditorVariable
	public Vec3f extents = Vec3f.One();

	public final Projection project(Vec3f axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vec3f vertex : this.getVertices()) {
			float projection = vertex.dot(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(min, max);
	}

	public final Vec3f meanCenter() {
		Vec3f mean = Vec3f.Zero();
		for(Vec3f vertex : this.getVertices()) {
			mean = mean.plus(vertex);
		}
		return mean.dividedBy(8);
	}

	private Vec3f[] getVertices() {
		Vec3f halfExtents = this.extents.dividedBy(2.0f);
		Vec3f origin = this.getComponent(Transform3D.class).map(Transform3D::globalPosition).orElse(Vec3f.Zero()).plus(this.offset);
		Mat4f rotation = this.getComponent(Transform3D.class).map(Transform3D::globalRotation).orElse(Mat4f.Identity());
		Vec3f scale = this.getComponent(Transform3D.class).map(Transform3D::globalScale).orElse(Vec3f.One());
		return new Vec3f[] {
				origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale))
		};
	}
}
