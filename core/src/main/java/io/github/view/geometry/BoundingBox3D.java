package io.github.view.geometry;

import io.github.view.math.Vector3;

public record BoundingBox3D(Vector3 origin, Vector3 extents) {

	public Vector3[] getAxes() {
		return new Vector3[] {Vector3.RIGHT, Vector3.UP, Vector3.FORWARD};
	}

	public Projection project(Vector3 axis) {
		float min = Float.MAX_VALUE, max = Float.MIN_VALUE;
		for(Vector3 vertex : this.getVertices()) {
			float projection = vertex.dotProduct(axis);
			if(projection < min) min = projection;
			if(projection > max) max = projection;
		}
		return new Projection(max, min);
	}

	public Vector3 meanCenter() {
		Vector3 mean = Vector3.ZERO;
		for(Vector3 vertex : this.getVertices()) {
			mean = mean.plus(vertex);
		}
		return mean.dividedBy(8);
	}

	public Vector3[] getVertices() {
		return new Vector3[] {
				this.origin(),
				this.origin().plus(this.extents().x(), 0.0f, 0.0f),
				this.origin().plus(0.0f, this.extents().y(), 0.0f),
				this.origin().plus(this.extents().x(), this.extents().y(), 0.0f),
				this.origin().plus(this.extents().x(), 0.0f, this.extents().z()),
				this.origin().plus(this.extents().x(), this.extents().y(), this.extents().z()),
				this.origin().plus(0.0f, 0.0f, this.extents().z()),
				this.origin().plus(0.0f, this.extents().y(), this.extents().z())
		};
	}
}
