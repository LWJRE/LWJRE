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

	default float length() {
		return (float) Math.sqrt(this.lengthSquared());
	}
}
