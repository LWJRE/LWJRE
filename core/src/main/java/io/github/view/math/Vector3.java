package io.github.view.math;

public record Vector3(float x, float y, float z) implements Vector<Vector3>, Float3 {

	public static final Vector3 ZERO = new Vector3(0.0f, 0.0f, 0.0f);
	public static final Vector3 ONE = new Vector3(1.0f, 1.0f, 1.0f);
	public static final Vector3 UP = new Vector3(0.0f, 1.0f, 0.0f);
	public static final Vector3 DOWN = new Vector3(0.0f, -1.0f, 0.0f);
	public static final Vector3 LEFT = new Vector3(-1.0f, 0.0f, 0.0f);
	public static final Vector3 RIGHT = new Vector3(1.0f, 0.0f, 0.0f);
	public static final Vector3 FORWARD = new Vector3(0.0f, 0.0f, 1.0f);
	public static final Vector3 BACKWARDS = new Vector3(0.0f, 0.0f, -1.0f);

	public Vector3 plus(float x, float y, float z) {
		return new Vector3(this.x() + x, this.y() + y, this.z() + z);
	}

	@Override
	public Vector3 plus(Vector3 vector) {
		return this.plus(vector.x(), vector.y(), vector.z());
	}

	@Override
	public Vector3 negated() {
		return new Vector3(-this.x(), -this.y(), -this.z());
	}

	public Vector3 minus(float x, float y, float z) {
		return this.plus(-x, -y, -z);
	}

	@Override
	public Vector3 multipliedBy(float k) {
		return new Vector3(this.x() * k, this.y() * k, this.z() * k);
	}

	public float dotProduct(float x, float y, float z) {
		return this.x() * x + this.y() * y + this.z() * z;
	}

	@Override
	public float dotProduct(Vector3 vector) {
		return this.dotProduct(vector.x(), vector.y(), vector.z());
	}

	@Override
	public float lengthSquared() {
		return this.dotProduct(this);
	}
}
