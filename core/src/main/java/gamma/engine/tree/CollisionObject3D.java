package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.Collision3D;
import gamma.engine.physics.PhysicsSystem;
import gamma.engine.physics.Projection;
import gamma.engine.rendering.CubeMesh;
import gamma.engine.rendering.DebugRenderer;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.util.List;

/**
 * Node that represents a 3D object with a collision box.
 * Base class for all physic bodies.
 *
 * @author Nico
 */
public class CollisionObject3D extends Node3D {

	/**
	 * Extents of this object's bounding box.
	 */
	@EditorVariable(name = "Bounding box")
	@EditorRange
	public Vec3f boundingBox = Vec3f.One();

	/**
	 * Offset of the bounding box from the object's origin.
	 */
	@EditorVariable(name = "Offset")
	@EditorRange
	public Vec3f offset = Vec3f.Zero();

	@Override
	protected void onEnter() {
		super.onEnter();
		PhysicsSystem.add(this);
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		DebugRenderer.addToBatch(CubeMesh.INSTANCE, mesh -> {
			Mat4f shape = Mat4f.translation(this.offset).multiply(Mat4f.scaling(this.boundingBox));
			DebugRenderer.SHADER.setUniform("transformation_matrix", this.globalTransformation().multiply(shape));
			DebugRenderer.SHADER.setUniform("color", 0.0f, 0.5f, 1.0f, 1.0f);
			mesh.drawElements();
		});
	}

	@Override
	protected void onExit() {
		super.onExit();
		PhysicsSystem.remove(this);
	}

	/**
	 * Computes the {@link Projection} of this object's bounding box on the given (arbitrary) axis.
	 * Used to resolve collision in the {@link PhysicsSystem}.
	 *
	 * @param axis Axis on which the box should be projected (must be normalized)
	 *
	 * @return The projection of the object's bounding box on the given axis
	 */
	public Projection projectBoundingBox(Vec3f axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vec3f vertex : this.getVertices()) {
			float projection = vertex.dot(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(min, max);
	}

	/**
	 * Computes the global position of the vertices of the object's bounding box and returns them in a {@link List}.
	 * Used to resolve collision in the {@link PhysicsSystem}.
	 *
	 * @return A list containing the global position of the 8 vertices of the object's bounding box
	 */
	public List<Vec3f> getVertices() {
		Vec3f halfExtents = this.boundingBox.dividedBy(2.0f);
		Vec3f origin = this.globalPosition().plus(this.offset);
		Mat4f rotation = this.globalRotation();
		Vec3f scale = this.globalScale();
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
	}

	/**
	 * Computes the mean center of the vertices returned by {@link CollisionObject3D#getVertices()}.
	 * Used to resolve collision in the {@link PhysicsSystem}.
	 *
	 * @return The object's mean center
	 */
	public Vec3f meanCenter() {
		return this.getVertices().stream().reduce(Vec3f.Zero(), Vec3f::plus).dividedBy(8);
	}

	/**
	 * Called when this object collides with another one.
	 * Classes that extend {@code CollisionObject3D} must override this method to implement their collision resolution or to listen for collision events.
	 *
	 * @param collision Object representing the collision that happened
	 */
	public void onCollision(Collision3D collision) {

	}
}
