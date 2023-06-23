package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.lwjre.engine.annotations.EditorRange;
import io.github.lwjre.engine.annotations.EditorVariable;
import io.github.lwjre.engine.debug.DebugRenderer;
import io.github.lwjre.engine.servers.PhysicsServer;

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
		DebugRenderer.drawCube(this.globalTransformation().multiply(Mat4f.scaling(this.boundingBox)), 0.0f, 0.5f, 1.0f);
		super.onEditorProcess();
	}

	@Override
	protected void onExit() {
		PhysicsServer.remove(this);
		super.onExit();
	}

	/**
	 * Called when this object collides with another one.
	 * Classes that extend {@code CollisionObject3D} must override this method to implement their collision resolution or to listen for collision events.
	 *
	 * @param collider The {@code CollisionObject3D} that collided with this one
	 * @param normal The collision's normal
	 * @param depth Penetration depth
	 */
	protected void onCollision(CollisionObject3D collider, Vec3f normal, float depth) {

	}

	public final void resolveCollision(CollisionObject3D collider) {
		Vec3f normal = Vec3f.Zero();
		float depth = Float.POSITIVE_INFINITY;
		Mat4f rotationA = this.globalRotation();
		Mat4f rotationB = collider.globalRotation();
		Vec3f[] axes = new Vec3f[] {
				rotationA.col0().xyz(),
				rotationA.col1().xyz(),
				rotationA.col2().xyz(),
				rotationB.col0().xyz(),
				rotationB.col1().xyz(),
				rotationB.col2().xyz()
		};
		List<Vec3f> verticesA = this.getVertices();
		List<Vec3f> verticesB = collider.getVertices();
		for(Vec3f axis : axes) {
			float axisDepth = checkAxis(verticesA, verticesB, axis);
			if(axisDepth < 0.0f) {
				return;
			} else if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		if(collider.globalPosition().minus(this.globalPosition()).dot(normal) < 0.0f) {
			normal = normal.negated();
		}
		this.onCollision(collider, normal, depth);
	}

	private static float checkAxis(List<Vec3f> verticesA, List<Vec3f> verticesB, Vec3f axis) {
		float minA = Float.POSITIVE_INFINITY, maxA = Float.NEGATIVE_INFINITY;
		for(Vec3f vertexA : verticesA) {
			float projection = vertexA.dot(axis);
			if(projection < minA) minA = projection;
			if(projection > maxA) maxA = projection;
		}
		float minB = Float.POSITIVE_INFINITY, maxB = Float.NEGATIVE_INFINITY;
		for(Vec3f vertexB : verticesB) {
			float projection = vertexB.dot(axis);
			if(projection < minB) minB = projection;
			if(projection > maxB) maxB = projection;
		}
		return Math.min(maxA, maxB) - Math.max(minA, minB);
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

	public List<Vec3f> getFaces() {
		List<Vec3f> vertices = this.getVertices();
		return List.of(
			// Bottom face
			vertices.get(0), vertices.get(1), vertices.get(2), vertices.get(3),
			// Front face
			vertices.get(5), vertices.get(1), vertices.get(2), vertices.get(6),
			// Left face
			vertices.get(4), vertices.get(0), vertices.get(1), vertices.get(5),
			// Right face
			vertices.get(6), vertices.get(2), vertices.get(3), vertices.get(7),
			// Back face
			vertices.get(4), vertices.get(0), vertices.get(3), vertices.get(7),
			// Top face
			vertices.get(4), vertices.get(5), vertices.get(6), vertices.get(7)
		);
	}
}
