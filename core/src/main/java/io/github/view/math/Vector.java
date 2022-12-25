package io.github.view.math;

public interface Vector<V extends Vector<V>> {

	V plus(V vector);

	V negated();

	default V minus(V vector) {
		return this.plus(vector.negated());
	}

	V multipliedBy(float k);

	default V dividedBy(float k) {
		return this.multipliedBy(1.0f / k);
	}

	float dotProduct(V vector);

	float lengthSquared();

	default double length() {
		return Math.sqrt(this.lengthSquared());
	}

	default V normalized() {
		return this.dividedBy((float) this.length());
	}

	V multiply(V vector);

	V abs();

	default V project(V vector) {
		return vector.multipliedBy(this.dotProduct(vector) / vector.lengthSquared());
	}

	default V reflect(V normal) {
		return this.minus(normal.multipliedBy(2 * this.dotProduct(normal)));
	}

	// TODO: slide

	V clamped(V max, V min);

	V floor();

	V ceil();

	default V directionTo(V vector) {
		return this.plus(vector).negated().normalized();
	}

	default float distanceSquaredTo(V vector) {
		return this.minus(vector).dotProduct(this.minus(vector));
	}

	default double distanceTo(V vector) {
		return Math.sqrt(this.distanceSquaredTo(vector));
	}

	default double angleTo(V vector) {
		return Math.acos(this.dotProduct(vector) / (this.length() * vector.length()));
	}

	default V interpolate(V vector, float weight) {
		return this.plus(this.minus(vector).negated().multipliedBy(weight));
	}

	default V moveToward(V vector, float delta) {
		return this.plus(vector.multipliedBy(delta)).clamped(vector, this.multipliedBy(1.0f));
	}

	V sign();
}
