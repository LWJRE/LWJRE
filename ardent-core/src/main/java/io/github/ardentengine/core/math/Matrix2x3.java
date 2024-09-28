package io.github.ardentengine.core.math;

import java.io.Serializable;

/**
 * A 2x3 (two rows and three columns) float matrix.
 * <p>
 *     Useful for representing 2D transformations such as translation, rotation, and scaling.
 * </p>
 *
 * @param m00 Element 0 0
 * @param m01 Element 0 1
 * @param m02 Element 0 2
 * @param m10 Element 1 0
 * @param m11 Element 1 1
 * @param m12 Element 1 2
 */
public record Matrix2x3(float m00, float m01, float m02, float m10, float m11, float m12) implements Serializable {

    /** Shorthand for the zero matrix. */
    public static Matrix2x3 ZERO = new Matrix2x3(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);

    /**
     * Constructs a 2x3 matrix from a 2x2 matrix and an additional column.
     *
     * @param m The 2x2 matrix representing the first two columns.
     * @param m02 First element of the third column.
     * @param m12 Second element of the third column.
     */
    public Matrix2x3(Matrix2 m, float m02, float m12) {
        this(m.m00(), m.m01(), m02, m.m10(), m.m11(), m12);
    }

    /**
     * Constructs a 2x3 matrix from a 2x2 matrix and an additional column.
     *
     * @param m The 2x2 matrix representing the first two columns.
     * @param column2 The third column.
     */
    public Matrix2x3(Matrix2 m, Vector2 column2) {
        this(m, column2.x(), column2.y());
    }

    /**
     * Constructs a 2x3 matrix from a 2x2 matrix and an additional column.
     *
     * @param m00 First element of the first column.
     * @param m10 Second element of the first column.
     * @param m The 2x2 matrix representing the second and the third column.
     */
    public Matrix2x3(float m00, float m10, Matrix2 m) {
        this(m00, m.m00(), m.m01(), m10, m.m10(), m.m11());
    }

    /**
     * Constructs a 2x3 matrix from a 2x2 matrix and an additional column.
     *
     * @param column0 The first column.
     * @param m The 2x2 matrix representing the second and the third column.
     */
    public Matrix2x3(Vector2 column0, Matrix2 m) {
        this(column0.x(), column0.y(), m);
    }

    /**
     * Adds the given matrix to this one and returns the result.
     *
     * @param m The matrix to add.
     * @return The sum between this matrix and the given one.
     */
    public Matrix2x3 plus(Matrix2x3 m) {
        return new Matrix2x3(
            this.m00() + m.m00(), this.m01() + m.m01(), this.m02() + m.m02(),
            this.m10() + m.m10(), this.m11() + m.m11(), this.m12() + m.m12()
        );
    }

    /**
     * Subtracts the given matrix from this one and returns the result.
     *
     * @param m The matrix to subtract.
     * @return The subtraction between this matrix and the given one.
     */
    public Matrix2x3 minus(Matrix2x3 m) {
        return new Matrix2x3(
            this.m00() - m.m00(), this.m01() - m.m01(), this.m02() - m.m02(),
            this.m10() - m.m10(), this.m11() - m.m11(), this.m12() - m.m12()
        );
    }

    /**
     * Returns the additive inverse of this matrix.
     *
     * @return The additive inverse of this matrix.
     */
    public Matrix2x3 negated() {
        return new Matrix2x3(
            -this.m00(), -this.m01(), -this.m02(),
            -this.m10(), -this.m11(), -this.m12()
        );
    }

    /**
     * Multiplies this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to multiply this matrix by.
     * @return The product between this matrix and the given scalar.
     */
    public Matrix2x3 multiply(float k) {
        return new Matrix2x3(
            this.m00() * k, this.m01() * k, this.m02() * k,
            this.m10() * k, this.m11() * k, this.m12() * k
        );
    }

