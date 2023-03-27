package gamma.engine.core.components;

import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.physics.Projection;
import gamma.engine.core.rendering.DebugRenderer;
import gamma.engine.core.scene.Component;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

/**
 * An oriented bounding box used by {@link CollisionObject3D} to detect collisions.
 * The entity needs a {@link Transform3D} component to determine the bounding box's origin.
 * If the entity has no transform, the bounding box's origin will be the world's origin.
 *
 * @see CollisionObject3D
 *
 * @author Nico
 */
public class BoundingBox3D extends Component {

	/** The bounding box's offset from the origin */
	@EditorVariable("Offset")
	@EditorRange
	public Vec3f offset = Vec3f.Zero();
	/** The bounding box's extents */
	@EditorVariable("Extents")
	@EditorRange
	public Vec3f extents = Vec3f.One();

	@Override
	protected void editorUpdate() {
		super.editorUpdate();
		this.getComponent(Transform3D.class).ifPresent(transform -> DebugRenderer.addToBatch(this, transform.globalTransformation()));
	}

	/**
	 * Projects this bounding box on the given axis.
	 *
	 * @param axis The axis on which to project this bounding box. Must be normalized.
	 * @return The {@link Projection} of this bounding box on the given axis
	 */
	public final Projection project(Vec3f axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vec3f vertex : this.getVertices()) {
			float projection = vertex.dot(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(min, max);
	}

	/**
	 * Computes the mean center of this shape.
	 * The mean center is in global coordinates if the entity has a {@link Transform3D} component,
	 * otherwise it is in local coordinates.
	 *
	 * @return The mean center of all the vertices of this shape
	 */
	public final Vec3f meanCenter() {
		Vec3f mean = Vec3f.Zero();
		for(Vec3f vertex : this.getVertices()) {
			mean = mean.plus(vertex);
		}
		return mean.dividedBy(8);
	}

	/**
	 * Gets all the vertices of this shape.
	 *
	 * @return An array of {@link Vec3f} containing all the shape's vertices
	 */
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
