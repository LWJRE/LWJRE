package io.github.view.math;

public interface Matrix<M extends Matrix<M, V>, V extends Vector<V>> {

	M plus(M matrix);

	V multiply(V vector);

	M multiply(M matrix);
}
