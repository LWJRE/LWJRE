package io.github.view.math;

public record Vector2(float x, float y) implements Vector<Vector2>, Float2 {

	public static final Vector2 ZERO = new Vector2(0.0f, 0.0f);
	public static final Vector2 ONE = new Vector2(1.0f, 1.0f);
	public static final Vector2 UP = new Vector2(0.0f, 1.0f);
	public static final Vector2 DOWN = new Vector2(0.0f, -1.0f);
	public static final Vector2 LEFT = new Vector2(-1.0f, 0.0f);
	public static final Vector2 RIGHT = new Vector2(1.0f, 0.0f);

	public Vector2 plus(float x, float y) {
		return new Vector2(this.x() + x, this.y() + y);
	}

	@Override
	public Vector2 plus(Vector2 vector) {
		return this.plus(vector.x(), vector.y());
	}

	@Override
	public Vector2 negated() {
		return new Vector2(-this.x(), -this.y());
	}

	public Vector2 minus(float x, float y) {
		return this.plus(-x, -y);
	}

	@Override
	public Vector2 multipliedBy(float k) {
		return new Vector2(this.x() * k, this.y() * k);
	}

	public float dotProduct(float x, float y) {
		return this.x() * x + this.y() * y;
	}

	@Override
	public float dotProduct(Vector2 vector) {
		return this.dotProduct(vector.x(), vector.y());
	}

	@Override
	public float lengthSquared() {
		return this.dotProduct(this);
	}

	public Vector2 multiply(float x, float y) {
		return new Vector2(this.x() * x, this.y() * y);
	}

	@Override
	public Vector2 multiply(Vector2 vector) {
		return this.multiply(vector.x(), vector.y());
	}

	@Override
	public Vector2 abs() {
		return new Vector2(Math.abs(this.x()), Math.abs(this.y()));
	}

	@Override
	public Vector2 clamped(Vector2 max, Vector2 min) {
		return new Vector2(MathExtra.clamp(this.x(), max.x(), min.x()), MathExtra.clamp(this.y(), max.y(), max.y()));
	}

	@Override
	public Vector2 floor() {
		return new Vector2(MathExtra.floor(this.x()), MathExtra.floor(this.y()));
	}

	@Override
	public Vector2 ceil() {
		return new Vector2(MathExtra.ceil(this.x()), MathExtra.ceil(this.y()));
	}

	public Vector2 directionTo(float x, float y) {
		return this.directionTo(new Vector2(x, y));
	}

	public float distanceSquaredTo(float x, float y) {
		return this.distanceSquaredTo(new Vector2(x, y));
	}

	public double distanceTo(float x, float y) {
		return this.distanceTo(new Vector2(x, y));
	}

	public double angleTo(float x, float y) {
		return this.angleTo(new Vector2(x, y));
	}

	@Override
	public Vector2 sign() {
		return new Vector2(Math.signum(this.x()), Math.signum(this.y()));
	}
}
