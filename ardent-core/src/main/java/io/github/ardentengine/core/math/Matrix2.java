package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 2x2 float matrix.
 *
 * @param m00 Element 0 0
 * @param m01 Element 0 1
 * @param m10 Element 1 0
 * @param m11 Element 1 1
 */
public record Matrix2(float m00, float m01, float m10, float m11) implements Serializable {

    /** Shorthand for the zero matrix. */
    public static Matrix2 ZERO = new Matrix2(0.0f, 0.0f, 0.0f, 0.0f);
    /** Shorthand for the identity matrix. */
    public static Matrix2 IDENTITY = new Matrix2(1.0f, 0.0f, 0.0f, 1.0f);

    /**
     * Adds the given matrix to this one and returns the result.
     *
     * @param m The matrix to add.
     * @return The sum between this matrix and the given one.
     */
    public Matrix2 plus(Matrix2 m) {
        return new Matrix2(
            this.m00() + m.m00(), this.m01() + m.m01(),
            this.m10() + m.m10(), this.m11() + m.m11()
        );
    }

    /**
     * Subtracts the given matrix from this one and returns the result.
     *
     * @param m The matrix to subtract.
     * @return The subtraction between this matrix and the given one.
     */
    public Matrix2 minus(Matrix2 m) {
        return new Matrix2(
            this.m00() - m.m00(), this.m01() - m.m01(),
            this.m10() - m.m10(), this.m11() - m.m11()
        );
    }

    /**
     * Returns the additive inverse of this matrix.
     *
     * @return The additive inverse of this matrix.
     */
    public Matrix2 negated() {
        return new Matrix2(
            -this.m00(), -this.m01(),
            -this.m10(), -this.m11()
        );
    }

    /**
     * Multiplies this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this matrix by.
     * @return The product between this matrix and the given scalar.
     */
    public Matrix2 multiply(float k) {
        return new Matrix2(
            this.m00() * k, this.m01() * k,
            this.m10() * k, this.m11() * k
        );
    }

    /**
     * Divides this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to divide this matrix by.
     * @return The division between this matrix and the given scalar.
     */
    public Matrix2 divide(float k) {
        return new Matrix2(
            this.m00() / k, this.m01() / k,
            this.m10() / k, this.m11() / k
        );
    }

    /**
     * Returns the first row of this matrix as a {@link Vector2}.
     *
     * @return The first row of this matrix.
     * @see Matrix2#row(int)
     */
    public Vector2 row0() {
        return new Vector2(this.m00(), this.m01());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector2}.
     *
     * @return The second row of this matrix.
     * @see Matrix2#row(int)
     */
    public Vector2 row1() {
        return new Vector2(this.m10(), this.m11());
    }

    /**
     * Returns the row at the given index as a {@link Vector2}.
     *
     * @param i The index of the requested row. Must be either 0 or 1.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector2 row(int i) {
        return switch (i) {
            case 0 -> this.row0();
            case 1 -> this.row1();
            default -> throw new IndexOutOfBoundsException("Row " + i + " is out of bounds for a 2x2 matrix");
        };
    }

    /**
     * Returns the first column of this matrix as a {@link Vector2}.
     *
     * @return The first column of this matrix.
     * @see Matrix2#column(int)
     */
    public Vector2 column0() {
        return new Vector2(this.m00(), this.m10());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector2}.
     *
     * @return The second column of this matrix.
     * @see Matrix2#column(int)
     */
    public Vector2 column1() {
        return new Vector2(this.m01(), this.m11());
    }

