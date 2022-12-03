package io.github.view.geometry;

import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.math.Vector4;

public record BoundingBox3D(Vector3 origin, Vector3 v1, Vector3 v2, Vector3 v3) {

	public BoundingBox3D transformed(Matrix4 transformation) {
		return new BoundingBox3D(
				transformation.multiply(new Vector4(this.origin(), 1.0f)).xyz(),
				transformation.multiply(new Vector4(this.v1(), 1.0f)).xyz(),
				transformation.multiply(new Vector4(this.v2(), 1.0f)).xyz(),
				transformation.multiply(new Vector4(this.v3(), 1.0f)).xyz()
		);
	}

	public boolean intersects(BoundingBox3D boundingBox) {
		Vector3[] axes = {this.v1(), this.v2(), this.v3(), boundingBox.v1(), boundingBox.v2(), boundingBox.v3()};
		for(Vector3 axis : axes) {
			Projection projection1 = this.project(axis);
			Projection projection2 = boundingBox.project(axis);
			if(!projection1.overlaps(projection2))
				return false;
		}
		return true;
	}

	public Projection project(Vector3 axis) {
		Vector3[] vertices = this.findVertices(); // TODO: Find a way to compute projections that does not require finding all vertices
		float min = axis.dotProduct(vertices[0]), max = 0.0f;
		for(Vector3 vertex : vertices) {
			float p = axis.dotProduct(vertex);
			if(p < min) min = p;
			else if(p > max) max = p;
		}
		return new Projection(max, min);
	}

	public Vector3[] findVertices() {
		return new Vector3[] {
				this.origin(),
				this.origin().plus(this.v1()),
				this.origin().plus(this.v2()),
				this.origin().plus(this.v3()),
				this.origin().plus(this.v1()).plus(this.v2()),
				this.origin().plus(this.v2()).plus(this.v3()),
				this.origin().plus(this.v1()).plus(this.v3()),
				this.origin().plus(this.v1()).plus(this.v2()).plus(this.v3())
		};
	}
}
