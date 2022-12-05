package io.github.view.math;

public interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> {

	M plus(M matrix);

	M negative();

	default M minus(M matrix) {
		return this.plus(matrix.negative());
	}

	M multipliedBy(float k);

	V multiply(V vector);

	M transposed();

	default M negativeTransposed() {
		return this.transposed().negative();
	}

	default boolean isSymmetric() {
		return this.equals(this.transposed());
	}

	default boolean isSkewSymmetric() {
		return this.equals(this.negativeTransposed());
	}

	M multiply(M matrix);

	M power(int exponent);
}