    /**
     * Divides this matrix by the given scalar and returns the result.
     *
     * @param k The scalar to divide this matrix by.
     * @return The division between this matrix and the given scalar.
     */
    public Matrix2x3 divide(float k) {
        return new Matrix2x3(
            this.m00() / k, this.m01() / k, this.m02() / k,
            this.m10() / k, this.m11() / k, this.m12() / k
        );
    }

    /**
     * Returns the first row of this matrix as a {@link Vector3}.
     *
     * @return The first row of this matrix.
     * @see Matrix2x3#row(int)
     */
    public Vector3 row0() {
        return new Vector3(this.m00(), this.m01(), this.m02());
    }

    /**
     * Returns the second row of this matrix as a {@link Vector3}.
     *
     * @return The second row of this matrix.
     * @see Matrix2x3#row(int)
     */
    public Vector3 row1() {
        return new Vector3(this.m10(), this.m11(), this.m12());
    }

    /**
     * Returns the row at the given index as a {@link Vector3}.
     *
     * @param i The index of the requested row. Must be either 0 or 1.
     * @return The row at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector3 row(int i) {
        return switch (i) {
            case 0 -> this.row0();
            case 1 -> this.row1();
            default -> throw new IndexOutOfBoundsException("Row " + i + " is out of bounds for a 2x3 matrix");
        };
    }

    /**
     * Returns the first column of this matrix as a {@link Vector2}.
     *
     * @return The first column of this matrix.
     * @see Matrix2x3#column(int)
     */
    public Vector2 column0() {
        return new Vector2(this.m00(), this.m10());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector2}.
     *
     * @return The second column of this matrix.
     * @see Matrix2x3#column(int)
     */
    public Vector2 column1() {
        return new Vector2(this.m01(), this.m11());
    }

    /**
     * Returns the second column of this matrix as a {@link Vector2}.
     *
     * @return The second column of this matrix.
     * @see Matrix2x3#column(int)
     */
    public Vector2 column2() {
        return new Vector2(this.m02(), this.m12());
    }

    /**
     * Returns the column at the given index as a {@link Vector2}.
     *
     * @param i The index of the requested column. Must be either 0, 1, or 2.
     * @return The column at the given index.
     * @throws IndexOutOfBoundsException If the given index is out of bounds.
     */
    public Vector2 column(int i) {
        return switch (i) {
            case 0 -> this.column0();
            case 1 -> this.column1();
            case 2 -> this.column2();
            default -> throw new IndexOutOfBoundsException("Column " + i + " is out of bounds for a 2x3 matrix");
        };
    }

