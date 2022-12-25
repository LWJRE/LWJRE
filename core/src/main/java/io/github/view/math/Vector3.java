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

	public Vector3(Float2 xy, float z) {
		this(xy.x(), xy.y(), z);
	}

	public Vector2 xy() {
		return new Vector2(this.x(), this.y());
	}

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

	public Vector3 crossProduct(float x, float y, float z) {
		return new Vector3(this.y() * z - this.z() * y, x * this.z() - z * this.x(), this.x() * y - this.y() * x);
	}

	public Vector3 crossProduct(Vector3 vector) {
		return this.crossProduct(vector.x(), vector.y(), vector.z());
	}

	@Override
	public float lengthSquared() {
		return this.dotProduct(this);
	}

	public Vector3 multiply(float x, float y, float z) {
		return new Vector3(this.x() * x, this.y() * y, this.z() * z);
	}

	@Override
	public Vector3 multiply(Vector3 vector) {
		return this.multiply(vector.x(), vector.y(), vector.z());
	}

	@Override
	public Vector3 abs() {
		return new Vector3(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()));
	}

	@Override
	public Vector3 clamped(Vector3 max, Vector3 min) {
		return new Vector3(MathExtra.clamp(this.x(), max.x(), min.x()), MathExtra.clamp(this.y(), max.y(), min.y()), MathExtra.clamp(this.z(), max.z(), min.z()));
	}

	@Override
	public Vector3 floor() {
		return new Vector3(MathExtra.floor(this.x()), MathExtra.floor(this.y()), MathExtra.floor(this.z()));
	}

	@Override
	public Vector3 ceil() {
		return new Vector3(MathExtra.ceil(this.x()), MathExtra.ceil(this.y()), MathExtra.ceil(this.z()));
	}

	public Vector3 directionTo(float x, float y, float z) {
		return this.directionTo(new Vector3(x, y, z));
	}

	public float distanceSquaredTo(float x, float y, float z) {
		return this.distanceSquaredTo(new Vector3(x, y, z));
	}

	public double distanceTo(float x, float y, float z) {
		return this.distanceTo(new Vector3(x, y, z));
	}

	public double angleTo(float x, float y, float z) {
		return this.angleTo(new Vector3(x, y, z));
	}

	@Override
	public Vector3 sign() {
		return new Vector3(Math.signum(this.x()), Math.signum(this.y()), Math.signum(this.z()));
	}
}
