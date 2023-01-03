package engine.physics.tree;

import engine.core.tree.Transform3D;
import engine.physics.Collision3D;
import engine.physics.geometry.Projection;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

import java.util.Objects;

public class BoundingBox3D extends Transform3D {

	private Vec3f extents = Vec3f.One();

	public final boolean intersects(BoundingBox3D box) {
		Mat4f rotationA = this.globalRotation();
		Mat4f rotationB = box.globalRotation();
		Vec3f[] axes = new Vec3f[] {
				rotationA.col0().xyz(),
				rotationA.col1().xyz(),
				rotationA.col2().xyz(),
				rotationB.col0().xyz(),
				rotationB.col1().xyz(),
				rotationB.col2().xyz()
		};
		for(Vec3f axis : axes) {
			Projection projectionA = this.project(axis);
			Projection projectionB = box.project(axis);
			if(!projectionA.overlaps(projectionB))
				return false;
		}
		return true;
	}

	// TODO: Optional<Collision3D> ?

	public final Collision3D computeCollision(BoundingBox3D box) {
		Vec3f normal = Vec3f.Zero();
		float depth = Float.MAX_VALUE;
		Mat4f rotationA = this.globalRotation();
		Mat4f rotationB = box.globalRotation();
		Vec3f[] axes = new Vec3f[] {
				rotationA.col0().xyz(),
				rotationA.col1().xyz(),
				rotationA.col2().xyz(),
				rotationB.col0().xyz(),
				rotationB.col1().xyz(),
				rotationB.col2().xyz()
		};
		for(Vec3f axis : axes) {
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
		if(this.meanCenter().minus(box.meanCenter()).dot(normal) < 0.0f) {
			normal = normal.negated();
		}
		return new Collision3D(null, normal, depth);
	}

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
		Vec3f origin = this.globalPosition();
		Mat4f rotation = this.globalRotation();
		Vec3f scale = this.globalScale();
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

//	@Override
//	protected void onUpdate(float delta) {
//		super.onUpdate(delta);
//		Shader.main().createOrLoad().runProgram(shader -> {
//			shader.loadUniform("transformation_matrix", this.globalTransformation());
//			shader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
//			shader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
//			Vec3f halfExtents = this.extents.dividedBy(2.0f);
//			Debug.drawQuads(
//					new Vec3f(halfExtents.x(), halfExtents.y(), halfExtents.z()),
//					new Vec3f(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
//					new Vec3f(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//
//					new Vec3f(-halfExtents.x(), halfExtents.y(), halfExtents.z()),
//					new Vec3f(-halfExtents.x(), halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//
//					new Vec3f(halfExtents.x(), halfExtents.y(), halfExtents.z()),
//					new Vec3f(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), halfExtents.y(), halfExtents.z()),
//
//					new Vec3f(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//					new Vec3f(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//
//					new Vec3f(halfExtents.x(), halfExtents.y(), halfExtents.z()),
//					new Vec3f(halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), halfExtents.z()),
//					new Vec3f(-halfExtents.x(), halfExtents.y(), halfExtents.z()),
//
//					new Vec3f(halfExtents.x(), halfExtents.y(), -halfExtents.z()),
//					new Vec3f(halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), -halfExtents.y(), -halfExtents.z()),
//					new Vec3f(-halfExtents.x(), halfExtents.y(), -halfExtents.z())
//			);
//		});
//	}

	public final void setExtents(Vec3f extents) {
		this.extents = Objects.requireNonNull(extents);
	}

	public final void setExtents(float x, float y, float z) {
		this.extents = new Vec3f(x, y, z);
	}

	public final Vec3f getExtents() {
		return this.extents;
	}
}