    /**
     * Returns the column at the given index as a {@link Vector2}.
     *
     * @param i The index of the requested column. Must be either 0 or 1.
     * @return The column at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector2 column(int i) {
        return switch (i) {
            case 0 -> this.column0();
            case 1 -> this.column1();
            default -> throw new IndexOutOfBoundsException("Column " + i + " is out of bounds for a 2x2 matrix");
        };
    }

    /**
     * Multiplies this matrix by the vector with the given components and returns the result.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @return The product of this matrix by the vector with the given components.
     */
    public Vector2 multiply(float x, float y) {
        return new Vector2(
            this.m00() * x + this.m01() * y,
            this.m10() * x + this.m11() * y
        );
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     *
     * @param v The vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector2 multiply(Vector2 v) {
        return this.multiply(v.x(), v.y());
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix2 multiply(Matrix2 m) {
        return new Matrix2(
            this.m00() * m.m00() + this.m01() * m.m10(), this.m00() * m.m01() + this.m01() * m.m11(),
            this.m10() * m.m00() + this.m11() * m.m10(), this.m10() * m.m01() + this.m11() * m.m11()
        );
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix2x3 multiply(Matrix2x3 m) {
        return new Matrix2x3(
            this.m00() * m.m00() + this.m01() * m.m10(), this.m00() * m.m01() + this.m01() * m.m11(), this.m00() * m.m02() + this.m01() * m.m12(),
            this.m10() * m.m00() + this.m11() * m.m10(), this.m10() * m.m01() + this.m11() * m.m11(), this.m10() * m.m02() + this.m11() * m.m12()
        );
    }

    /**
     * Returns the transposed of this matrix.
     * <p>
     *     The transposed of a matrix is obtained by switching its rows and its columns.
     * </p>
     *
     * @return The transposed of this matrix.
     */
    public Matrix2 transposed() {
        return new Matrix2(
            this.m00(), this.m10(),
            this.m01(), this.m11()
        );
    }

    /**
     * Checks if this matrix is symmetric by checking if it is equal to its {@link Matrix2#transposed()}.
     *
     * @return True if this matrix is equal to its transposed, otherwise false.
     */
    public boolean isSymmetric() {
        return this.m01() == this.m10();
    }

    /**
     * Checks if this matrix is skew-symmetric by checking if it is equal to its {@link Matrix2#negated()} {@link Matrix2#transposed()}.
     *
     * @return True if this matrix is equal to its negated transposed, otherwise false.
     */
    public boolean isSkewSymmetric() {
        return this.m01() == -this.m10();
    }

    /**
     * Returns the determinant of this matrix.
     *
     * @return The determinant of this matrix.
     */
    public float determinant() {
        return this.m00() * this.m11() - this.m01() * this.m10();
    }

    /**
     * Returns the inverse of this matrix.
     * <p>
     *     The result is undefined if this matrix is not invertible.
     *     It is possible to check if the matrix is invertible by checking if its {@link Matrix2#determinant()} is not zero.
     * </p>
     *
     * @return The inverse of this matrix.
     */
    public Matrix2 inverse() {
        var det = this.determinant();
        return new Matrix2(
            this.m11() / det, -this.m01() / det,
            -this.m10() / det, this.m00() / det
        );
    }

    /**
     * Raises this matrix to the given power by multiplying it with itself {@code exp} times and returns the result.
     * <p>
     *     If the given exponent is zero, the result will be the identity matrix.
     * </p>
     * <p>
     *     A matrix raised to a negative power is defined as the {@link Matrix2#inverse()} matrix raised to {@code -exp}.
     *     The result is undefined if the given exponent is negative and this matrix is not invertible.
     * </p>
     *
     * @param exp The exponent to raise this matrix to.
     * @return This matrix raised to the given power.
     */
    public Matrix2 power(int exp) {
        if(exp == -1) {
            return this.inverse();
        } else if(exp < 0) {
            return this.inverse().power(-exp);
        } else if(exp == 1) {
            return this;
        } else if(exp == 0) {
            return Matrix2.IDENTITY;
        } else {
            return this.multiply(this.power(exp - 1));
        }
    }

    /**
     * Computes the linear interpolation between the elements of this matrix and the elements of the given one by the given weight and returns the result.
     * <p>
     *     The given weight must be between zero and one, representing the amount of interpolation.
     * </p>
     *
     * @param to The second matrix.
     * @param weight The weight of the interpolation between zero and one.
     * @return The result of linearly interpolating between the elements of this matrix and the elements of the given one.
     */
    public Matrix2 lerp(Matrix2 to, float weight) {
        return new Matrix2(
            MathUtils.lerp(this.m00(), to.m00(), weight), MathUtils.lerp(this.m01(), to.m01(), weight),
            MathUtils.lerp(this.m10(), to.m10(), weight), MathUtils.lerp(this.m11(), to.m11(), weight)
        );
    }

    /**
     * Returns a matrix with all elements in absolute value.
     *
     * @return A matrix with all elements in absolute value.
     */
    public Matrix2 abs() {
        return new Matrix2(
            Math.abs(this.m00()), Math.abs(this.m01()),
            Math.abs(this.m10()), Math.abs(this.m11())
        );
    }

    /**
     * Returns a matrix with the signs of the elements of this matrix.
     *
     * @return A matrix with all elements representing the sign of this matrix.
     */
    public Matrix2 sign() {
        return new Matrix2(
            Math.signum(this.m00()), Math.signum(this.m01()),
            Math.signum(this.m10()), Math.signum(this.m11())
        );
    }

    /**
     * Returns a matrix with all elements rounded to the nearest integer.
     *
     * @return A matrix with all elements rounded to the nearest integer.
     */
    public Matrix2 round() {
        return new Matrix2(
            Math.round(this.m00()), Math.round(this.m01()),
            Math.round(this.m10()), Math.round(this.m11())
        );
    }

    /**
     * Returns a matrix with all elements rounded up.
     *
     * @return A matrix with all elements rounded up.
     */
    public Matrix2 ceil() {
        return new Matrix2(
            (float) Math.ceil(this.m00()), (float) Math.ceil(this.m01()),
            (float) Math.ceil(this.m10()), (float) Math.ceil(this.m11())
        );
    }

    /**
     * Returns a matrix with all elements rounded down.
     *
     * @return A matrix with all elements rounded down.
     */
    public Matrix2 floor() {
        return new Matrix2(
            (float) Math.floor(this.m00()), (float) Math.floor(this.m01()),
            (float) Math.floor(this.m10()), (float) Math.floor(this.m11())
        );
    }

    /**
     * Constructs an orthonormal matrix from the columns of this matrix using the Gram-Schmidt procedure.
     *
     * @return This matrix with orthogonal columns of unit length.
     */
    public Matrix2 orthonormalized() {
        var v0 = this.column0();
        var v1 = this.column1();
        v1 = v1.minus(v1.project(v0));
        return Matrix2.fromColumns(v0.normalized(), v1.normalized());
    }

    /**
     * Checks if this matrix is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param m The second matrix.
     * @return True if this matrix is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Matrix2 m) {
        return MathUtils.equalsApprox(this.m00(), m.m00()) && MathUtils.equalsApprox(this.m01(), m.m01())
            && MathUtils.equalsApprox(this.m10(), m.m10()) && MathUtils.equalsApprox(this.m11(), m.m11());
    }

    /**
     * Returns a 2x2 matrix from the given rows.
     *
     * @param row0 The first row.
     * @param row1 The second row.
     * @return A 2x2 matrix from the given rows.
     */
    public static Matrix2 fromRows(Vector2 row0, Vector2 row1) {
        return new Matrix2(row0.x(), row0.y(), row1.x(), row1.y());
    }

    /**
     * Returns a 2x2 matrix from the given columns.
     *
     * @param col0 The first column.
     * @param col1 The second column.
     * @return A 2x2 matrix from the given columns.
     */
    public static Matrix2 fromColumns(Vector2 col0, Vector2 col1) {
        return new Matrix2(col0.x(), col1.x(), col0.y(), col1.y());
    }

    /**
     * Returns a 2x2 matrix representing a rotation by the given angle in a 2D space.
     *
     * @param angle The rotation angle in radians.
     * @return A 2x2 matrix representing a rotation by the given angle in a 2D space.
     */
    public static Matrix2 rotation(double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Matrix2(cos, -sin, sin, cos);
    }

    /**
     * Returns a 2x2 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param x Scale factor on the x axis.
     * @param y Scale factor on the y axis.
     * @return A 2x2 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2 scaling(float x, float y) {
        return new Matrix2(x, 0.0f, 0.0f, y);
    }

    /**
     * Returns a 2x2 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param v A vector representing the scale factor on the x and y axes.
     * @return A 2x2 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2 scaling(Vector2 v) {
        return scaling(v.x(), v.y());
    }

    /**
     * Returns a 2x2 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param scale Scale factor on the x and y axes.
     * @return A 2x2 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2 scaling(float scale) {
        return scaling(scale, scale);
    }

    /**
     * Returns a 2x2 matrix representing a shear transformation by the given angles in a 2D space.
     *
     * @param x Angle on the x axis in radians.
     * @param y Angle on the y axis in radians.
     * @return A 2x2 matrix representing a shear transformation by the given angles in a 2D space.
     */
    public static Matrix2 shearing(double x, double y) {
        return new Matrix2(1.0f, (float) Math.tan(x), (float) Math.tan(y), 1.0f);
    }

    /**
     * Returns a 2x2 matrix representing a shear transformation by the given angles in a 2D space.
     *
     * @param v A vector representing the shear angle on the x and y axes.
     * @return A 2x2 matrix representing a shear transformation by the given angles in a 2D space.
     */
    public static Matrix2 shearing(Vector2 v) {
        return shearing(v.x(), v.y());
    }
}