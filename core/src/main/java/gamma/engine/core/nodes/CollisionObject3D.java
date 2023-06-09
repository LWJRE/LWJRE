package gamma.engine.core.nodes;

import gamma.engine.core.annotations.EditorRange;
import gamma.engine.core.annotations.EditorVariable;
import gamma.engine.core.servers.PhysicsServer;
import io.github.hexagonnico.vecmatlib.Float2;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec2f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;

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
		PhysicsServer.add(this);
		super.onEnter();
	}

	@Override
	protected void onEditorProcess() {
//		DebugRenderer.addToBatch(CubeMesh.INSTANCE, mesh -> {
//			Mat4f shape = this.globalTransformation().multiply(Mat4f.scaling(this.boundingBox));
//			DebugRenderer.SHADER.setUniform("transformation_matrix", shape);
//			DebugRenderer.SHADER.setUniform("color", 0.0f, 0.5f, 1.0f, 1.0f);
//			mesh.drawElements();
//		});
		super.onEditorProcess();
	}

	@Override
	protected void onExit() {
		PhysicsServer.remove(this);
		super.onExit();
	}

	public Float2 projectBoundingBox(Vec3f axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vec3f vertex : this.getVertices()) {
			float projection = vertex.dot(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Vec2f(min, max);
	}

	/**
	 * Computes the global position of the vertices of the object's bounding box and returns them in a {@link List}.
	 * Used to resolve collision in the {@link PhysicsServer}.
	 *
	 * @return A list containing the global position of the 8 vertices of the object's bounding box
	 */
	public List<Vec3f> getVertices() {
		Vec3f halfExtents = this.boundingBox.dividedBy(2.0f);
		Mat4f transform = this.globalTransformation();
		return List.of(
				transform.multiply(-halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f).xyz(),
				transform.multiply(-halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f).xyz(),
				transform.multiply(halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f).xyz(),
				transform.multiply(halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f).xyz(),
				transform.multiply(-halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f).xyz(),
				transform.multiply(-halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f).xyz(),
				transform.multiply(halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f).xyz(),
				transform.multiply(halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f).xyz()
		);
	}

	public List<Vec3f> getEdges() {
		List<Vec3f> vertices = this.getVertices();
		return List.of(
				// Bottom face edges
				vertices.get(0), vertices.get(1),
				vertices.get(1), vertices.get(2),
				vertices.get(2), vertices.get(3),
				vertices.get(3), vertices.get(0),
				// Top face edges
				vertices.get(4), vertices.get(5),
				vertices.get(5), vertices.get(6),
				vertices.get(6), vertices.get(7),
				vertices.get(7), vertices.get(4),
				// Vertical edges
				vertices.get(0), vertices.get(4),
				vertices.get(1), vertices.get(5),
				vertices.get(2), vertices.get(6),
				vertices.get(3), vertices.get(7)
		);
	}

	/**
	 * Computes the mean center of the vertices returned by {@link CollisionObject3D#getVertices()}.
	 * Used to resolve collision in the {@link PhysicsServer}.
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
