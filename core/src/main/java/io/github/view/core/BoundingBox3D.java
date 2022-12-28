package io.github.view.core;

import io.github.view.graphics.Debug;
import io.github.view.math.Projection;
import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.math.Vector4;
import io.github.view.physics.Collision3D;
import io.github.view.resources.Shader;

import java.util.Objects;

public class BoundingBox3D extends Transform3D {

	private Vector3 extents = Vector3.ONE;

	public final boolean intersects(BoundingBox3D box) {
		Matrix4 rotationA = this.globalRotation();
		Matrix4 rotationB = box.globalRotation();
		Vector3[] axes = new Vector3[] {
				rotationA.column0().xyz(),
				rotationA.column1().xyz(),
				rotationA.column2().xyz(),
				rotationB.column0().xyz(),
				rotationB.column1().xyz(),
				rotationB.column2().xyz()
		};
		for(Vector3 axis : axes) {
			Projection projectionA = this.project(axis);
			Projection projectionB = box.project(axis);
			if(!projectionA.overlaps(projectionB))
				return false;
		}
		return true;
	}

	// TODO: Optional<Collision3D> ?

	public final Collision3D computeCollision(BoundingBox3D box) {
		Vector3 normal = Vector3.ZERO;
		float depth = Float.MAX_VALUE;
		Matrix4 rotationA = this.globalRotation();
		Matrix4 rotationB = box.globalRotation();
		Vector3[] axes = new Vector3[] {
				rotationA.column0().xyz(),
				rotationA.column1().xyz(),
				rotationA.column2().xyz(),
				rotationB.column0().xyz(),
				rotationB.column1().xyz(),
				rotationB.column2().xyz()
		};
		for(Vector3 axis : axes) {
			Projection projectionA = this.project(axis);
			Projection projectionB = box.project(axis);
			if(!projectionA.overlaps(projectionB))
				return null;
			float axisDepth = projectionA.getOverlap(projectionB).length();
			if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		if(this.meanCenter().minus(box.meanCenter()).dotProduct(normal) < 0.0f) {
			normal = normal.negated();
		}
		return new Collision3D(null, normal, depth);
	}

	public final Projection project(Vector3 axis) {
		float min = Float.POSITIVE_INFINITY, max = Float.NEGATIVE_INFINITY;
		for(Vector3 vertex : this.getVertices()) {
			float projection = vertex.dotProduct(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(min, max);
	}

	public final Vector3 meanCenter() {
		Vector3 mean = Vector3.ZERO;
		for(Vector3 vertex : this.getVertices()) {
			mean = mean.plus(vertex);
		}
		return mean.dividedBy(8);
	}

	private Vector3[] getVertices() {
		Vector3 halfExtents = this.extents.dividedBy(2.0f);
		Vector3 origin = this.globalPosition();
		Matrix4 rotation = this.globalRotation();
		Vector3 scale = this.globalScale();
		return new Vector3[] {
				origin.plus(rotation.multiply(new Vector4(halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(-halfExtents.x(), halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(-halfExtents.x(), -halfExtents.y(), halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(-halfExtents.x(), halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale)),
				origin.plus(rotation.multiply(new Vector4(-halfExtents.x(), -halfExtents.y(), -halfExtents.z(), 1.0f)).xyz().multiply(scale))
		};
	}

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		Shader.main().createOrLoad().runProgram(shader -> {
			shader.loadUniform("transformation_matrix", this.globalTransformation());
			shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
			shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
			Vector3 halfExtents = this.extents.dividedBy(2.0f);
			Debug.drawQuads(
					new Vector3(halfExtents.x(), halfExtents.y(), halfExtents.z()),
					new Vector3(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
					new Vector3(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(halfExtents.x(), -halfExtents.y(), halfExtents.z()),

					new Vector3(-halfExtents.x(), halfExtents.y(), halfExtents.z()),
					new Vector3(-halfExtents.x(), halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),

					new Vector3(halfExtents.x(), halfExtents.y(), halfExtents.z()),
					new Vector3(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), halfExtents.y(), halfExtents.z()),

					new Vector3(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
					new Vector3(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),

					new Vector3(halfExtents.x(), halfExtents.y(), halfExtents.z()),
					new Vector3(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),
					new Vector3(-halfExtents.x(), halfExtents.y(), halfExtents.z()),

					new Vector3(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
					new Vector3(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
					new Vector3(-halfExtents.x(), halfExtents.y(), -halfExtents.z())
			);
		});
	}

	public final void setExtents(Vector3 extents) {
		this.extents = Objects.requireNonNull(extents);
	}

	public final void setExtents(float x, float y, float z) {
		this.extents = new Vector3(x, y, z);
	}

	public final Vector3 getExtents() {
		return this.extents;
	}
}
