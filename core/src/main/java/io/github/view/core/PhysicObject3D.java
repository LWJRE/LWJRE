package io.github.view.core;

import io.github.view.geometry.BoundingBox3D;
import io.github.view.geometry.Projection;
import io.github.view.graphics.Debug;
import io.github.view.math.Vector3;
import io.github.view.physics.Collision3D;
import io.github.view.physics.PhysicsSystem3D;
import io.github.view.resources.Shader;
import io.github.view.scene.SceneObject;

public abstract class PhysicObject3D extends Script {

	private Shader tempShader;
	protected final Transform3D transform;
	protected final BoundingBox3D boundingBox = new BoundingBox3D(new Vector3(-0.5f, -0.5f, -0.5f), Vector3.ONE);

	public PhysicObject3D(SceneObject object) {
		super(object);
		this.transform = this.object.getScript(Transform3D.class);
	}

	@Override
	public void onStart() {
		PhysicsSystem3D.addObject(this);
		this.tempShader = Shader.main().createOrLoad();
		super.onStart();
	}

	public abstract void onPhysicsUpdate(float time);

	public final Collision3D computeCollision(PhysicObject3D object) {
		Vector3 normal = Vector3.ZERO;
		float depth = Float.MAX_VALUE;
		BoundingBox3D boxA = this.worldPositionBoundingBox();
		BoundingBox3D boxB = object.worldPositionBoundingBox();
		for(Vector3 axis : boxA.getAxes()) {
			Projection projectionA = boxA.project(axis);
			Projection projectionB = boxB.project(axis);
			if(!projectionA.overlaps(projectionB)) {
				return null;
			}
			float axisDepth = projectionA.getOverlap(projectionB);
			if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		for(Vector3 axis : boxB.getAxes()) {
			Projection projectionA = boxA.project(axis);
			Projection projectionB = boxB.project(axis);
			if(!projectionA.overlaps(projectionB)) {
				return null;
			}
			float axisDepth = projectionA.getOverlap(projectionB);
			if(axisDepth < depth) {
				depth = axisDepth;
				normal = axis;
			}
		}
		if(boxA.meanCenter().minus(boxB.meanCenter()).dotProduct(normal) < 0.0) {
			normal = normal.negated();
		}
		return new Collision3D(object, normal, depth);
	}

	public abstract void onCollision(Collision3D collision);

	public final BoundingBox3D worldPositionBoundingBox() {
		return new BoundingBox3D(
				this.boundingBox.origin().plus(this.transform.getPosition()),
				new Vector3(
						this.boundingBox.extents().x() * this.transform.getScale().x(),
						this.boundingBox.extents().y() * this.transform.getScale().y(),
						this.boundingBox.extents().z() * this.transform.getScale().z()
				)
		);
	}

	@Override
	public void onUpdate(float time) {
		this.tempShader.start();
		this.tempShader.loadUniform("transformation_matrix", this.transform.matrix());
		this.tempShader.loadUniform("projection_matrix", Camera3D.currentProjectionMatrix());
		this.tempShader.loadUniform("view_matrix", Camera3D.currentViewMatrix());
		Debug.drawQuads(
				this.boundingBox.origin(),
				this.boundingBox.origin().plus(this.boundingBox.extents().x(), 0.0f, 0.0f),
				this.boundingBox.origin().plus(0.0f, this.boundingBox.extents().y(), 0.0f),
				this.boundingBox.origin().plus(this.boundingBox.extents().x(), this.boundingBox.extents().y(), 0.0f),
				this.boundingBox.origin().plus(this.boundingBox.extents().x(), 0.0f, this.boundingBox.extents().z()),
				this.boundingBox.origin().plus(this.boundingBox.extents().x(), this.boundingBox.extents().y(), this.boundingBox.extents().z()),
				this.boundingBox.origin().plus(0.0f, 0.0f, this.boundingBox.extents().z()),
				this.boundingBox.origin().plus(0.0f, this.boundingBox.extents().y(), this.boundingBox.extents().z())
		);
		super.onUpdate(time);
	}

	@Override
	public void onExit() {
		PhysicsSystem3D.removeObject(this);
		super.onExit();
	}
}
