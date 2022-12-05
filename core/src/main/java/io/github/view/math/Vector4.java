package io.github.view.math;

public record Vector4(float x, float y, float z, float w) implements Vector<Vector4>, Float4{

	public static final Vector4 ZERO = new Vector4(0.0f, 0.0f, 0.0f, 0.0f);
	public static final Vector4 ONE = new Vector4(1.0f, 1.0f, 1.0f, 1.0f);

	public Vector4(Float2 xy, float z, float w) {
		this(xy.x(), xy.y(), z, w);
	}

	public Vector4(Float3 xyz, float w) {
		this(xyz.x(), xyz.y(), xyz.z(), w);
	}

	public Vector2 xy() {
		return new Vector2(this.x(), this.y());
	}

	public Vector3 xyz() {
		return new Vector3(this.x(), this.y(), this.z());
	}

	public Vector4 plus(float x, float y, float z, float w) {
		return new Vector4(this.x() + x, this.y() + y, this.z() + z, this.w() + w);
	}

	@Override
	public Vector4 plus(Vector4 vector) {
		return this.plus(vector.x(), vector.y(), vector.z(), vector.w());
	}

	@Override
	public Vector4 negated() {
		return new Vector4(-this.x(), -this.y(), -this.z(), -this.w());
	}

	public Vector4 minus(float x, float y, float z, float w) {
		return this.plus(-x, -y, -z, -w);
	}

	@Override
	public Vector4 multipliedBy(float k) {
		return new Vector4(this.x() * k, this.y() * k, this.z() * k, this.w() * k);
	}

	public float dotProduct(float x, float y, float z, float w) {
		return this.x() * x + this.y() * y + this.z() * z + this.w() * w;
	}

	@Override
	public float dotProduct(Vector4 vector) {
		return this.dotProduct(vector.x(), vector.y(), vector.z(), vector.w());
	}

	@Override
	public float lengthSquared() {
		return this.dotProduct(this);
	}

	@Override
	public Vector4 abs() {
		return new Vector4(Math.abs(this.x()), Math.abs(this.y()), Math.abs(this.z()), Math.abs(this.w()));
	}

	@Override
	public Vector4 clamped(Vector4 max, Vector4 min) {
		return new Vector4(MathExtra.clamp(this.x(), max.x(), min.x()), MathExtra.clamp(this.y(), max.y(), min.y()), MathExtra.clamp(this.z(), max.z(), min.z()), MathExtra.clamp(this.w(), max.w(), min.w()));
	}

	@Override
	public Vector4 floor() {
		return new Vector4(MathExtra.floor(this.x()), MathExtra.floor(this.y()), MathExtra.floor(this.z()), MathExtra.floor(this.w()));
	}

	@Override
	public Vector4 ceil() {
		return new Vector4(MathExtra.ceil(this.x()), MathExtra.ceil(this.y()), MathExtra.ceil(this.z()), MathExtra.ceil(this.w()));
	}

	public Vector4 directionTo(float x, float y, float z, float w) {
		return this.directionTo(new Vector4(x, y, z, w));
	}

	public float distanceSquaredTo(float x, float y, float z, float w) {
		return this.distanceSquaredTo(new Vector4(x, y, z, w));
	}

	public double distanceTo(float x, float y, float z, float w) {
		return this.distanceTo(new Vector4(x, y, z, w));
	}

	public double angleTo(float x, float y, float z, float w) {
		return this.angleTo(new Vector4(x, y, z, w));
	}

	@Override
	public Vector4 sign() {
		return new Vector4(Math.signum(this.x()), Math.signum(this.y()), Math.signum(this.z()), Math.signum(this.w()));
	}
}
