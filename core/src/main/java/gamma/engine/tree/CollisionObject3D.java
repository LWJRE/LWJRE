package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.physics.PhysicsSystem;
import gamma.engine.physics.Projection;
import gamma.engine.rendering.CubeMesh;
import gamma.engine.rendering.DebugRenderer;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.hexagonnico.vecmatlib.vector.Vec4f;

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

	@Override
	protected void onEnter() {
		PhysicsSystem.add(this);
		super.onEnter();
	}

	@Override
	protected void onEditorProcess() {
		DebugRenderer.addToBatch(CubeMesh.INSTANCE, mesh -> {
			Mat4f shape = this.globalTransformation().multiply(Mat4f.scaling(this.boundingBox));
			DebugRenderer.SHADER.setUniform("transformation_matrix", shape);
			DebugRenderer.SHADER.setUniform("color", 0.0f, 0.5f, 1.0f, 1.0f);
			mesh.drawElements();
		});
		super.onEditorProcess();
	}

	@Override
	protected void onExit() {
		PhysicsSystem.remove(this);
		super.onExit();
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
		Mat4f transform = this.globalTransformation();
		return List.of(
				transform.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(-halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz(),
				transform.multiply(new Vec4f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz()
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
	 * @param collider The {@code CollisionObject3D} that collided with this one
	 * @param normal The collision's normal
	 * @param depth Penetration depth
	 */
	public void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {

	}
}
