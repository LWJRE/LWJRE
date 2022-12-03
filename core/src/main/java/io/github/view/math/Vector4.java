package io.github.view.math;

public record Vector4(float x, float y, float z, float w) implements Vector<Vector4>, Float4{

	public Vector4(Vector3 xyz, float w) {
		this(xyz.x(), xyz.y(), xyz.z(), w);
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
}
