package io.github.view.core;

import io.github.view.geometry.BoundingBox3D;
import io.github.view.geometry.Projection;
import io.github.view.math.Vector3;
import io.github.view.physics.Collision3D;

import java.util.ArrayList;
import java.util.Objects;

public class CollisionObject3D extends Transform3D {

	private static final ArrayList<CollisionObject3D> COLLIDERS = new ArrayList<>();

	private final BoundingBox3D boundingBox = new BoundingBox3D(new Vector3(-0.5f, -0.5f, -0.5f), Vector3.ONE);

	@Override
	protected void onStart() {
		COLLIDERS.add(this);
		super.onStart();
	}

	public final void moveAndCollide(Vector3 translation) {
		this.translate(translation);
		COLLIDERS.forEach(collider -> COLLIDERS.stream()
				.filter(otherCollider -> !otherCollider.equals(collider))
				.map(collider::computeCollision)
				.filter(Objects::nonNull)
				.findFirst()
				.ifPresent(collider::onCollision));
	}

	public final Collision3D computeCollision(CollisionObject3D collider) {
		Vector3 normal = Vector3.ZERO;
		float depth = Float.MAX_VALUE;
		BoundingBox3D boxA = this.worldPositionBoundingBox();
		BoundingBox3D boxB = collider.worldPositionBoundingBox();
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
		return new Collision3D(collider, normal, depth);
	}

	protected void onCollision(Collision3D collision) {

	}

	public final BoundingBox3D worldPositionBoundingBox() {
		return new BoundingBox3D(
				this.boundingBox.origin().plus(this.globalPosition()),
				new Vector3(
						this.boundingBox.extents().x() * this.globalScale().x(),
						this.boundingBox.extents().y() * this.globalScale().y(),
						this.boundingBox.extents().z() * this.globalScale().z()
				)
		);
	}

	@Override
	protected void onExit() {
		COLLIDERS.remove(this);
		super.onExit();
	}
}