    /**
     * Multiplies this matrix by the vector with the given components and returns the result.
     *
     * @param x The vector's x component.
     * @param y The vector's y component.
     * @return The product of this matrix by the vector with the given components.
     */
    public Vector2 multiply(float x, float y, float z) {
        return new Vector2(
            this.m00() * x + this.m01() * y + this.m02() * z,
            this.m10() * x + this.m11() * y + this.m12() * z
        );
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     *
     * @param v The vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector2 multiply(Vector3 v) {
        return this.multiply(v.x(), v.y(), v.z());
    }

    /**
     * Multiplies this matrix by the given vector and returns the result.
     * <p>
     *     This method can be used to simplify the transformation of a 2D vector by a 2x3 transformation matrix.
     * </p>
     *
     * @param xy The x and y components of the vector to multiply this matrix by.
     * @param z The z component of the vector to multiply this matrix by.
     * @return The product of this matrix by the given vector.
     */
    public Vector2 multiply(Vector2 xy, float z) {
        return this.multiply(xy.x(), xy.y(), z);
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     *
     * @param m The matrix to multiply this one by.
     * @return The product between this matrix and the given one.
     */
    public Matrix2x3 multiply(Matrix3 m) {
        return new Matrix2x3(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m.m20(), this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m.m21(), this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m.m22(),
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m.m20(), this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m.m21(), this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m.m22()
        );
    }

    /**
     * Multiplies this matrix by the given one and returns the result.
     * <p>
     *     This method can be used to simplify the composition of two 2x3 transformation matrices.
     * </p>
     *
     * @param m The matrix to multiply this one by.
     * @param m20 First element of the third row.
     * @param m21 Second element of the third row.
     * @param m22 Third element of the third row.
     * @return The product between this matrix and the given one.
     */
    public Matrix2x3 multiply(Matrix2x3 m, float m20, float m21, float m22) {
        return new Matrix2x3(
            this.m00() * m.m00() + this.m01() * m.m10() + this.m02() * m20, this.m00() * m.m01() + this.m01() * m.m11() + this.m02() * m21, this.m00() * m.m02() + this.m01() * m.m12() + this.m02() * m22,
            this.m10() * m.m00() + this.m11() * m.m10() + this.m12() * m20, this.m10() * m.m01() + this.m11() * m.m11() + this.m12() * m21, this.m10() * m.m02() + this.m11() * m.m12() + this.m12() * m22
        );
    }

    /**
     * Returns the inverse of this matrix, under the assumption that it is an affine transformation matrix.
     * The result is undefined if the determinant of the basis is zero.
     *
     * @return The affine inverse of this matrix.
     */
    public Matrix2x3 affineInverse() {
        var det = this.m00() * this.m11() - this.m01() * this.m10();
        return new Matrix2x3(
            this.m11() / det, -this.m01() / det, -this.m00() / det * this.m02() - this.m10() / det * this.m12(),
            -this.m10() / det, this.m00() / det, -this.m01() / det * this.m02() - this.m11() / det * this.m12()
        );
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
    public Matrix2x3 lerp(Matrix2x3 to, float weight) {
        return new Matrix2x3(
            MathUtils.lerp(this.m00(), to.m00(), weight), MathUtils.lerp(this.m01(), to.m01(), weight), MathUtils.lerp(this.m02(), to.m02(), weight),
            MathUtils.lerp(this.m10(), to.m10(), weight), MathUtils.lerp(this.m11(), to.m11(), weight), MathUtils.lerp(this.m12(), to.m12(), weight)
        );
    }

    /**
     * Returns a matrix with all elements in absolute value.
     *
     * @return A matrix with all elements in absolute value.
     */
    public Matrix2x3 abs() {
        return new Matrix2x3(
            Math.abs(this.m00()), Math.abs(this.m01()), Math.abs(this.m02()),
            Math.abs(this.m10()), Math.abs(this.m11()), Math.abs(this.m12())
        );
    }

    /**
     * Returns a matrix with the signs of the elements of this matrix.
     *
     * @return A matrix with all elements representing the sign of this matrix.
     */
    public Matrix2x3 sign() {
        return new Matrix2x3(
            Math.signum(this.m00()), Math.signum(this.m01()), Math.signum(this.m02()),
            Math.signum(this.m10()), Math.signum(this.m11()), Math.signum(this.m12())
        );
    }

    /**
     * Returns a matrix with all elements rounded to the nearest integer.
     *
     * @return A matrix with all elements rounded to the nearest integer.
     */
    public Matrix2x3 round() {
        return new Matrix2x3(
            Math.round(this.m00()), Math.round(this.m01()), Math.round(this.m02()),
            Math.round(this.m10()), Math.round(this.m11()), Math.round(this.m12())
        );
    }

    /**
     * Returns a matrix with all elements rounded up.
     *
     * @return A matrix with all elements rounded up.
     */
    public Matrix2x3 ceil() {
        return new Matrix2x3(
            (float) Math.ceil(this.m00()), (float) Math.ceil(this.m01()), (float) Math.ceil(this.m02()),
            (float) Math.ceil(this.m10()), (float) Math.ceil(this.m11()), (float) Math.ceil(this.m12())
        );
    }

    /**
     * Returns a matrix with all elements rounded down.
     *
     * @return A matrix with all elements rounded down.
     */
    public Matrix2x3 floor() {
        return new Matrix2x3(
            (float) Math.floor(this.m00()), (float) Math.floor(this.m01()), (float) Math.floor(this.m02()),
            (float) Math.floor(this.m10()), (float) Math.floor(this.m11()), (float) Math.floor(this.m12())
        );
    }

    /**
     * Constructs an orthonormal matrix from the columns of this matrix using the Gram-Schmidt procedure.
     *
     * @return This matrix with orthogonal columns of unit length.
     */
    public Matrix2x3 orthonormalized() {
        var v0 = this.column0();
        var v1 = this.column1();
        var v2 = this.column2();
        v1 = v1.minus(v1.project(v0));
        v2 = v2.minus(v2.project(v0)).minus(v2.project(v1));
        return Matrix2x3.fromColumns(v0.normalized(), v1.normalized(), v2.normalized());
    }

    /**
     * Checks if this matrix is approximately equal to the given one using {@link MathUtils#EPSILON}.
     *
     * @param m The second matrix.
     * @return True if this matrix is approximately equal to the given one, otherwise false.
     */
    public boolean equalsApprox(Matrix2x3 m) {
        return MathUtils.equalsApprox(this.m00(), m.m00()) && MathUtils.equalsApprox(this.m01(), m.m01()) && MathUtils.equalsApprox(this.m02(), m.m02())
            && MathUtils.equalsApprox(this.m10(), m.m10()) && MathUtils.equalsApprox(this.m11(), m.m11()) && MathUtils.equalsApprox(this.m12(), m.m12());
    }

    /**
     * Returns a 2x3 matrix from the given rows.
     *
     * @param row0 The first row.
     * @param row1 The second row.
     * @return A 2x3 matrix from the given rows.
     */
    public static Matrix2x3 fromRows(Vector3 row0, Vector3 row1) {
        return new Matrix2x3(row0.x(), row0.y(), row0.z(), row1.x(), row1.y(), row1.z());
    }

    /**
     * Returns a 2x3 matrix from the given columns.
     *
     * @param column0 The first column.
     * @param column1 The second column.
     * @return A 2x3 matrix from the given columns.
     */
    public static Matrix2x3 fromColumns(Vector2 column0, Vector2 column1, Vector2 column2) {
        return new Matrix2x3(column0.x(), column1.x(), column2.x(), column0.y(), column1.y(), column2.y());
    }

    /**
     * Returns a 2x3 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param x Translation on the x axis.
     * @param y Translation on the y axis.
     * @return A 2x3 matrix representing a translation in a 2D space.
     */
    public static Matrix2x3 translation(float x, float y) {
        return new Matrix2x3(1.0f, 0.0f, x, 0.0f, 1.0f, y);
    }

    /**
     * Returns a 2x3 matrix representing a translation in a 2D space.
     * <p>
     *     A translation can be applied to a vector by multiplying it with this matrix as {@code m.multiply(v, 1.0f)}.
     * </p>
     *
     * @param t The translation vector.
     * @return A 2x3 matrix representing a translation in a 2D space.
     */
    public static Matrix2x3 translation(Vector2 t) {
        return translation(t.x(), t.y());
    }

    /**
     * Returns a 2x3 matrix representing a rotation by the given angle in a 2D space.
     *
     * @param angle The rotation angle in radians.
     * @return A 2x3 matrix representing a rotation by the given angle in a 2D space.
     */
    public static Matrix2x3 rotation(double angle) {
        var sin = (float) Math.sin(angle);
        var cos = (float) Math.cos(angle);
        return new Matrix2x3(cos, -sin, 0.0f, sin, cos, 0.0f);
    }

    /**
     * Returns a 2x3 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param x Scale factor on the x axis.
     * @param y Scale factor on the y axis.
     * @return A 2x3 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2x3 scaling(float x, float y) {
        return new Matrix2x3(x, 0.0f, 0.0f, 0.0f, y, 0.0f);
    }

    /**
     * Returns a 2x3 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param v A vector representing the scale factor on the x and y axes.
     * @return A 2x3 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2x3 scaling(Vector2 v) {
        return scaling(v.x(), v.y());
    }

    /**
     * Returns a 2x3 matrix representing a scaling by the given factor in a 2D space.
     *
     * @param scale Scale factor on the x and y axes.
     * @return A 2x3 matrix representing a scaling by the given factor in a 2D space.
     */
    public static Matrix2x3 scaling(float scale) {
        return scaling(scale, scale);
    }

    /**
     * Returns a 2x3 matrix representing a shear transformation by the given angles in a 2D space.
     *
     * @param x Angle on the x axis in radians.
     * @param y Angle on the y axis in radians.
     * @return A 2x3 matrix representing a shear transformation by the given angles in a 2D space.
     */
    public static Matrix2x3 shearing(double x, double y) {
        return new Matrix2x3(1.0f, (float) Math.tan(x), 0.0f, (float) Math.tan(y), 1.0f, 0.0f);
    }

    /**
     * Returns a 2x3 matrix representing a shear transformation by the given angles in a 2D space.
     *
     * @param v A vector representing the shear angle on the x and y axes.
     * @return A 2x3 matrix representing a shear transformation by the given angles in a 2D space.
     */
    public static Matrix2x3 shearing(Vector2 v) {
        return shearing(v.x(), v.y());
    }

    /**
     * Returns a 2x3 transformation matrix.
     * <p>
     *     The scaling is applied first, then the rotation, then the translation.
     * </p>
     *
     * @param tx Translation on the x axis.
     * @param ty Translation on the y axis.
     * @param rotation Rotation angle in radians.
     * @param sx Scale on the x axis.
     * @param sy Scale on the y axis.
     * @return A 2x3 transformation matrix.
     */
    public static Matrix2x3 transformation(float tx, float ty, double rotation, float sx, float sy) {
        var sin = (float) Math.sin(rotation);
        var cos = (float) Math.cos(rotation);
        return new Matrix2x3(cos * sx, -sin * sy, tx, sin * sx, cos * sy, ty);
    }

    /**
     * Returns a 2x3 transformation matrix.
     * <p>
     *     The scaling is applied first, then the rotation, then the translation.
     * </p>
     *
     * @param translation Translation vector.
     * @param rotation Rotation angle in radians.
     * @param sx Scale on the x axis.
     * @param sy Scale on the y axis.
     * @return A 2x3 transformation matrix.
     */
    public static Matrix2x3 transformation(Vector2 translation, double rotation, float sx, float sy) {
        return transformation(translation.x(), translation.y(), rotation, sx, sy);
    }

    /**
     * Returns a 2x3 transformation matrix.
     * <p>
     *     The scaling is applied first, then the rotation, then the translation.
     * </p>
     *
     * @param tx Translation on the x axis.
     * @param ty Translation on the y axis.
     * @param rotation Rotation angle in radians.
     * @param scale Scale vector.
     * @return A 2x3 transformation matrix.
     */
    public static Matrix2x3 transformation(float tx, float ty, double rotation, Vector2 scale) {
        return transformation(tx, ty, rotation, scale.x(), scale.y());
    }

    /**
     * Returns a 2x3 transformation matrix.
     * <p>
     *     The scaling is applied first, then the rotation, then the translation.
     * </p>
     *
     * @param translation Translation vector.
     * @param rotation Rotation angle in radians.
     * @param scale Scale vector.
     * @return A 2x3 transformation matrix.
     */
    public static Matrix2x3 transformation(Vector2 translation, double rotation, Vector2 scale) {
        return transformation(translation.x(), translation.y(), rotation, scale.x(), scale.y());
    }
}